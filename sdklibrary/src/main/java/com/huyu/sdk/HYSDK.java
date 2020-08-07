package com.huyu.sdk;


import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;

import com.huyu.game.Test_Channel;
import com.huyu.sdk.base.BaseChannel;

import com.huyu.sdk.base.SDKProxy;
import com.huyu.sdk.data.ResultCode;
import com.huyu.sdk.data.bean.GameRoleInfo;
import com.huyu.sdk.data.bean.PayParams;
import com.huyu.sdk.data.bean.HYUser;
import com.huyu.sdk.data.config.SharedPreferenceHelper;
import com.huyu.sdk.listener.CallbackListener;
import com.huyu.sdk.listener.LoginCallBackListener;
import com.huyu.sdk.listener.PayCallbackListener;
import com.huyu.sdk.listener.HYSDKListener;
import com.huyu.sdk.util.Logger;
import com.huyu.sdk.util.SDKTools;

import java.lang.reflect.InvocationTargetException;

/**
 * 文件名：HHSDK 平台所有入口
 * 创建日期：2020/8/4 11:13
 * 描述：TODO
 */
public class HYSDK implements SDKProxy, HYSDKListener {
    public static final String TAG = HYSDK.class.getSimpleName();
    private static HYSDK instance;
    private BaseChannel IChannel;
    private HYUser mHYUser;

    public static HYSDK getInstance() {
        if (instance == null) {
            instance = new HYSDK();
        }
        return instance;
    }

    public void setHYSDKListener(final HYSDKListener listener) {
        IChannel.setHYSDKListener(new HYSDKListener() {
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
    public void initApplication(Application context) {
        if (IChannel == null) {
            Object object = null;
            try {
                object = SDKTools.getMainClassByChannelName(context);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            IChannel = ((null == object) ? new Test_Channel() : (BaseChannel) object);
        }
        U9Platform.getInstance().initApplication(context);
        IChannel.initApplication(context);
    }

    @Override
    public void initActivity(Activity activity, CallbackListener listener) {
        U9Platform.getInstance().initActivity(activity, listener);
        IChannel.initActivity(activity, listener);
    }

    @Override
    public void login(final Activity activity, final LoginCallBackListener listener) {
        IChannel.login(activity, new LoginCallBackListener() {
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
                        }else if(resultCode == ResultCode.CANCEL){
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
        IChannel.logout(activity);
    }

    @Override
    public void switchAccount(Activity activity) {
        IChannel.switchAccount(activity);
    }

    @Override
    public void showAccountCenter(Activity activity) {
        IChannel.showAccountCenter(activity);
    }

    @Override
    public void roleReport(Activity activity, GameRoleInfo gameRoleInfo, final CallbackListener listener) {
        IChannel.roleReport(activity, gameRoleInfo, listener);
        U9Platform.getInstance().roleReport(activity, gameRoleInfo, new CallbackListener() {
            @Override
            public void onResult(ResultCode resultCode, String msg, String data) {
                listener.onResult(resultCode, msg, data);
            }
        });
    }

    @Override
    public void pay(final Activity activity, final PayParams payParams, final PayCallbackListener listener) {
        U9Platform.getInstance().startVerifyPay(activity, payParams, mHYUser, new CallbackListener() {
            @Override
            public void onResult(ResultCode resultCode, String msg, String data) {
                if (resultCode == ResultCode.SUCCESS) {
                    IChannel.pay(activity, payParams, listener);
                } else {
                    listener.onPayFailed(msg);
                }
            }
        });
    }

    @Override
    public void applicationDestroy(Activity activity) {
        IChannel.applicationDestroy(activity);
    }

    @Override
    public void exit(Activity activity) {
        IChannel.exit(activity);
    }


    @Override
    public void onCreate(Activity context) {
        IChannel.onCreate(context);
    }

    @Override
    public void onNewIntent(Intent intent) {
        IChannel.onNewIntent(intent);
    }

    @Override
    public void onStart(Activity context) {
        IChannel.onStart(context);
    }

    @Override
    public void onResume(Activity context) {
        IChannel.onResume(context);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        IChannel.onConfigurationChanged(newConfig);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IChannel.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause(Activity context) {
        IChannel.onPause(context);
    }

    @Override
    public void onStop(Activity context) {
        IChannel.onStop(context);
    }

    @Override
    public void onDestroy(Activity context) {
        IChannel.onDestroy(context);
    }

    @Override
    public void onSwitchAccount(HYUser user) {
        Logger.i(TAG, "onSwitchAccount");
        this.mHYUser = user;
    }

    @Override
    public void onLogout() {
        Logger.i(TAG, "onLogout");
    }
}
