package z.disklru.cache.lib.scanner.strategy;

import java.io.File;

/**
 * Created by Mr-Z on 2017/2/25.
 */
public class DefFileStrategy implements FileCacheStrategy {

    @Override
    public int importantLevel(File file) {
        return IMPORTANT;
    }
}
