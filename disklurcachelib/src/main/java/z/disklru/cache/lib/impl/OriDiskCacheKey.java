package z.disklru.cache.lib.impl;

import z.disklru.cache.lib.inter.DiskCacheKey;
import z.disklru.cache.lib.util.Md5Util;

/**
 * default disk cache key
 */
public class OriDiskCacheKey implements DiskCacheKey {
    private String mOriginKey;

    public OriDiskCacheKey(String key) {
        mOriginKey = key;
    }

    @Override
    public String generateKey() {
        //避免中文乱码
        return Md5Util.toMd5Hex(mOriginKey);
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        return mOriginKey.equals(((OriDiskCacheKey)obj).mOriginKey);
    }

    @Override
    public int hashCode() {
        return mOriginKey.hashCode();
    }
}
