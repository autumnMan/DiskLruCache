package org.z.disklrucache;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import org.z.disklrucache.writer.CacheStringWriter;

import z.disklru.cache.lib.AndroidDiskCacheLog;
import z.disklru.cache.lib.DlcWrapper;
import z.disklru.cache.lib.inter.DiskCache;

public class MainActivity extends Activity {
    private final int TESTING = 1;
    private final int TEST_FINISHED = 2;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TESTING:
                    Toast.makeText(App.app, "正在测试", Toast.LENGTH_SHORT).show();
                    break;
                case TEST_FINISHED:
                    Toast.makeText(App.app, "测试结束", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        writeMsg();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    private void writeMsg() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(TESTING);
                DiskCache diskCache = new DlcWrapper(App.app.getExternalCacheDir(), 119, //119个字节
                        new AndroidDiskCacheLog());
                CacheStringWriter writer = new CacheStringWriter("Hello world!");
                //每次写入12个字节，总共写入120个字节，10个文件，但是限制了总大小为119个字节
                //所以最终应该写入9个文件
                for (int i = 0; i < 10; ++i) {
                    diskCache.put(new DefDiskCacheKey("test"), writer);
                }
                mHandler.sendEmptyMessage(TEST_FINISHED);
            }
        }).start();
    }

}
