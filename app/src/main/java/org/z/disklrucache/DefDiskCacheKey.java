package org.z.disklrucache;

import z.disklru.cache.lib.Md5Util;
import z.disklru.cache.lib.inter.DiskCacheKey;

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
        //避免中文乱码
        return Md5Util.toMd5Hex(ori);
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
