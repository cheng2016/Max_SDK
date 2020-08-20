package com.huyu.sdk;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.huyu.googlepay.HY_GameCenterActivity;
import com.huyu.sdk.data.Constant;
import com.huyu.sdk.data.ResultCode;
import com.huyu.sdk.data.bean.GameRoleInfo;
import com.huyu.sdk.data.bean.HYUser;
import com.huyu.sdk.data.bean.PayParams;
import com.huyu.sdk.data.config.PhoneInfoHelper;
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
import com.huyu.sdk.util.OkHttpUtils;
import com.huyu.sdk.view.AccountCenterDialog;
import com.huyu.sdk.view.AccountLoginDialog;

import org.json.JSONException;
import org.json.JSONObject;

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
        User.getInstance().guestLogin(context, commonRequestData(new HashMap<String, String>()), listener);
    }

    //自动登录
    public void autoLogin(Context context, CallbackListener listener) {
        Map<String, String> map = commonRequestData(new HashMap<String, String>());
        map.put("token", SharedPreferenceHelper.getAccessToken());
        map.put("guid", SharedPreferenceHelper.getChannelUserId());
        User.getInstance().autoLogin(context, map, listener);
    }

    //账号密码登录
    public void accountlogin(Context ctx, String username, String password, final CallbackListener listener) {
        HY_Log_TimeUtils.setStartLogin();
        logReport("3", "开始登录SDK", HY_Log_TimeUtils.startLogin - HY_Log_TimeUtils.initSdkSuccess + "");
        Map<String, String> map = commonRequestData(new HashMap<String, String>());
        map.put("username", username);
        map.put("password", MD5Utils.MD5(password));
        User.getInstance().accountlogin(ctx, map, new LoginCallBackListener() {
            @Override
            public void onLoginSuccess(HYUser user) {
                listener.onResult(ResultCode.SUCCESS, "", "");
                if (mSDKListener != null)
                    mSDKListener.onSwitchAccount(user);
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

    public void accountlogin(Context ctx, final int loginType, String username, String password, final CallbackListener listener) {
        HY_Log_TimeUtils.setStartLogin();
        logReport("3", "开始登录SDK", HY_Log_TimeUtils.startLogin - HY_Log_TimeUtils.initSdkSuccess + "");
        Map<String, String> map = commonRequestData(new HashMap<String, String>());
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

/*    public void accountlogin(Context ctx, String username, String password, final LoginCallBackListener listener) {
        HY_Log_TimeUtils.setStartLogin();
        logReport("3", "开始登录SDK", HY_Log_TimeUtils.startLogin - HY_Log_TimeUtils.initSdkSuccess + "");
        Map<String, String> map = commonRequestData(new HashMap<String, String>());
        map.put("username", username);
        map.put("password", MD5Utils.MD5(password));
        User.getInstance().accountlogin(ctx, map, new LoginCallBackListener() {
            @Override
            public void onLoginSuccess(HY_User user) {
                if (mUserListener != null)
                    mUserListener.onSwitchUser(user);
            }

            @Override
            public void onLoginCancel() {

            }

            @Override
            public void onLoginFailed(String message) {

            }
        });
    }*/

    public void logout(Context context, final CallbackListener listener) {
        Logger.i(TAG, "logout");
        Map<String, String> map = commonRequestData(new HashMap<String, String>());
        map.put("usecret", SharedPreferenceHelper.getAccessToken());
        map.put("uid", SharedPreferenceHelper.getUserId());
        User.getInstance().logout(context, map, listener);
    }

    public void roleReport(Context context, GameRoleInfo gameRoleInfo, CallbackListener listener) {
        Logger.i(TAG, "roleReport");
        Map<String, String> map = commonRequestData(new HashMap<String, String>());
        map.put("u9uid", SharedPreferenceHelper.getChannelUserId());
        map.put("role_id", gameRoleInfo.getRoleId());
        map.put("role_name", gameRoleInfo.getRoleName());
        map.put("role_level", gameRoleInfo.getRoleLevel());
        map.put("zone_id", gameRoleInfo.getZoneId());
        map.put("zone_name", gameRoleInfo.getZoneName());
        map.put("balance", gameRoleInfo.getBalance() + "");
        map.put("vip", gameRoleInfo.getVip());
        map.put("party_name", gameRoleInfo.getPartyName());
        User.getInstance().roleReport(context, map, listener);
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

    /**
     * 打开个人中心（设置中心）
     *
     * @param context
     * @param listener
     */
    public void showAccountCenter(Context context, CallbackListener listener) {
        Logger.i(TAG, "showAccountCenter");
        Dialog dialog = new AccountCenterDialog(context, listener);
        dialog.show();
    }


    /**
     * 支付入口
     *
     * @param context
     * @param payParams
     * @param payListener
     * @return
     */
    public boolean pay(final Context context, final PayParams payParams, final CallbackListener payListener) {
        Logger.i(TAG, "pay");
        checkPaymentMethod(context, new CallbackListener() {
            @Override
            public void onResult(ResultCode resultCode, String msg, String data) {
                if (resultCode == ResultCode.SUCCESS) {
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        String url = jsonObject.optString("url");
                        String order_id = jsonObject.optString("order_id");

                        payParams.setOrderId(order_id);
                        payParams.setPayUrl(url);
                        payParams.setPayChannel("GOOGLEPAY");

                        startPay(context, payParams, payListener);
                    } catch (JSONException e) {
                        Logger.e(TAG, "pay", e);
                    }
                } else {
                    payListener.onResult(resultCode, msg, data);
                }
            }
        });
        return false;
    }

    //检查支付参数配置,是否切换支付方式
    private void checkPaymentMethod(Context context, CallbackListener listener) {
        Logger.i(TAG, "createPaymentOrder");
        Map<String, String> params = commonRequestData(new HashMap<String, String>());
        params.put("total_fee", 99 + "");
        params.put("u9uid", SharedPreferenceHelper.getUserId());
        params.put("role_id", "1235698465");
        params.put("guid", SharedPreferenceHelper.getChannelUserId());
        params.put("token", SharedPreferenceHelper.getAccessToken());
        Pay.getInstance().checkPaymentMethod(context, params, listener);
    }

    private void startPay(final Context context, final PayParams payParams, final CallbackListener payListener) {
        Logger.i(TAG, "startPay");
        Map<String, String> map = new HashMap<>();
        map.put("ChannelId", Constant.CHANNEL_CODE);
        map.put("UserId", SharedPreferenceHelper.getUserId());
        map.put("ProductId", Constant.APPID);
        map.put("ProductOrderId", payParams.getGameOrderId());
        map.put("Amount", payParams.getAmount() + "");
        map.put("DeviceId", PhoneInfoHelper.deviceId);
        map.put("CallbackUrl", payParams.getCallBackUrl());
        map.put("AppExt", payParams.getAppExtInfo());
        map.put("Aid", Constant.PLAN_ID);
        map.put("PayChannel", payParams.getPayChannel());
        map.put("IsSwitchPayChannel", 1 + "");
        Pay.getInstance().startPay(context, map, new CallbackListener() {
            @Override
            public void onResult(ResultCode resultCode, String msg, String data) {
                if (resultCode == ResultCode.SUCCESS) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(data);
                        String public_key = jsonObject.optString("public_key");
//                        String g_pay_public_key = jsonObject.optString("g_pay_public_key");
                        payParams.setGooglePublicKey(public_key);

                        startGooglePayActivity(context, payParams, payListener);
//                        payListener.onResult(ResultCode.SUCCESS, msg, data);

                    } catch (JSONException e) {
                        Logger.e(TAG, "startPay", e);
                    }
                } else {
                    payListener.onResult(resultCode, msg, data);
                }
            }
        });
    }

    public void startGooglePayActivity(Context context, PayParams params, CallbackListener payListener) {
        Logger.i(TAG, "startGooglePayActivity");
        params.setU9uid(SharedPreferenceHelper.getChannelUserId());
        U9Platform.payCallback = payListener;
        U9Platform.mPayParams = params;
        Intent intent = new Intent();
        intent.setClass(context, HY_GameCenterActivity.class);
        context.startActivity(intent);
    }

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
        Map<String, String> params = commonRequestData(new HashMap<String, String>());
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

    //sdk退出
    public void exit(Context context, CallbackListener listener) {
        logout(context, listener);
    }

    public void onStart() {
        Sdk.getInstance().onStart();
    }

    public void onResume() {
        Sdk.getInstance().onResume();
    }

    public void onPause() {
        Sdk.getInstance().onPause();
    }

    public void onStop() {
        Sdk.getInstance().onStop();
    }

    public void onDestroy() {
        Sdk.getInstance().onDestroy();
        //释放资源
        OkHttpUtils.release();

    }

    public Map<String, String> commonRequestData(Map<String, String> paramsMap) {
        paramsMap.put("type", "0");
        if (!TextUtils.isEmpty(PhoneInfoHelper.deviceId)) {
            paramsMap.put("device", PhoneInfoHelper.deviceId);
        }
        paramsMap.put("aid", Constant.PLAN_ID);
        paramsMap.put("app_id", Constant.APPID);
        paramsMap.put("app", Constant.APPID);
        paramsMap.put("appversion", AppUtils.APP_NAME);
        paramsMap.put("imei", PhoneInfoHelper.imei);
        paramsMap.put("channel", Constant.CHANNEL_CODE);
        paramsMap.put("channel_id", Constant.CHANNEL_CODE);
        paramsMap.put("sub_channel", Constant.CHANNEL_ID);
        paramsMap.put("sub_channel_id", Constant.CHANNEL_ID);
        paramsMap.put("sdk_version", Constant.HY_SDK_VERSION_CODE);
        return paramsMap;
    }

    public interface SDKListener {
        void onSwitchAccount(HYUser user);
    }
}
