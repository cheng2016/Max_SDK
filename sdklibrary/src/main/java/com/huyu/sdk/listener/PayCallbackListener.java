package com.huyu.sdk.listener;

import com.huyu.sdk.data.bean.PayParams;

/**
 * 文件名：PayCallbackListener
 * 创建日期：2020/8/4 11:18
 * 描述：TODO
 */
public interface PayCallbackListener {
    void onPaySuccess(PayParams payParams);
    void onPayCancel();
    void onPayFailed( String msg);
}
