package z.disklru.cache.lib.scanner.file;

import java.io.File;

import z.disklru.cache.lib.scanner.strategy.FileCacheStrategy;

/**
 * Created by Administrator on 2017/2/27.
 */
public abstract class PriorityFile implements Comparable<PriorityFile>{
    protected final File file;
    protected final FileCacheStrategy fileStrategy;

    private int cacheLevel = Integer.MIN_VALUE;

    public PriorityFile(File file, FileCacheStrategy fileCacheStrategy) {
        this.file = file;
        this.fileStrategy = fileCacheStrategy;
    }

    public long fileSize() {
        return file.length();
    }

    public void deleteFile() {
        file.delete();
    }

    public int importantLevel() {
        if (cacheLevel == Integer.MIN_VALUE) {
            //增加缓存，避免重复计算
            cacheLevel = fileStrategy.importantLevel(file);
        }
        return cacheLevel;
    }
}
