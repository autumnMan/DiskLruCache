package z.disklru.cache.lib.scanner;

import java.io.File;
import java.util.PriorityQueue;

import z.disklru.cache.lib.scanner.file.PriorityFile;
import z.disklru.cache.lib.scanner.file.TimeLevelFile;
import z.disklru.cache.lib.scanner.strategy.FileCacheStrategy;
import z.disklru.cache.lib.scanner.strategy.FileSizeOverFlowStrategy;

/**
 * Created by Administrator on 2017/2/27.
 */
public class TimeScanner extends AbsScanner{
    private static PriorityQueue<PriorityFile> priorityQueue = new PriorityQueue<>();

    public TimeScanner(String cacheDir, int maxSize, FileCacheStrategy fileStrategy, FileSizeOverFlowStrategy fileSizeOverFlowStrategy) {
        super(cacheDir, maxSize, fileStrategy, fileSizeOverFlowStrategy, priorityQueue);
    }

    @Override
    public PriorityFile createPriorityFile(File file, FileCacheStrategy fileCacheStrategy) {
        return new TimeLevelFile(file, fileCacheStrategy);
    }
}
