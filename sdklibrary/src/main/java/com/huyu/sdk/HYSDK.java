package com.huyu.sdk;


import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;

import com.huyu.sdk.base.BaseChannel;
import com.huyu.sdk.base.SDKProxy;
import com.huyu.sdk.data.Constant;
import com.huyu.sdk.data.ResultCode;
import com.huyu.sdk.data.bean.GameRoleInfo;
import com.huyu.sdk.data.bean.HYUser;
import com.huyu.sdk.data.bean.PayParams;
import com.huyu.sdk.data.config.SharedPreferenceHelper;
import com.huyu.sdk.listener.CallbackListener;
import com.huyu.sdk.listener.HYSDKListener;
import com.huyu.sdk.listener.LoginCallBackListener;
import com.huyu.sdk.listener.PayCallbackListener;
import com.huyu.sdk.util.Logger;
import com.huyu.sdk.util.SDKTools;


/**
 * 文件名：HHSDK
 * 创建日期：2020/8/4 11:13
 * 描述：平台所有入口
 */
public class HYSDK implements SDKProxy, HYSDKListener {
    public static final String TAG = HYSDK.class.getSimpleName();
    private static HYSDK instance;
    private BaseChannel mChannel;
    private HYUser mHYUser;

    public static HYSDK getInstance() {
        if (instance == null) {
            instance = new HYSDK();
        }
        return instance;
    }

    @Override
    public void initApplication(Application context) {
        if (mChannel == null) {
            Object object = null;
            try {
                object = SDKTools.getMainClassByChannelName(context);
            } catch (Exception e) {
                Log.e("Logger HYSDK", e.toString());
                e.printStackTrace();
            }
//            mChannel = ((null == object) ? new Test_Channel() : (BaseChannel) object);
            mChannel = (BaseChannel) object;
        }
        U9Platform.getInstance().initApplication(context);
        mChannel.initApplication(context);
    }

    @Override
    public void initActivity(Activity activity, CallbackListener listener) {
        U9Platform.getInstance().initActivity(activity, listener);
        mChannel.initActivity(activity, listener);
    }

    @Override
    public void setHYSDKListener(final HYSDKListener listener) {
        mChannel.setHYSDKListener(new HYSDKListener() {
            @Override
            public void onSwitchAccount(HYUser user) {
                instance.onSwitchAccount(user);
                listener.onSwitchAccount(user);
            }

            @Override
            public void onLogout() {
                instance.onLogout();
                listener.onLogout();
            }
        });
    }

    @Override
    public void login(final Activity activity, final LoginCallBackListener listener) {
        mChannel.login(activity, new LoginCallBackListener() {
            @Override
            public void onLoginSuccess(final HYUser user) {
                U9Platform.getInstance().verifyChannelLogin(activity, user, new CallbackListener() {
                    @Override
                    public void onResult(ResultCode resultCode, String msg, String data) {
                        if (resultCode == ResultCode.SUCCESS) {
                            user.userId = SharedPreferenceHelper.getUserId();
                            mHYUser = user;
                            listener.onLoginSuccess(user);
                        } else if (resultCode == ResultCode.Fail) {
                            listener.onLoginFailed(msg);
                        } else if (resultCode == ResultCode.CANCEL) {
                            listener.onLoginCancel();
                        }
                    }
                });
            }

            @Override
            public void onLoginCancel() {
                listener.onLoginCancel();
            }

            @Override
            public void onLoginFailed(String message) {
                listener.onLoginFailed(message);
            }
        });
    }

    @Override
    public void logout(final Activity activity) {
        mChannel.logout(activity);
    }

    @Override
    public void switchAccount(Activity activity) {
        mChannel.switchAccount(activity);
    }

    @Override
    public void showAccountCenter(Activity activity) {
        if (this.mHYUser == null) {
            Logger.e(TAG,"showAccountCenter 请先登录");
            return;
        }
        mChannel.showAccountCenter(activity);
    }

    @Override
    public void roleReport(Activity activity, GameRoleInfo gameRoleInfo, final CallbackListener listener) {
        mChannel.roleReport(activity, gameRoleInfo, listener);
        U9Platform.getInstance().roleReport(activity, gameRoleInfo, new CallbackListener() {
            @Override
            public void onResult(ResultCode resultCode, String msg, String data) {
                listener.onResult(resultCode, msg, data);
            }
        });
    }

    @Override
    public void pay(final Activity activity, final PayParams params, final PayCallbackListener listener) {
        if (this.mHYUser == null) {
            listener.onPayFailed("请先登录");
            return;
        }
        if(Constant.CHANNEL_TYPE.contains("HY")){
            mChannel.pay(activity, params, listener);
            return;
        }
        U9Platform.getInstance().startPay(activity, params, mHYUser, new CallbackListener() {
            @Override
            public void onResult(ResultCode resultCode, String msg, String data) {
                if (resultCode == ResultCode.SUCCESS) {
                    mChannel.pay(activity, params, listener);
                } else {
                    listener.onPayFailed(msg);
                }
            }
        });
    }

    @Override
    public void applicationDestroy(Activity activity) {
        mChannel.applicationDestroy(activity);
    }

    @Override
    public void exit(Activity activity) {
        mChannel.exit(activity);
    }

    @Override
    public void onCreate(Activity context) {
        mChannel.onCreate(context);
    }

    @Override
    public void onNewIntent(Intent intent) {
        mChannel.onNewIntent(intent);
    }

    @Override
    public void onStart(Activity context) {
        mChannel.onStart(context);
    }

    @Override
    public void onResume(Activity context) {
        mChannel.onResume(context);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        mChannel.onConfigurationChanged(newConfig);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mChannel.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause(Activity context) {
        mChannel.onPause(context);
    }

    @Override
    public void onStop(Activity context) {
        mChannel.onStop(context);
    }

    @Override
    public void onDestroy(Activity context) {
        U9Platform.getInstance().onDestroy();
        mChannel.onDestroy(context);
    }

    @Override
    public void onSwitchAccount(HYUser user) {
        Logger.d(TAG, "onSwitchAccount");
        this.mHYUser = user;
    }

    @Override
    public void onLogout() {
        Logger.d(TAG, "onLogout");
        this.mHYUser = null;
    }
}
