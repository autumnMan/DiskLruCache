package z.disklru.cache.lib.core;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Writer;

/**
 * Created by Mr.Z on 2017/1/26.
 */

public class FileUtil {

    public static String getFileContentAsString(File file) {
        if (file == null) {
            return null;
        }
        String content = null;
        InputStreamReader is = null;
        try {
            is = new InputStreamReader(new FileInputStream(file));
            StringBuilder builder = new StringBuilder();
            char[] buf = new char[1024];
            int r = -1;
            while ((r = is.read(buf, 0, buf.length)) >= 0) {
                builder.append(buf, 0, r);
            }
            content = builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeStream(is);
        }
        return content;
    }

    public static void writeString2File(File file, String content) {
        if (file == null) {
            return;
        }
        Writer writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(content);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeStream(writer);
        }
    }

    public static void closeStream(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (Exception e) {
        }
    }
}
