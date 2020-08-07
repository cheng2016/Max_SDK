package com.huyu.sdk.listener;

import com.huyu.sdk.data.bean.HYUser;

/**
 * 文件名：LoginCallBackListener
 * 创建日期：2020/8/4 11:13
 * 描述：TODO
 */
public interface LoginCallBackListener {
    void onLoginSuccess(HYUser user);

    void onLoginCancel();

    void onLoginFailed(String message);
}
