package z.disklru.cache.lib.scanner.file;

import java.io.File;

import z.disklru.cache.lib.scanner.strategy.FileCacheStrategy;

/**
 * 基于文件修改时间和重要性作为排序依据：<br/>
 * 1、文件越重要，就会越往后排<br/>
 * 2、文件最后修改时间越接近当前时间{@link System#currentTimeMillis()}，就会越往后排<br/>
 * 3、优先按文件重要性排序，文件重要性相同的就按规则2排序<br/>
 */
public class TimeLevelFile extends PriorityFile{
    private File file;
    private FileCacheStrategy fileStrategy;
    private int cacheLevel = Integer.MIN_VALUE;

    public TimeLevelFile(File file, FileCacheStrategy fileStrategy) {
        this.file = file;
        this.fileStrategy = fileStrategy;
    }

    @Override
    public long fileSize() {
        return file.length();
    }

    public long lastModifyTime() {
        return file.lastModified();
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
        TimeLevelFile another = (TimeLevelFile) a;

        final int myImportantLevel = importantLevel();
        final int anotherImportantLevel = another.importantLevel();

        final long myFileModifyTime = lastModifyTime();
        final long anotherFileModifyTime = another.lastModifyTime();


        if (myImportantLevel == anotherImportantLevel) {
            final long currentTime = System.currentTimeMillis();
            final long diff1 = currentTime - myFileModifyTime;
            final long diff2 = currentTime - anotherFileModifyTime;
            //1、两个都大于等于0
            //2、第一个小于0，第二个大于等于0
            //3、第一个大于等于0，第二个小于0
            //4、两个都小于0
            if (diff1 >= 0 && diff2 >= 0) {
                //重要程度相同，那就比较文件修改时间，文件修改时间越靠近当前时间重要程度越高，需要排在后面
                return diff1 < diff2 ? 1 : -1;
            } else {
                //用户修改了系统时间或其它问题，导致当前系统时间小于文件最终修改时间
                //小于0的需要往前排
                if (diff1 < 0 && diff2 >= 0) {
                    return -1;
                } else if (diff1 >= 0 && diff2 < 0) {
                    return 1;
                } else {
                    return diff1 > diff2 ? 1 : -1;
                }
            }
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
        builder.append(" -- ").append("last modify time: ").append(lastModifyTime());
        builder.append(" -- ").append(" level : ").append(importantLevel());
        return builder.toString();
    }

}
