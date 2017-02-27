package z.disklru.cache.lib.scanner;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Iterator;
import java.util.PriorityQueue;

/**
 * Created by Mr-Z on 2017/2/25.
 */
public class ScanDiskSizeWorker implements Runnable{
    private File mDir;
    private int mMaxSize;
    private static PriorityQueue<PriorityFile> priorityQueue = new PriorityQueue<PriorityFile>();
    private FileCacheStrategy mFileStrategy;

    public ScanDiskSizeWorker(String cacheDir, int maxSize) {
        this(cacheDir, maxSize, null);
    }

    public ScanDiskSizeWorker(String cacheDir, int maxSize, FileCacheStrategy fileStrategy) {
        mDir = new File(cacheDir);
        mMaxSize = maxSize;
        mFileStrategy = fileStrategy == null ? new DefFileStrategy() : fileStrategy;
    }

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
                    priorityQueue.offer(new PriorityFile(f, mFileStrategy));
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
