package z.disklru.cache.lib.scanner.file;

import java.io.File;

import z.disklru.cache.lib.scanner.strategy.FileCacheStrategy;

/**
 * 基于文件长度和重要性作为排序依据：<br/>
 * 1、文件越重要，就会越往后排<br/>
 * 2、文件大小越小，就会越往后排<br/>
 * 3、优先按文件重要性排序，文件重要性相同的就按规则2排序<br/>
 */
public class LengthLevelFile extends PriorityFile{
    private File file;
    private FileCacheStrategy fileStrategy;
    private int cacheLevel = Integer.MIN_VALUE;

    public LengthLevelFile(File file, FileCacheStrategy fileStrategy) {
        this.file = file;
        this.fileStrategy = fileStrategy;
    }

    @Override
    public long fileSize() {
        return file.length();
    }

    @Override
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

    @Override
    public int compareTo(PriorityFile a) {
        //如果返回小于0的数，则会放到前面
        //如果返回大于0的数，则会放到后面
        LengthLevelFile another = (LengthLevelFile) a;

        final int myImportantLevel = importantLevel();
        final int anotherImportantLevel = another.importantLevel();

        final long myFileSize = fileSize();
        final long anotherFileSize = another.fileSize();

        if (myImportantLevel == anotherImportantLevel) {
            //重要程度相同，那就比较文件大小，文件越大，重要程度越低，需要排在前头
            return myFileSize > anotherFileSize ? -1 : 1;
        } else {
            //level越大越次要
            return myImportantLevel > anotherImportantLevel ? -1 : 1;
        }
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(64);
        builder.append(file.getAbsolutePath());
        builder.append(" -- ").append("file size: ").append(fileSize());
        builder.append(" -- ").append(" level : ").append(importantLevel());
        return builder.toString();
    }
}
