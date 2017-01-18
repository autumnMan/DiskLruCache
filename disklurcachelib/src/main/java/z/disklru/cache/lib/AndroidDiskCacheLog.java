package z.disklru.cache.lib;

import android.util.Log;

public class AndroidDiskCacheLog implements DiskCacheLog {

    @Override
    public void v(String tag, String msg) {
        if (Log.isLoggable(tag, Log.VERBOSE)) {
            Log.v(tag, msg);
        }
    }

    @Override
    public void v(String tag, String msg, Throwable e) {
        if (Log.isLoggable(tag, Log.VERBOSE)) {
            Log.v(tag, msg, e);
        }
    }

    @Override
    public void d(String tag, String msg) {
        if (Log.isLoggable(tag, Log.DEBUG)) {
            Log.d(tag, msg);
        }
    }

    @Override
    public void d(String tag, String msg, Throwable e) {
        if (Log.isLoggable(tag, Log.DEBUG)) {
            Log.d(tag, msg, e);
        }
    }

    @Override
    public void i(String tag, String msg) {
        if (Log.isLoggable(tag, Log.INFO)) {
            Log.i(tag, msg);
        }
    }

    @Override
    public void i(String tag, String msg, Throwable e) {
        if (Log.isLoggable(tag, Log.INFO)) {
            Log.i(tag, msg, e);
        }
    }

    @Override
    public void w(String tag, String msg) {
        if (Log.isLoggable(tag, Log.WARN)) {
            Log.w(tag, msg);
        }
    }

    @Override
    public void w(String tag, String msg, Throwable e) {
        if (Log.isLoggable(tag, Log.WARN)) {
            Log.w(tag, msg, e);
        }
    }

    @Override
    public void e(String tag, String msg) {
        if (Log.isLoggable(tag, Log.ERROR)) {
            Log.e(tag, msg);
        }
    }

    @Override
    public void e(String tag, String msg, Throwable e) {
        if (Log.isLoggable(tag, Log.ERROR)) {
            Log.e(tag, msg, e);
        }
    }
}
