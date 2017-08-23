package z.disklru.cache.lib.scanner.strategy;


import java.util.PriorityQueue;

import z.disklru.cache.lib.scanner.file.PriorityFile;

/**
 * Created by Administrator on 2017/6/12.
 */
public class DefFileSizeOverFlowStrategy implements FileSizeOverFlowStrategy {
    @Override
    public long onOverFlow(long curSize, long maxSize, String dir, final PriorityQueue<PriorityFile> queue) {
        //不做任何处理，直接返回当前大小
        return curSize;
    }
}
