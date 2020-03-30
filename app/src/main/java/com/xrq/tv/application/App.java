package com.xrq.tv.application;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * @author 765773123
 * @date 2018/11/22
 */
public class App extends Application {
    private static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        Fresco.initialize(this);
    }

    public static App getApp() {
        return app;
    }

}
