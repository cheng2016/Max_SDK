package com.huyu.sdk.base;


import com.huyu.sdk.listener.HYSDKListener;

/**
 * 文件名：BaseChannel
 * 创建日期：2020/8/7 14:57
 * 描述：渠道基类
 */
public abstract class BaseChannel implements SDKProxy {

    public HYSDKListener mHYSDKListener;

    @Override
    public void setHYSDKListener(HYSDKListener listener) {
        this.mHYSDKListener = listener;
    }
}
