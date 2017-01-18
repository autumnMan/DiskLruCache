package org.z.disklrucache;

import android.app.Application;

/**
 * Created by zwh on 2017/1/18.
 */
public class App extends Application {
    public static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }
}
