package z.disklru.cache.lib;

public interface DiskCacheLog {
    void v(String tag, String msg);

    void v(String tag, String msg, Throwable e);

    void d(String tag, String msg);

    void d(String tag, String msg, Throwable e);

    void i(String tag, String msg);

    void i(String tag, String msg, Throwable e);

    void w(String tag, String msg);

    void w(String tag, String msg, Throwable e);

    void e(String tag, String msg);

    void e(String tag, String msg, Throwable e);
}
