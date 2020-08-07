package com.huyu.sdk.listener;

import com.huyu.sdk.data.bean.HYUser;

/**
 * 文件名：HY_UserListener
 * 创建日期：2020/8/4 17:03
 * 描述：TODO
 */
public interface HYSDKListener {
    void onSwitchAccount(HYUser user);

    void onLogout();
}
