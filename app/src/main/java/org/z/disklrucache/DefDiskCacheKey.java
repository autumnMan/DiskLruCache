package org.z.disklrucache;

import z.disklru.cache.lib.DiskCacheKey;

/**
 * 默认的缓存文件名
 */
public class DefDiskCacheKey implements DiskCacheKey {
    private String ori;

    public DefDiskCacheKey(String ori) {
        this.ori = ori;
    }

    @Override
    public String generateKey() {
        return ori;
    }
}
