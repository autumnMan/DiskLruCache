package z.disklru.cache.lib.inter;


import java.io.File;

/**
 * An interface for writing to and reading from a disk cache.
 */
public interface DiskCache {
    /**
     * An interface to actually write data to a key in the disk cache.
     */
    interface Writer {
        /**
         * Writes data to the file and returns true if the write was successful and should be committed, and
         * false if the write should be aborted.
         *
         * @param file The File the Writer should write to.
         */
        boolean write(File file);
    }

    /**
     * Get the cache for the value at the given key.
     *
     * <p>
     *     Note - This is potentially dangerous, someone may write a new value to the file at any point in timeand we
     *     won't know about it.
     * </p>
     *
     * @param key The key in the cache.
     * @return An InputStream representing the data at key at the time get is called.
     */
    File get(DiskCacheKey key);

    /**
     * Write to a key in the cache. {@link Writer} is used so that the cache implementation can perform actions after
     * the write finishes, like commit (via atomic file rename).
     *
     * @param key The key to write to.
     * @param writer An interface that will write data given an OutputStream for the key.
     * @return true means  success
     */
    boolean put(DiskCacheKey key, Writer writer);

    /**
     * Append content to a key in the cache.
     * Like {@link #put(DiskCacheKey, Writer)} but this will not override the cache
     * @param key The key to append to.
     * @param writer An interface that will write data given an OutputStream for the key.
     * @return true means  success
     */
    boolean appendContent(DiskCacheKey key, Writer writer);

    /**
     * Remove the key and value from the cache.
     *
     * @param key The key to remove.
     * @return true means  success
     */
    boolean delete(DiskCacheKey key);

    /**
     * Clear the cache.
     * @return true means  success
     */
    boolean clear();
}

