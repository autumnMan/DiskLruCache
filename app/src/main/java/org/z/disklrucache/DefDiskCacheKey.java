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

    @Override
    public boolean equals(Object o) {
        boolean equ = false;
        if (o != null && o.getClass().getName().equals(this.getClass().getName())) {
            equ = ori.equals(((DefDiskCacheKey) o).ori);
        }
        return equ;
    }

    @Override
    public int hashCode() {
        if (ori != null) {
            return ori.hashCode();
        }
        return super.hashCode();
    }
}
