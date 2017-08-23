package z.disklru.cache.lib.scanner;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Iterator;
import java.util.PriorityQueue;

import z.disklru.cache.lib.scanner.file.PriorityFile;
import z.disklru.cache.lib.scanner.strategy.DefFileSizeOverFlowStrategy;
import z.disklru.cache.lib.scanner.strategy.DefFileStrategy;
import z.disklru.cache.lib.scanner.strategy.FileCacheStrategy;
import z.disklru.cache.lib.scanner.strategy.FileSizeOverFlowStrategy;

/**
 * Created by Mr-Z on 2017/2/25.
 */
public abstract class AbsScanner implements Runnable{
    private final File mDir;
    private final long mMaxSize;
    private final PriorityQueue<PriorityFile> priorityQueue;
    private final FileCacheStrategy mFileStrategy;
    private final FileSizeOverFlowStrategy mFileSizeOverFlowStrategy;

    public AbsScanner(String cacheDir, long maxSize) {
        this(cacheDir, maxSize, null, null, null);
    }

    public AbsScanner(String cacheDir, long maxSize, FileCacheStrategy fileStrategy) {
        this(cacheDir, maxSize, fileStrategy, null, null);
    }

    public AbsScanner(String cacheDir, long maxSize, PriorityQueue<PriorityFile> queue) {
        this(cacheDir, maxSize, null, null, queue);
    }

    public AbsScanner(String cacheDir, long maxSize, FileCacheStrategy fileStrategy,
                      FileSizeOverFlowStrategy fileSizeOverFlowStrategy, PriorityQueue<PriorityFile> queue) {
        mDir = new File(cacheDir);
        mMaxSize = maxSize;
        mFileStrategy = fileStrategy == null ? new DefFileStrategy() : fileStrategy;
        mFileSizeOverFlowStrategy = fileSizeOverFlowStrategy == null ? new DefFileSizeOverFlowStrategy() : fileSizeOverFlowStrategy;
        priorityQueue = queue == null ? new PriorityQueue<PriorityFile>() : queue;
    }

    public abstract PriorityFile createPriorityFile(File file, FileCacheStrategy fileCacheStrategy);

    @Override
    public void run() {
        try {
            priorityQueue.clear();

            //start to scan file
            final File[] files = mDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    return !".".equals(filename) && !"..".equals(filename);
                }
            });

            if (files != null && files.length > 0) {
                for (File f : files) {
                    priorityQueue.offer(createPriorityFile(f, mFileStrategy));
                }
            }

            if (!priorityQueue.isEmpty()) {
                tryToClean();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            priorityQueue.clear();
        }

    }

    protected void tryToClean() {
        //开始检查所有文件的总大小
        final Iterator<PriorityFile> iterator = priorityQueue.iterator();
        final long percent80 = mMaxSize * 8 / 10;
        long size = 0;
        while (iterator.hasNext()) {
            size += iterator.next().fileSize();
        }

        if (size >= percent80 && mFileSizeOverFlowStrategy != null) {
            //文件夹的大小达到限制大小的80%时，可以先做一些额外处理来降低文件夹大小
            size = mFileSizeOverFlowStrategy.onOverFlow(size, percent80, mDir.getAbsolutePath(), priorityQueue);
        }

        PriorityFile tmpFile = null;
        while (size >= mMaxSize) {
            //文件的总大小超出范围，则从头开始删除
            tmpFile = priorityQueue.poll();
            size -= tmpFile.fileSize();
            tmpFile.deleteFile();
        }
    }

}
