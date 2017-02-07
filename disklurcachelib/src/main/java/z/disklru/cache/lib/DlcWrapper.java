package z.disklru.cache.lib;

import java.io.File;
import java.io.IOException;

import z.disklru.cache.lib.core.DiskLruCache;

/**
 * 对DiskLruCache进行封装，实现一些自定义操作，例如自定义文件名等
 */
public class DlcWrapper implements DiskCache{
    private static final String TAG = "DiskLruCacheWrapper";

    private static final int APP_VERSION = 1;
    private static final int VALUE_COUNT = 1;

    private final DlcWriteLocker writeLocker = new DlcWriteLocker();
    private final File directory;
    private final long maxSize;
    private DiskLruCache diskLruCache;
    private DiskCacheLog log;

    /**
     * 获取一个对应的缓存目录
     *
     * @param directory 文件缓存目录
     * @param maxSize 最大缓存大小，单位字节
     */
    public DlcWrapper(File directory, long maxSize, DiskCacheLog logPrinter) {
        this.directory = directory;
        this.maxSize = maxSize;
        this.log = logPrinter;
    }

    private synchronized DiskLruCache getDiskCache() throws IOException {
        if (diskLruCache == null) {
            diskLruCache = DiskLruCache.open(directory, APP_VERSION, VALUE_COUNT, maxSize);
        }
        return diskLruCache;
    }

    private synchronized void resetDiskCache() {
        diskLruCache = null;
    }

    @Override
    public File get(DiskCacheKey key) {
        final String safeKey = key.generateKey();
        File result = null;
        try {
            //It is possible that the there will be a put in between these two gets. If so that shouldn't be a problem
            //because we will always put the same value at the same key so our input streams will still represent
            //the same data
            final DiskLruCache.Value value = getDiskCache().get(safeKey);
            if (value != null) {
                result = value.getFile(0);
            }
        } catch (IOException e) {
//            if (Log.isLoggable(TAG, Log.WARN)) {
//                Log.w(TAG, "Unable to get from disk cache", e);
//            }
            if (log != null) {
                log.w(TAG, "Unable to get from disk cache", e);
            }
        }
        return result;
    }

    @Override
    public boolean put(DiskCacheKey key, Writer writer) {
        return doWrite(key, writer, false);
    }

    private boolean doWrite(DiskCacheKey key, Writer writer, boolean appendOrOverride) {
        final String safeKey = key.generateKey();
        writeLocker.acquire(key);
        try {
            DiskLruCache.Editor editor = getDiskCache().edit(safeKey);
            // Editor will be null if there are two concurrent puts. In the worst case we will just silently fail.
            if (editor != null) {
                try {
                    File file = editor.getFile(0);
                    if (writer.write(file)) {
                        editor.commit(appendOrOverride);
                        return true;
                    }
                } finally {
                    editor.abortUnlessCommitted();
                }
            }
        } catch (IOException e) {
//            if (Log.isLoggable(TAG, Log.WARN)) {
//                Log.w(TAG, "Unable to put to disk cache", e);
//            }
            if (log != null) {
                log.w(TAG, "Unable to put to disk cache", e);
            }
        } finally {
            writeLocker.release(key);
        }
        return false;
    }

    @Override
    public boolean appendContent(DiskCacheKey key, Writer writer) {
        return doWrite(key, writer, true);
    }

    @Override
    public boolean delete(DiskCacheKey key) {
        final String safeKey = key.generateKey();
        try {
            return getDiskCache().remove(safeKey);
        } catch (IOException e) {
//            if (Log.isLoggable(TAG, Log.WARN)) {
//                Log.w(TAG, "Unable to delete from disk cache", e);
//            }
            if (log != null) {
                log.w(TAG, "Unable to delete from disk cache", e);
            }
        }
        return false;
    }

    @Override
    public synchronized boolean clear() {
        try {
            getDiskCache().delete();
            resetDiskCache();
            return true;
        }  catch (IOException e) {
//            if (Log.isLoggable(TAG, Log.WARN)) {
//                Log.w(TAG, "Unable to clear disk cache", e);
//            }
            if (log != null) {
                log.w(TAG, "Unable to clear disk cache", e);
            }
        }
        return false;
    }

}
