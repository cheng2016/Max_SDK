package com.huyu.sdk;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;

import com.huyu.sdk.data.Constant;
import com.huyu.sdk.data.ResultCode;
import com.huyu.sdk.data.bean.HYUser;
import com.huyu.sdk.data.bean.PayParams;
import com.huyu.sdk.data.config.SharedPreferenceHelper;
import com.huyu.sdk.impl.Pay;
import com.huyu.sdk.impl.Sdk;
import com.huyu.sdk.impl.User;
import com.huyu.sdk.listener.CallbackListener;
import com.huyu.sdk.listener.LoginCallBackListener;
import com.huyu.sdk.util.AppUtils;
import com.huyu.sdk.util.HY_Log_TimeUtils;
import com.huyu.sdk.util.Logger;
import com.huyu.sdk.util.MD5Utils;
import com.huyu.sdk.util.Utils;
import com.huyu.sdk.view.AccountCenterDialog;
import com.huyu.sdk.view.AccountLoginDialog;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件名：HYPlatform
 * 创建日期：2020/8/5 11:22
 * 描述：TODO
 */
public class HYPlatform {
    public final static String TAG = U9Platform.class.getSimpleName();
    private static HYPlatform instance;
    private SDKListener mSDKListener;

    public static HYPlatform getInstance() {
        if (instance == null)
            instance = new HYPlatform();
        return instance;
    }

    public void init(Activity activity, SDKListener sdkListener) {
        this.mSDKListener = sdkListener;
    }

    /**
     * 登录入口
     *
     * @param ctx
     * @param listener
     */
    public void login(Context ctx, CallbackListener listener) {
        Logger.i(TAG, "login");
        HY_Log_TimeUtils.setStartLogin();
        logReport("3", "开始登录SDK", HY_Log_TimeUtils.startLogin - HY_Log_TimeUtils.initSdkSuccess + "");
        //无感自动登录
        if (TextUtils.isEmpty(SharedPreferenceHelper.getAccessToken())) {
            if (TextUtils.isEmpty(SharedPreferenceHelper.getChannelUserName())) {
                guestLogin(ctx, listener);
            } else {
                Dialog dialog = new AccountLoginDialog(ctx, listener, AccountLoginDialog.ACCOUNT_LOGIN);
                dialog.show();
            }
        } else {
            autoLogin(ctx, listener);
        }
    }

    //游客登录
    public void guestLogin(Context context, CallbackListener listener) {
        User.getInstance().guestLogin(context, Utils.commonRequestData(new HashMap<String, String>()), listener);
    }

    //自动登录
    public void autoLogin(Context context, CallbackListener listener) {
        Map<String, String> map = Utils.commonRequestData(new HashMap<String, String>());
        map.put("token", SharedPreferenceHelper.getAccessToken());
        map.put("guid", SharedPreferenceHelper.getChannelUserId());
        User.getInstance().autoLogin(context, map, listener);
    }

    //账号密码登录
    public void accountlogin(Context ctx, final int loginType, String username, String password, final CallbackListener listener) {
        HY_Log_TimeUtils.setStartLogin();
        logReport("3", "开始登录SDK", HY_Log_TimeUtils.startLogin - HY_Log_TimeUtils.initSdkSuccess + "");
        Map<String, String> map = Utils.commonRequestData(new HashMap<String, String>());
        map.put("username", username);
        map.put("password", MD5Utils.MD5(password));
        User.getInstance().accountlogin(ctx, map, new LoginCallBackListener() {
            @Override
            public void onLoginSuccess(HYUser user) {
                listener.onResult(ResultCode.SUCCESS, "", "");
                if (loginType == AccountLoginDialog.ACCOUNT_SWITCH) {
                    if (mSDKListener != null)
                        mSDKListener.onSwitchAccount(user);
                }
            }

            @Override
            public void onLoginCancel() {
                listener.onResult(ResultCode.CANCEL, "cancel", "");
            }

            @Override
            public void onLoginFailed(String message) {
                listener.onResult(ResultCode.Fail, message, "");
            }
        });
    }

    public void logout(Context context, final CallbackListener listener) {
        Logger.i(TAG, "logout");
        Map<String, String> map = Utils.commonRequestData(new HashMap<String, String>());
        map.put("usecret", SharedPreferenceHelper.getAccessToken());
        map.put("uid", SharedPreferenceHelper.getUserId());
        User.getInstance().logout(context, map, listener);
    }

    /**
     * 打开个人中心（设置中心）
     *
     * @param context
     */
    public void showAccountCenter(Context context) {
        Logger.i(TAG, "showAccountCenter");
        Dialog dialog = new AccountCenterDialog(context);
        dialog.show();
    }

    //检查支付参数配置,是否切换支付方式
    public void checkPaymentMethod(Context context, CallbackListener listener) {
        Logger.i(TAG, "createPaymentOrder");
        Map<String, String> params = Utils.commonRequestData(new HashMap<String, String>());
        params.put("total_fee", 99 + "");
        params.put("u9uid", SharedPreferenceHelper.getUserId());
        params.put("role_id", "1235698465");
        params.put("guid", SharedPreferenceHelper.getChannelUserId());
        params.put("token", SharedPreferenceHelper.getAccessToken());
        params.put("bundle_id",AppUtils.APP_PACKAGE_NAME);
        Pay.getInstance().checkPaymentMethod(context, params, listener);
    }

    public void startPay(final Context context, final PayParams payParams, final CallbackListener listener) {
        Pay.getInstance().startPay(context, Utils.getPayParamsMap(payParams), listener);
    }

    public void bindAccount(Context context, String username, String password, CallbackListener listener) {
        Logger.i(TAG, "bindAccount");
        Map<String, String> map = new HashMap<>();
        map.put("guid", SharedPreferenceHelper.getChannelUserId());
        map.put("token", SharedPreferenceHelper.getAccessToken());
        map.put("username", username);
        map.put("password", MD5Utils.MD5(password));
        map.put("app_id", Constant.APPID);
        User.getInstance().bindAccount(context, map, listener);
    }

/*    private void startChoosePayActivity(Context context, PayParams params, CallbackListener payListener) {
        Logger.i(TAG, "startGooglePayActivity");
        params.setU9uid(SharedPreferenceHelper.getChannelUserId());
        U9Platform.payCallback = payListener;
        U9Platform.mPayParams = params;
        Intent intent = new Intent();
        intent.setClass(context, HY_GameCenterActivity.class);
        context.startActivity(intent);
    }*/

    //检查支付结果
    public void checkPayResult(Context context, String order_id, CallbackListener listener) {
        Logger.i(TAG, "checkPayResult");
        Map<String, String> params = new HashMap<>();
        params.put("order_id", order_id);
        Pay.getInstance().checkPayResult(context, params, listener);
    }

    //支付成功后到服务器校验
    public void orderServerVerify(Context context, String url, Map<String, String> params, CallbackListener listener) {
        Pay.getInstance().orderServerVerify(context, url, params, listener);
    }

    public void payNotifyServer(Context context, CallbackListener listener) {

    }

    public void logReport(String step, String step_desc, String time) {
        Logger.i(TAG, "logReport");
        Map<String, String> params = Utils.commonRequestData(new HashMap<String, String>());
        if (!TextUtils.isEmpty(step)) {
            params.put("step", step);
        }
        if (!TextUtils.isEmpty(time)) {
            params.put("time", time);
        }
        if (!TextUtils.isEmpty(step_desc)) {
            params.put("step_desc", step_desc);
        }
        params.put("v", "1");
        Sdk.getInstance().logReport(params);
    }

    public interface SDKListener {
        void onSwitchAccount(HYUser user);
    }
}
