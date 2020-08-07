package com.huyu.sdk.impl;

import android.content.Context;

import com.huyu.sdk.listener.CallbackListener;

import java.util.Map;

public interface IUser    {
    public static final int PLUGIN_TYPE = 1;

//    void login(Context context,Map<String, String> map, CallbackListener listener);

    void guestLogin(final Context context, Map map, final CallbackListener listener);

    void autoLogin(final Context context, Map<String, String> map, final CallbackListener listener);

    void accountlogin(final Context context, Map<String, String> map, final CallbackListener listener);

    void verifylogin(Context context, Map<String, String> map, final CallbackListener listener);

    void switchLogin(Map<String, String> map,CallbackListener listener);

    void submitExtraData(UserExtraData paramUserExtraData, CallbackListener listener);

    void logout(Context context, Map<String, String> map, CallbackListener listener);


    /*

    void loginCustom(String paramString);

    boolean showAccountCenter();

    void postGiftCode(String paramString);

    void realNameRegister();

    void queryAntiAddiction();

    void queryProducts();
    */
}


