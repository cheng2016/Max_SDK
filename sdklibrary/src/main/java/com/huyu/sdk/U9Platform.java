package com.huyu.sdk;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.huyu.sdk.impl.Pay;
import com.huyu.sdk.impl.Sdk;
import com.huyu.sdk.impl.User;
import com.huyu.sdk.data.Constant;
import com.huyu.sdk.data.HttpUrl;
import com.huyu.sdk.data.ResultCode;
import com.huyu.sdk.data.bean.GameRoleInfo;
import com.huyu.sdk.data.bean.PayParams;
import com.huyu.sdk.data.bean.HYUser;
import com.huyu.sdk.data.config.PhoneInfoHelper;
import com.huyu.sdk.data.config.SharedPreferenceHelper;
import com.huyu.sdk.data.config.XmlConfigHelper;
import com.huyu.sdk.listener.CallbackListener;
import com.huyu.sdk.util.AppUtils;
import com.huyu.sdk.util.HY_Log_TimeUtils;
import com.huyu.sdk.util.Logger;
import com.huyu.sdk.util.NetworkUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * U9Platform  平台
 */
public class U9Platform {
    public final static String TAG = U9Platform.class.getSimpleName();
    private static U9Platform instance;

    public static CallbackListener payCallback;
    public static PayParams mPayParams;
    private Context context;

    private U9Platform() {
    }

    public static U9Platform getInstance() {
        if (instance == null)
            instance = new U9Platform();
        return instance;
    }


    /**
     * 本地信息初始化
     *
     * @param atc
     */
    public void initApplication(Application atc) {
        Logger.i(TAG, "init");
        Sdk.getInstance().initApp(atc);
    }

    /**
     * 服务器信息初始化
     *
     * @param context
     * @param sdkInitListener
     */
    public void initActivity(Activity context, CallbackListener sdkInitListener) {
        this.context = context;
        if (!NetworkUtil.isNetworkAvailable(context)) {
            if (sdkInitListener != null)
                sdkInitListener.onResult(ResultCode.Fail, "网络连接失败", "");
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("mac", PhoneInfoHelper.macAddress);
        map.put("coordinate_lat", PhoneInfoHelper.latitude + "");
        map.put("coordinate_lng", PhoneInfoHelper.longitude + "");
        map.put("brand", "");
        map.put("brand_desc", PhoneInfoHelper.model);
        map.put("mobile", PhoneInfoHelper.phoneNum);
        map.put("tel_op", PhoneInfoHelper.spType);
        map.put("idfa", "");
        if (!TextUtils.isEmpty(PhoneInfoHelper.deviceId)) {
            map.put("device", PhoneInfoHelper.deviceId);
        }
        U9Platform.getInstance().logReport("2", "初始化SDK", HY_Log_TimeUtils.initSdk - HY_Log_TimeUtils.startGame + "");
        Sdk.getInstance().initU9Server(context, commonRequestData(map), sdkInitListener);
    }


    //最终登录入口
    public void verifylogin(Context context, CallbackListener listener) {
        Logger.i(TAG, "verifylogin");
        Map<String, String> map = new HashMap<>();
        JSONObject phoneInfo = new JSONObject();
        try {
            phoneInfo.put("Imei", PhoneInfoHelper.imei);
            phoneInfo.put("Imsi", PhoneInfoHelper.imsi);
        } catch (JSONException e) {
            Logger.e(TAG, "commonlogin", e);
        }
        map.put("Token", SharedPreferenceHelper.getAccessToken());
        map.put("ChannelId", Constant.CHANNEL_CODE);
        map.put("IsDebug", Constant.isDebug);
        map.put("ProductId", Constant.APPID);
        map.put("ChannelUserId", SharedPreferenceHelper.getChannelUserId());
        map.put("MobileInfo", phoneInfo.toString());
        map.put("ChannelUserName", SharedPreferenceHelper.getChannelUserName());
        map.put("DeviceId", PhoneInfoHelper.deviceId);
        map.put("Aid", Constant.PLAN_ID);
        map.put("LoginType", "0");
        map.put("Ext", SharedPreferenceHelper.getChannelUserId());
        User.getInstance().verifylogin(context, map, listener);
    }

    //最终登录入口
    public void verifylogin(Context context, HYUser user, CallbackListener listener) {
        Logger.i(TAG, "verifylogin");
        Map<String, String> map = new HashMap<>();
        JSONObject phoneInfo = new JSONObject();
        try {
            phoneInfo.put("Imei", PhoneInfoHelper.imei);
            phoneInfo.put("Imsi", PhoneInfoHelper.imsi);
        } catch (JSONException e) {
            Logger.e(TAG, "commonlogin", e);
        }
        map.put("Token", user.token);
        map.put("ChannelId", Constant.CHANNEL_CODE);
        map.put("IsDebug", Constant.isDebug);
        map.put("ProductId", Constant.APPID);
        map.put("ChannelUserId", user.channelUserId);
        map.put("MobileInfo", phoneInfo.toString());
        map.put("ChannelUserName", user.channelUserName);
        map.put("DeviceId", PhoneInfoHelper.deviceId);
        map.put("Aid", Constant.PLAN_ID);
        map.put("LoginType", "0");
        map.put("Ext", user.extInfo);
        User.getInstance().verifylogin(context, map, listener);
    }

    public void verifyChannelLogin(Context context, HYUser user, CallbackListener listener) {
        Logger.i(TAG, "verifyChannelLogin");
        User.getInstance().verifylogin(context, getLoginInfoRequest(user), listener);
    }

    public static Map<String, String> getLoginInfoRequest( HYUser user) {
        JSONObject phoneInfo = new JSONObject();
        try {
            phoneInfo.put("Imei", PhoneInfoHelper.imei);
            phoneInfo.put("Imsi", PhoneInfoHelper.imsi);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("ChannelId", Constant.CHANNEL_CODE);
        map.put("IsDebug", Constant.isDebug);
        map.put("Token", user.token);
        map.put("ProductId", Constant.APPID);
        map.put("ChannelUserId", user.channelUserId);
        map.put("MobileInfo", phoneInfo.toString());
        map.put("ChannelUserName", XmlConfigHelper.getInstance().HY_CHANNEL_TYPE);
        map.put("DeviceId", PhoneInfoHelper.deviceId);
        map.put("Aid", Constant.PLAN_ID);
        Logger.d(TAG, "设备信息:" + phoneInfo.toString());
        Logger.d(
                TAG,
                "登录请求信息: " + HttpUrl.URL_LOGIN + "?ChannelId="
                        + Constant.CHANNEL_CODE + "&IsDebug="
                        + Constant.isDebug + "&Token="
                        + user.token + "&ProductId="
                        + Constant.APPID + "&ChannelUserId="
                        + user.channelUserId + "&ChannelUserName="
                        + XmlConfigHelper.getInstance().HY_CHANNEL_TYPE + "&PhoneInfo="
                        + phoneInfo.toString());
        return map;
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

    public static Map<String, String> getPayInfoRequest(PayParams mPayParsms, HYUser mHYUserVo) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("ChannelId", Constant.CHANNEL_CODE);
        map.put("UserId", mHYUserVo.userId);
        map.put("ProductId", Constant.APPID);
        map.put("ProductOrderId", mPayParsms.getGameOrderId());
        map.put("Amount", mPayParsms.getAmount() + "");
        map.put("DeviceId", PhoneInfoHelper.deviceId);
        map.put("CallbackUrl", mPayParsms.getCallBackUrl());
        map.put("AppExt", mPayParsms.getAppExtInfo());
        map.put("Aid", Constant.PLAN_ID);

        Logger.d(
                TAG,
                "支付请求信息: " + HttpUrl.URL_PAY + "?ChannelId="
                        + Constant.CHANNEL_CODE + "&UserId="
                        + mHYUserVo.userId + "&ProductId="
                        + Constant.APPID + "&ProductOrderId="
                        + mPayParsms.getGameOrderId() + "&Amount="
                        + mPayParsms.getAmount() + "&CallbackUrl="
                        + mPayParsms.getCallBackUrl());
        return map;
    }

    public void startVerifyPay(final Context context, final PayParams payParams, final HYUser user, final CallbackListener payListener) {
        Logger.i(TAG, "startVerifyPay");
        Pay.getInstance().startPay(context, getPayInfoRequest(payParams, user), new CallbackListener() {
            @Override
            public void onResult(ResultCode resultCode, String msg, String data) {
                payListener.onResult(resultCode,msg,data);
            }
        });
    }

    public void startPay(final Context context, final PayParams payParams, final CallbackListener payListener) {
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
//                        U9Platform.getInstance().startGooglePayActivity(context, payParams, payListener);

                        payListener.onResult(ResultCode.SUCCESS, msg, data);
                    } catch (JSONException e) {
                        Logger.e(TAG, "startPay", e);
                    }
                } else {
                    payListener.onResult(resultCode, msg, data);
                }
            }
        });
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
    }

    public Context getContext() {
        return context;
    }

    public Map<String, String> commonRequestData(Map<String, String> paramsMap) {
        paramsMap.put("type", "0");
        if (!TextUtils.isEmpty(PhoneInfoHelper.deviceId)) {
            paramsMap.put("device", PhoneInfoHelper.deviceId);
        }
        paramsMap.put("aid", Constant.PLAN_ID);
        paramsMap.put("app_id", Constant.APPID);
        paramsMap.put("app", Constant.APPID);
        paramsMap.put("appversion", AppUtils.getVersionName(context));
        paramsMap.put("imei", PhoneInfoHelper.imei);
        paramsMap.put("channel", Constant.CHANNEL_CODE);
        paramsMap.put("channel_id", Constant.CHANNEL_CODE);
        paramsMap.put("sub_channel", Constant.CHANNEL_ID);
        paramsMap.put("sub_channel_id", Constant.CHANNEL_ID);
        paramsMap.put("sdk_version", Constant.HY_SDK_VERSION_CODE);
        return paramsMap;
    }
}
