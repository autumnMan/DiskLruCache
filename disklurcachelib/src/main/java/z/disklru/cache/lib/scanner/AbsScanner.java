package z.disklru.cache.lib.scanner;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Iterator;
import java.util.PriorityQueue;

import z.disklru.cache.lib.scanner.file.PriorityFile;
import z.disklru.cache.lib.scanner.strategy.DefFileStrategy;
import z.disklru.cache.lib.scanner.strategy.FileCacheStrategy;

/**
 * Created by Mr-Z on 2017/2/25.
 */
public abstract class AbsScanner implements Runnable{
    private File mDir;
    private int mMaxSize;
    private PriorityQueue<PriorityFile> priorityQueue;
    private FileCacheStrategy mFileStrategy;

    public AbsScanner(String cacheDir, int maxSize) {
        this(cacheDir, maxSize, null, null);
    }

    public AbsScanner(String cacheDir, int maxSize, FileCacheStrategy fileStrategy) {
        this(cacheDir, maxSize, fileStrategy, null);
    }

    public AbsScanner(String cacheDir, int maxSize, PriorityQueue<PriorityFile> queue) {
        this(cacheDir, maxSize, null, queue);
    }

    public AbsScanner(String cacheDir, int maxSize, FileCacheStrategy fileStrategy, PriorityQueue<PriorityFile> queue) {
        mDir = new File(cacheDir);
        mMaxSize = maxSize;
        mFileStrategy = fileStrategy == null ? new DefFileStrategy() : fileStrategy;
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
                //开始检查所有文件的总大小
                final Iterator<PriorityFile> iterator = priorityQueue.iterator();
                long size = 0;
                while (iterator.hasNext()) {
                    size += iterator.next().fileSize();
                }

                PriorityFile tmpFile = null;
                while (size > mMaxSize) {
                    //文件的总大小超出范围，则从头开始删除
                    tmpFile = priorityQueue.poll();
                    size -= tmpFile.fileSize();
                    tmpFile.deleteFile();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
