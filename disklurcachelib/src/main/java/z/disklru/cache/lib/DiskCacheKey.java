package z.disklru.cache.lib;

/**
 * 保存缓存文件对应的key，一般就是文件名作为key
 */
public interface DiskCacheKey {
    String STRING_CHARSET_NAME = "UTF-8";

    /**生成最终的磁盘缓存文件对应的文件名，注意文件名做好是引文或数字，避免引起乱码，可使用MD5对文件名进行加密后转16进制**/
    String generateKey();

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();
}
