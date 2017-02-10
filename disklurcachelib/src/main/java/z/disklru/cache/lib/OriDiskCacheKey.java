package z.disklru.cache.lib;

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
        return mOriginKey;
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
