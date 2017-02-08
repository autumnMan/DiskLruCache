package z.disklru.cache.lib;

import java.security.MessageDigest;

/**
 * Created by Administrator on 2017/2/8.
 */
public class Md5Util {
    private static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5', '6',
        '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private static final LruCache<DiskCacheKey, String> lruCache = new LruCache<>(1000);

    private Md5Util(){}

    public static String toMd5Hex(DiskCacheKey key) {
        String result = null;
        synchronized (lruCache) {
            result = lruCache.get(key);
        }
        if (result != null) {
            return result;
        }

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(key.generateKey().getBytes());

            result = bufferToHex(messageDigest.digest());
            synchronized (lruCache) {
                lruCache.put(key, result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = key.generateKey();
        }
        return result;
    }

    private static String bufferToHex(byte[] bytes) {
        final StringBuffer strHexString = new StringBuffer();
        for (byte b : bytes) {
            strHexString.append(HEX_DIGITS[(b & 0xf0) >> 4]);
            strHexString.append(HEX_DIGITS[b & 0xf]);
        }
        // 得到返回結果
        return strHexString.toString();
    }

}
