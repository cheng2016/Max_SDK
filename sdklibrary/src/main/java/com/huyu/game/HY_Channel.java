package com.huyu.game;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.widget.Toast;

import com.huyu.googlepay.util.AppsFlyerActionHelper;
import com.huyu.sdk.HYPlatform;
import com.huyu.sdk.base.BaseChannel;

import com.huyu.sdk.data.ResultCode;
import com.huyu.sdk.data.bean.GameRoleInfo;
import com.huyu.sdk.data.bean.PayParams;
import com.huyu.sdk.data.bean.HYUser;
import com.huyu.sdk.data.config.SharedPreferenceHelper;
import com.huyu.sdk.listener.CallbackListener;
import com.huyu.sdk.listener.LoginCallBackListener;
import com.huyu.sdk.listener.PayCallbackListener;
import com.huyu.sdk.util.Logger;

/**
 * 文件名：DefaultChannel
 * 创建日期：2020/8/4 11:13
 * 描述：TODO
 */
public class HY_Channel extends BaseChannel {
    public static final String TAG = HY_Channel.class.getSimpleName();

    static HY_Channel instance;

    public static HY_Channel getInstance() {
        if (instance == null) {
            instance = new HY_Channel();
        }
        return instance;
    }

    @Override
    public void initApplication(Application atc) {
        //初始化AppsFlyer 数据来源统计
        AppsFlyerActionHelper.initAppsFlyerSdk(atc);
    }

    @Override
    public void initActivity(Activity activity, CallbackListener listener) {
        Logger.i(TAG, "  init success");
        listener.onResult(ResultCode.SUCCESS, "thirdSdk init success", null);
        HYPlatform.getInstance().init(activity, new HYPlatform.SDKListener() {
            @Override
            public void onSwitchAccount(HYUser user) {
                Logger.i(TAG,"onSwitchAccount");
                mHYSDKListener.onSwitchAccount(user);
//                //cp更新数据，并重新跳转到登录界面进行登录操作
            }
        });
    }

    @Override
    public void login(final Activity activity, final LoginCallBackListener listener) {
        Logger.i(TAG, "  login  ");
        HYPlatform.getInstance().login(activity, new CallbackListener() {
            @Override
            public void onResult(ResultCode resultCode, String msg, String data) {
                if (resultCode == ResultCode.SUCCESS) {
                    final HYUser user = new HYUser();
                    user.token = SharedPreferenceHelper.getAccessToken();
                    user.hyuid = SharedPreferenceHelper.getHyUserId();
                    user.userId = SharedPreferenceHelper.getUserId();
                    user.channelUserId = SharedPreferenceHelper.getChannelUserId();
                    user.channelUserName = SharedPreferenceHelper.getChannelUserName();
                    user.info = "default_info";
                    listener.onLoginSuccess(user);
                } else if (resultCode == ResultCode.Fail) {
                    listener.onLoginFailed(msg);
                }else if(resultCode ==ResultCode.CANCEL){
                    listener.onLoginCancel();
                }
            }
        });
    }


    public void logout(Activity activity) {
        Logger.i(TAG, "  logout  ");
        HYPlatform.getInstance().logout(activity, new CallbackListener() {
            @Override
            public void onResult(ResultCode resultCode, String msg, String data) {
                if (resultCode == ResultCode.SUCCESS) {
                    mHYSDKListener.onLogout();
                }
            }
        });
    }

    @Override
    public void switchAccount(Activity activity) {

    }

    @Override
    public void showAccountCenter(Activity activity) {
        Logger.i(TAG, "  showAccountCenter  ");
        HYPlatform.getInstance().showAccountCenter(activity);
    }

    @Override
    public void roleReport(Activity activity, GameRoleInfo gameRoleInfo, final CallbackListener listener) {
        Logger.i(TAG, "  roleReport  ");
    }

    @Override
    public void pay(final Activity activity, final PayParams payParams, final PayCallbackListener listener) {
        Logger.i(TAG, " pay  ");
        Toast.makeText(activity, "U9支付订单创建成功，启动支付界面", Toast.LENGTH_SHORT).show();
        HYPlatform.getInstance().pay(activity, payParams, new CallbackListener() {
            @Override
            public void onResult(ResultCode resultCode, String msg, String data) {
                Logger.i(TAG, "支付接口 ： " + resultCode + " ,msg : " + msg);
                switch (resultCode) {
                    case SUCCESS:
                        listener.onPaySuccess(payParams);
                        break;
                    case Fail:
                        listener.onPayFailed(msg);
                        break;
                    case CANCEL:
                        listener.onPayCancel();
                        break;
                }
            }
        });

    }

    @Override
    public void applicationDestroy(Activity activity) {

    }

    @Override
    public void exit(final Activity activity) {
        Logger.i(TAG, " exit  ");
        new AlertDialog.Builder(activity)
                .setTitle("是否退出游戏")
                .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        activity.finish();
                    }
                }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create().show();
    }

    @Override
    public void onCreate(Activity context) {
        Logger.i(TAG, " onCreate");
    }

    @Override
    public void onNewIntent(Intent intent) {
        Logger.i(TAG, " onNewIntent");
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.i(TAG, " onActivityResult requestCode = " + requestCode + " , resultCode = " + resultCode);
    }

    @Override
    public void onStart(Activity context) {
        Logger.i(TAG, " onStart");
    }

    @Override
    public void onResume(Activity context) {
        Logger.i(TAG, " onResume");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Logger.i(TAG, " onConfigurationChanged");
    }

    @Override
    public void onPause(Activity context) {
        Logger.i(TAG, " onPause");
    }

    @Override
    public void onStop(Activity context) {
        Logger.i(TAG, " onStop");
    }

    @Override
    public void onDestroy(Activity context) {
        Logger.i(TAG, " onDestroy");
    }
}
