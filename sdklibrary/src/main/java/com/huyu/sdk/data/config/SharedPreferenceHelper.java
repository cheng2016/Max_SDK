package com.huyu.sdk.data.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.huyu.sdk.U9Platform;


public class SharedPreferenceHelper {
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


    public static final String NICK_NAME = "nick_name";

    public static final String IS_FIRST = "isFirst";

    public static final String KEY_IMEI = "imei";

    public static final String KEY_MAC = "macaddr";


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
        return getPreference().getInt(IS_BIND_ACCOUNT,0);
    }

    public static void setIsBindAccount(int isBindAccount) {
        getPreference().edit().putInt(IS_BIND_ACCOUNT, isBindAccount).commit();
    }

    public static boolean getFirstState() {
        return getPreference().getBoolean("isFirst", true);
    }

    public static String getImei() {
        return getPreference().getString(KEY_IMEI, "");
    }

    public static String getNikeName() {
        return getPreference().getString(NICK_NAME, "name");
    }

    public static String getmac() {
        return getPreference().getString(KEY_MAC, "");
    }

    public static void saveFirstState(boolean paramBoolean) {
        getPreference().edit().putBoolean("isFirst", paramBoolean).commit();
    }

    public static void saveNikeName(String paramString) {
        getPreference().edit().putString(NICK_NAME, paramString).commit();
    }

    public static void setImei(String paramString) {
        getPreference().edit().putString(KEY_IMEI, paramString).commit();
    }

    public static void setMac(String paramString) {
        getPreference().edit().putString(KEY_MAC, paramString).commit();
    }

}

