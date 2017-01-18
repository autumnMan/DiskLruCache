package org.z.disklrucache.writer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * 将文本写入文件
 */
public class CacheStringWriter extends BaseCacheWriter {
    private String msg;

    public CacheStringWriter(String msg) {
        this.msg = msg;
    }

    @Override
    public boolean write(File file) {
        OutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            os.write(msg.getBytes());
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            closeStream(os);
        }
        return true;
    }
}
