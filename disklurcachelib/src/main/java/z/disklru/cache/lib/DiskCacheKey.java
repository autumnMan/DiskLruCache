package z.disklru.cache.lib;

/**
 * 保存缓存文件对应的key，一般就是文件名作为key
 */
public interface DiskCacheKey {
    String STRING_CHARSET_NAME = "UTF-8";

    /**生成最终的磁盘缓存文件对应的文件名**/
    String generateKey();

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();
}
