package z.disklru.cache.lib.scanner.strategy;

import java.io.File;

/**
 * 文件缓存策略，可自定义文件的重要级别，越重要的就放在越后面；
 * 如果两个文件的重要性相同，那就按照文件大小来排序，文件大小越小的就放在越后面
 */
public interface FileCacheStrategy {
    byte VERY_IMPORTANT = 1;

    byte IMPORTANT = 2;

    byte DO_NOT_CARE = 3;

    int importantLevel(File file);
}
