package z.disklru.cache.lib.scanner.strategy;

import java.util.PriorityQueue;

import z.disklru.cache.lib.scanner.file.PriorityFile;


/**
 * Created by Administrator on 2017/6/12.
 */
public interface FileSizeOverFlowStrategy {

    long onOverFlow(final long curSize, final long maxSize, String dir, final PriorityQueue<PriorityFile> queue);
}
