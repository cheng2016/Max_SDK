package com.huyu.sdk.base;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;

import com.huyu.sdk.data.bean.GameRoleInfo;
import com.huyu.sdk.data.bean.PayParams;
import com.huyu.sdk.listener.CallbackListener;
import com.huyu.sdk.listener.LoginCallBackListener;
import com.huyu.sdk.listener.PayCallbackListener;
import com.huyu.sdk.listener.HYSDKListener;

/**
 * 文件名：IChannelProxy
 * 创建日期：2020/8/4 11:13
 * 描述：TODO
 */
public abstract class IChannelProxy implements BasePresenter {

    private SDKProxy mSDKProxy;

    @Override
    public void setPresenter(SDKProxy sdkProxy) {
        this.mSDKProxy = sdkProxy;
    }

    public HYSDKListener getU9SDKListener() {
        return mSDKProxy.getU9SDKListener();
    }

    public SDKProxy getSdkProxy(){
        return mSDKProxy;
    }

    public abstract void initConfig(Application application);

    public abstract void init(Activity activity, CallbackListener listener);

    public abstract void login(Activity activity, LoginCallBackListener listener);

    public abstract void logout(Activity activity);

    public abstract void switchAccount(Activity activity);

    public abstract void showAccountCenter(Activity activity);

    public abstract void roleReport(Activity activity, GameRoleInfo gameRoleInfo, CallbackListener listener);

    public abstract void pay(Activity activity, PayParams payParams, PayCallbackListener listener);

    public abstract void exit(Activity activity);

    public abstract void onCreate(Activity activity);


    public abstract void onNewIntent(Intent intent);

    public abstract  void onActivityResult(int requestCode, int resultCode, Intent data);


    public abstract void onStart(Activity activity);

    public abstract void onResume(Activity activity);

    public abstract  void onConfigurationChanged(Configuration newConfig);

    public abstract void onPause(Activity activity);

    public abstract void onStop(Activity activity);

    public abstract void onDestroy(Activity activity);
}
