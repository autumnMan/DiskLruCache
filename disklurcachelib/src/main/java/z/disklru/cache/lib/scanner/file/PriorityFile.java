package z.disklru.cache.lib.scanner.file;

/**
 * Created by Administrator on 2017/2/27.
 */
public abstract class PriorityFile implements Comparable<PriorityFile>{

    public abstract void deleteFile();

    public abstract long fileSize();
}
