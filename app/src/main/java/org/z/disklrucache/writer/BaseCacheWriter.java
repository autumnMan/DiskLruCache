package org.z.disklrucache.writer;

import java.io.Closeable;

import z.disklru.cache.lib.DiskCache;

/**
 * Created by zwh on 2017/1/18.
 */
public abstract class BaseCacheWriter implements DiskCache.Writer {

    protected static void closeStream(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
            }
        }
    }
}
