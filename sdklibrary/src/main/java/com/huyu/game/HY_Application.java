package com.huyu.game;

import android.app.Application;

import com.huyu.sdk.HYSDK;

/**
 * 文件名：HY_Application
 * 创建日期：2020/8/7 18:20
 * 描述：TODO
 */
public class HY_Application extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        HYSDK.getInstance().initApplication(this);
    }
}
