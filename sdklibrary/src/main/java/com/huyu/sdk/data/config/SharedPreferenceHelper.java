package com.huyu.sdk.data.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.huyu.sdk.U9Platform;
import com.huyu.sdk.util.encry.RC4Http;
import com.huyu.sdk.util.encry.TypeConvert;


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


    public static final String CURRENTDAY = "current_day";

    public static final String NICK_NAME = "nick_name";

    public static final String IS_FIRST = "isFirst";

    public static final String KEY_IMEI = "imei";

    public static final String KEY_MAC = "macaddr";

    public static final String PLAYERID = "playerId";

    public static final String USER_TYPE = "type";

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

    public static String getAccount() {
        String account = getPreference().getString("account", null);
        if (null != account) {
            try {
                byte[] b = TypeConvert.hexStr2ByteArr(account);
                RC4Http.RC4Base(b, 0, b.length);
                account = new String(b);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return account;
    }

    public static String getCurrentDay() {
        return getPreference().getString(CURRENTDAY, "time");
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

    public static String getPlayerId() {
        return getPreference().getString(PLAYERID, "");
    }

    public static SharedPreferences getPreference() {
        return U9Platform.getInstance().getContext().getSharedPreferences("u9", Context.MODE_PRIVATE);
    }

    public static String getPsd() {
        String str2 = getPreference().getString("psd", null);
        String str1 = str2;
        if (str2 != null) {
            byte[] arrayOfByte = TypeConvert.hexStr2ByteArr(str2);
            RC4Http.RC4Base(arrayOfByte, 0, arrayOfByte.length);
            str1 = new String(arrayOfByte);
        }
        return str1;
    }

    public static int getUserType() {
        return getPreference().getInt("type", 0);
    }

    public static String getmac() {
        return getPreference().getString(KEY_MAC, "");
    }

    public static void saveAccount(String paramString) {
        byte[] arrayOfByte = paramString.getBytes();
        RC4Http.RC4Base(arrayOfByte, 0, arrayOfByte.length);
        String str = TypeConvert.toHexString(arrayOfByte);
        getPreference().edit().putString("account", str).commit();
    }

    public static void saveFirstState(boolean paramBoolean) {
        getPreference().edit().putBoolean("isFirst", paramBoolean).commit();
    }

    public static void saveNikeName(String paramString) {
        getPreference().edit().putString(NICK_NAME, paramString).commit();
    }

    public static void savePsd(String paramString) {
        byte[] arrayOfByte = paramString.getBytes();
        RC4Http.RC4Base(arrayOfByte, 0, paramString.length());
        paramString = TypeConvert.toHexString(arrayOfByte);
        getPreference().edit().putString("psd", paramString).commit();
    }


    public static void setCurrentDay(String paramString) {
        getPreference().edit().putString(CURRENTDAY, paramString).commit();
    }

    public static void setImei(String paramString) {
        getPreference().edit().putString(KEY_IMEI, paramString).commit();
    }

    public static void setMac(String paramString) {
        getPreference().edit().putString(KEY_MAC, paramString).commit();
    }

    public static void setPlayerId(String paramString) {
        getPreference().edit().putString(PLAYERID, paramString).commit();
    }

    public static void setUserType(int paramInt) {
        getPreference().edit().putInt("type", 0).commit();
    }
}

