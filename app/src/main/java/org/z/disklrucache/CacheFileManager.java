package org.z.disklrucache;

import z.disklru.cache.lib.AndroidDiskCacheLog;
import z.disklru.cache.lib.DiskCache;
import z.disklru.cache.lib.DiskLruCacheWrapper;

import android.support.v4.util.ArrayMap;

/**
 * 缓存文件管理类，管理每个缓存文件夹的大小
 */
public class CacheFileManager {
    public static DiskLruCacheWrapper.DiskDirCacheMap diskDirCacheMap;
    private static final CacheFileManager instance = new CacheFileManager();
    private DiskCache mCacheFileWrapper;

    private CacheFileManager() {
        diskDirCacheMap = DiskLruCacheWrapper.DiskDirCacheMap.get(new ArrayMap<String, DiskCache>());
    }

    public static CacheFileManager get() {
        return instance;
    }

    public DiskCache getCacheFileWrapper() {
        if (mCacheFileWrapper == null) {
            mCacheFileWrapper = diskDirCacheMap.getDiskLruCache(App.app.getExternalCacheDir(), 119, //119个字节
                    new AndroidDiskCacheLog());
        }
        return mCacheFileWrapper;
    }

}
