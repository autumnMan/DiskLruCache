package z.disklru.cache.lib.scanner;

import java.io.File;
import java.util.PriorityQueue;

import z.disklru.cache.lib.scanner.file.PriorityFile;
import z.disklru.cache.lib.scanner.file.TimeLevelFile;
import z.disklru.cache.lib.scanner.strategy.FileCacheStrategy;

/**
 * 基于文件夹内的文件个数进行限制，以时间和重要性作为排序依据，文件个数超出限制后将进行清理
 */
public class NumberScanner extends AbsScanner{
    private static PriorityQueue<PriorityFile> priorityQueue = new PriorityQueue<>();

    private int totalNumber;

    public NumberScanner(String cacheDir, int totalNumber, FileCacheStrategy fileStrategy) {
        super(cacheDir, 0, fileStrategy, priorityQueue);
        this.totalNumber = totalNumber;
    }

    @Override
    public PriorityFile createPriorityFile(File file, FileCacheStrategy fileCacheStrategy) {
        return new TimeLevelFile(file, fileCacheStrategy);
    }

    @Override
    protected void tryToClean() {
        int count = priorityQueue.size();
        PriorityFile tmpFile = null;
        while (count > totalNumber) {
            tmpFile = priorityQueue.poll();
            tmpFile.deleteFile();
            --count;
        }
    }
}
