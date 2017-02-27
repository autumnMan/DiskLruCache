package z.disklru.cache.lib;

import java.security.MessageDigest;

/**
 * Created by Administrator on 2017/2/8.
 */
public class Md5Util {
    private static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5', '6',
        '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private static final LruCache<String, String> lruCache = new LruCache<>(1000);

    private Md5Util(){}

    public static String toMd5Hex(String origin) {
        if (origin == null) {
            return "";
        }

        String result = null;
        synchronized (lruCache) {
            result = lruCache.get(origin);
        }
        if (result != null) {
            return result;
        }

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(origin.getBytes());

            result = bufferToHex(messageDigest.digest());
            synchronized (lruCache) {
                lruCache.put(origin, result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = origin;
        }
        return result;
    }

    private static String bufferToHex(byte[] bytes) {
        final StringBuilder strHexString = new StringBuilder();
        for (byte b : bytes) {
            strHexString.append(HEX_DIGITS[(b & 0xf0) >> 4]);
            strHexString.append(HEX_DIGITS[b & 0xf]);
        }
        // 得到返回結果
        return strHexString.toString();
    }

}
