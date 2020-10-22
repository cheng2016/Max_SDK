package com.huyu.sdk.base;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;

import com.huyu.sdk.data.bean.GameRoleInfo;
import com.huyu.sdk.data.bean.PayParams;
import com.huyu.sdk.listener.CallbackListener;
import com.huyu.sdk.listener.HYSDKListener;
import com.huyu.sdk.listener.LoginCallBackListener;
import com.huyu.sdk.listener.PayCallbackListener;

/**
 * 文件名：SDKProxy
 * 创建日期：2020/8/4 15:30
 * 描述：TODO
 */
public interface SDKProxy {

    void initApplication(Application activity);

    void initActivity(Activity activity, CallbackListener listener);

    void setHYSDKListener(HYSDKListener listener);

    void login(Activity activity, LoginCallBackListener listener);

    void logout(Activity activity);

    void switchAccount(Activity activity);

    void showAccountCenter(Activity activity);

    void roleReport(Activity activity, GameRoleInfo gameRoleInfo, CallbackListener listener);

    void pay(Activity activity, PayParams payParams, PayCallbackListener listener);


    void applicationDestroy(Activity activity);

    void exit(Activity activity);

    void onCreate(Activity context);


    void onNewIntent(Intent intent);

    void onActivityResult(int requestCode, int resultCode, Intent data);


    void onStart(Activity context);

    void onResume(Activity context);


    void onConfigurationChanged(Configuration newConfig);


    void onPause(Activity context);

    void onStop(Activity context);

    void onDestroy(Activity context);

}
