package z.disklru.cache.lib.scanner;

import java.io.File;

/**
 * Created by Mr-Z on 2017/2/25.
 */
public interface FileCacheStrategy {
    byte VERY_IMPORTANT = 1;

    byte IMPORTANT = 2;

    byte DO_NOT_CARE = 3;

    int importantLevel(File file);
}
