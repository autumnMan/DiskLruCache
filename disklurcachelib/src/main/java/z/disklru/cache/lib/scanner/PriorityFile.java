package z.disklru.cache.lib.scanner;

import java.io.File;

/**
 * Created by Mr-Z on 2017/2/25.
 */
class PriorityFile implements Comparable<PriorityFile>{
    private File file;
    private FileCacheStrategy fileStrategy;

    PriorityFile(File file, FileCacheStrategy fileStrategy) {
        this.file = file;
        this.fileStrategy = fileStrategy;
    }

    public long fileSize() {
        return file.length();
    }

    public void deleteFile() {
        file.delete();
    }

    public int importantLevel() {
        return fileStrategy.importantLevel(file);
    }

    @Override
    public int compareTo(PriorityFile another) {
        //如果返回小于0的数，则会放到前面
        //如果返回大于0的数，则会放到后面

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
        builder.append(" -- ").append(" level : ").append(fileStrategy.importantLevel(file));
        return builder.toString();
    }
}
