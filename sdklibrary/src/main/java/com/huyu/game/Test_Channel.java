package com.huyu.game;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.widget.Toast;

import com.huyu.sdk.HYPlatform;
import com.huyu.sdk.base.BaseChannel;
import com.huyu.sdk.data.ResultCode;
import com.huyu.sdk.data.bean.GameRoleInfo;
import com.huyu.sdk.data.bean.HYUser;
import com.huyu.sdk.data.bean.PayParams;
import com.huyu.sdk.data.config.SharedPreferenceHelper;
import com.huyu.sdk.listener.CallbackListener;
import com.huyu.sdk.listener.LoginCallBackListener;
import com.huyu.sdk.listener.PayCallbackListener;
import com.huyu.sdk.util.Logger;

/**
 * 文件名：TestChannel
 * 创建日期：2020/8/6 18:46
 * 描述：测试渠道
 */
public class Test_Channel extends BaseChannel {
    public static final String TAG = Test_Channel.class.getSimpleName();

    @Override
    public void initApplication(Application activity) {
        Logger.d(TAG, "initConfig");
    }

    @Override
    public void initActivity(Activity activity, CallbackListener listener) {
        Logger.d(TAG, "init");
    }

    @Override
    public void login(final Activity activity, final LoginCallBackListener listener) {
        Logger.d(TAG, "login");
        new AlertDialog.Builder(activity)
                .setTitle("模拟SDK登录框")
                .setMessage("进入游戏请选择登录按钮")
                .setNegativeButton("登录", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(activity, "第三方sdk登录成功，进行U9服务器校验", Toast.LENGTH_SHORT).show();
                        HYPlatform.getInstance().guestLogin(activity, new CallbackListener() {
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
                                }
                            }
                        });

                    }
                }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onLoginCancel();
            }
        }).create().show();
    }

    @Override
    public void logout(Activity activity) {
        Logger.d(TAG, "logout");
        mHYSDKListener.onLogout();
    }

    @Override
    public void switchAccount(Activity activity) {
        Logger.d(TAG, "switchAccount");
    }

    @Override
    public void showAccountCenter(Activity activity) {
        Logger.d(TAG, "showAccountCenter");
    }

    @Override
    public void roleReport(Activity activity, GameRoleInfo gameRoleInfo, CallbackListener listener) {
        Logger.d(TAG, "roleReport");
    }

    @Override
    public void pay(final Activity activity, final PayParams payParams, final PayCallbackListener listener) {
        Logger.d(TAG, "pay orderid = " + payParams.getOrderId());
        new AlertDialog.Builder(activity)
                .setTitle("模拟SDK支付框")
                .setMessage("充值请点击支付按钮")
                .setNegativeButton("支付", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onPaySuccess(payParams);
                    }
                }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onPayCancel();
            }
        }).create().show();
    }

    @Override
    public void applicationDestroy(Activity activity) {

    }

    @Override
    public void exit(final Activity activity) {
        Logger.d(TAG, "exit");

        new AlertDialog.Builder(activity)
                .setTitle("模拟SDK推出框")
                .setMessage("是否退出游戏")
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
    public void onCreate(Activity activity) {
        Logger.d(TAG, "onCreate");
    }

    @Override
    public void onNewIntent(Intent intent) {
        Logger.d(TAG, "onNewIntent");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.d(TAG, " onActivityResult requestCode = " + requestCode + " , resultCode = " + resultCode);
    }

    @Override
    public void onStart(Activity activity) {
        Logger.d(TAG, "onStart");
    }

    @Override
    public void onResume(Activity activity) {
        Logger.d(TAG, "onResume");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Logger.d(TAG, " onConfigurationChanged");
    }

    @Override
    public void onPause(Activity activity) {
        Logger.d(TAG, "onPause");
    }

    @Override
    public void onStop(Activity activity) {
        Logger.d(TAG, "onStop");
    }

    @Override
    public void onDestroy(Activity activity) {
        Logger.d(TAG, "onDestroy");
    }

}
