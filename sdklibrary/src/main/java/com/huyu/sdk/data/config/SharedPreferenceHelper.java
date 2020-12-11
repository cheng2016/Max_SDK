package com.huyu.sdk.data.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.huyu.sdk.U9Platform;


public class SharedPreferenceHelper {
    /**
     * token: 登录令牌
     */
    public static final String ACCESSTOKEN = "token";
    /**
     * userId:  用户id   u9uid
     */
    public static final String USER_ID = "user_id";
    /**
     * hyUserId:  新版用户id
     */
    public static final String HY_USER_ID = "hy_uid";
    /**
     * channelUserId: 渠道用户ID， (guid)
     */
    public static final String CHANNEL_USER_ID = "channel_user_id";
    /**
     * channelUserName:渠道登录用户名
     */
    public static final String CHANNEL_USER_NAME = "channel_user_name";
    /**
     * 是否绑定字段
     */
    public static final String IS_BIND_ACCOUNT = "is_bind_account";

    /**
     * 登录密码
     */
    public static final String USER_PASSWORD = "user_password";

    /**
     * login type ： 登录类型： 0 表示账户登录，1 表示一键登录，2 表示Facebook登录
     */
    public static final String LOGIN_TYPE = "LoginType";

    /**
     * IsSwitchPayChannel  0 标识不切支付  1表示切支付 (默认为0)
     */
    public static final String IS_SWITCH_PAY_CHANNEL = "IsSwitchPayChannel  ";

    public static SharedPreferences getPreference() {
        return U9Platform.getInstance().getApplication().getSharedPreferences("u9", Context.MODE_PRIVATE);
    }


    public static String getAccessToken() {
        return getPreference().getString(ACCESSTOKEN, "");
    }

    public static void setAccessToken(String paramString) {
        getPreference().edit().putString(ACCESSTOKEN, paramString).commit();
    }

    public static String getUserId() {
        return getPreference().getString(USER_ID, "");
    }

    public static void setUserId(String userId) {
        getPreference().edit().putString(USER_ID, userId).commit();
    }

    public static String getHyUserId() {
        return getPreference().getString(HY_USER_ID, "");
    }

    public static void setHyUserId(String hyUserId) {
        getPreference().edit().putString(HY_USER_ID, hyUserId).commit();
    }

    public static String getChannelUserId() {
        return getPreference().getString(CHANNEL_USER_ID, "");
    }

    public static void setChannelUserId(String channelUserId) {
        getPreference().edit().putString(CHANNEL_USER_ID, channelUserId).commit();
    }

    public static String getChannelUserName() {
        return getPreference().getString(CHANNEL_USER_NAME, "");
    }

    public static void setChannelUserName(String channelUserName) {
        getPreference().edit().putString(CHANNEL_USER_NAME, channelUserName).commit();
    }

    public static int getIsBindAccount() {
        return getPreference().getInt(IS_BIND_ACCOUNT, 0);
    }

    public static void setIsBindAccount(int isBindAccount) {
        getPreference().edit().putInt(IS_BIND_ACCOUNT, isBindAccount).commit();
    }

    public static String getUserPassword() {
        return getPreference().getString(USER_PASSWORD, "");
    }

    public static void setUserPassword(String password) {
        getPreference().edit().putString(USER_PASSWORD, password).commit();
    }

    public static int getLoginType() {
        return getPreference().getInt(LOGIN_TYPE, 0);
    }

    public static void setLoginType(int loginType) {
        getPreference().edit().putInt(LOGIN_TYPE, loginType).commit();
    }

    public static int getIsSwitchPayChannel() {
        return getPreference().getInt(IS_SWITCH_PAY_CHANNEL, 0);
    }

    public static void setIsSwitchPayChannel(int isSwitchPayChannel) {
        getPreference().edit().putInt(IS_SWITCH_PAY_CHANNEL, isSwitchPayChannel).commit();
    }
}

