package com.yangming.myproject;

import android.app.Application;

import baselibrary.util.DensityUtils;

/**
 * @author yangming on 2019/7/5
 */
public class MyApplication extends Application {
    private static MyApplication application;
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

        // 初始化屏幕适配
        DensityUtils.initAppDensity(this);
    }

    public static MyApplication getApplication() {
        return application;
    }
}
