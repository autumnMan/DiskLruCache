package z.disklru.cache.lib.inter;

import java.io.File;

/**
 * An interface to actually write data to a key in the disk cache.
 */
public interface DiskCacheWriter {
    /**
     * Writes data to the file and returns true if the write was successful and should be committed, and
     * false if the write should be aborted.
     *
     * @param file The File the Writer should write to.
     */
    boolean write(File file);
}
