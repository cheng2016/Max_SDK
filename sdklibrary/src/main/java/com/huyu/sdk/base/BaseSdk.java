package com.huyu.sdk.base;

import android.app.Activity;

import com.huyu.sdk.listener.ExitCallback;

/**
 * 文件名：BaseSdk
 * 创建日期：2020/9/30 9:55
 * 描述：TODO
 */
public abstract class BaseSdk implements SDKProxy{

    public abstract void exit(Activity activity, ExitCallback callback);
}
