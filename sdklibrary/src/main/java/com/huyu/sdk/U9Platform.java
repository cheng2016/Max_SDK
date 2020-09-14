package com.huyu.sdk;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.huyu.sdk.data.Constant;
import com.huyu.sdk.data.HttpUrl;
import com.huyu.sdk.data.ResultCode;
import com.huyu.sdk.data.bean.GameRoleInfo;
import com.huyu.sdk.data.bean.HYUser;
import com.huyu.sdk.data.bean.PayParams;
import com.huyu.sdk.data.config.PhoneInfoHelper;
import com.huyu.sdk.data.config.SharedPreferenceHelper;
import com.huyu.sdk.data.config.XmlConfigHelper;
import com.huyu.sdk.impl.Pay;
import com.huyu.sdk.impl.Sdk;
import com.huyu.sdk.impl.User;
import com.huyu.sdk.listener.CallbackListener;
import com.huyu.sdk.util.AppUtils;
import com.huyu.sdk.util.HY_Log_TimeUtils;
import com.huyu.sdk.util.Logger;
import com.huyu.sdk.util.NetworkUtil;
import com.huyu.sdk.util.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * U9Platform  平台
 */
public class U9Platform {
    public static final String TAG = U9Platform.class.getSimpleName();
    private static U9Platform instance;

    public static CallbackListener payCallback;
    public static PayParams mPayParams;

    public Context mApplication;

    public Context getApplication() {
        return mApplication;
    }

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
     * @param application
     */
    public void initApplication(Application application) {
        this.mApplication = application;
        Sdk.getInstance().initApp(application);
    }

    /**
     * 服务器信息初始化
     *
     * @param context
     * @param sdkInitListener
     */
    public void initActivity(Activity context, CallbackListener sdkInitListener) {
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

    public void verifyChannelLogin(Context context, HYUser user, CallbackListener listener) {
        Logger.i(TAG, "verifyChannelLogin");
        User.getInstance().verifylogin(context, getLoginInfoRequest(user), listener);
    }

    public void startPay(final Context context, final PayParams payParams, final HYUser user, final CallbackListener payListener) {
        Logger.i(TAG, "startVerifyPay");
        Pay.getInstance().startPay(context, getPayInfoRequest(payParams, user), payListener);
    }

    public void roleReport(Context context, GameRoleInfo gameRoleInfo, CallbackListener listener) {
        Logger.i(TAG, "roleReport");
        Map<String, String> paramsMap = new HashMap<>();
        Map<String, String> map = commonRequestData(paramsMap);
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
        //释放资源
        OkHttpUtils.release();
        Sdk.getInstance().onDestroy();
    }

    public static Map<String, String> getLoginInfoRequest(HYUser user) {
        JSONObject phoneInfo = new JSONObject();
        try {
            phoneInfo.put("Imei", PhoneInfoHelper.imei);
            phoneInfo.put("Imsi", PhoneInfoHelper.imsi);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Map<String, String> map = new HashMap<>();
        map.put("ChannelId", Constant.CHANNEL_CODE);
        map.put("IsDebug", Constant.isDebug);
        map.put("Token", user.token);
        map.put("ProductId", Constant.APPID);
        map.put("ChannelUserId", user.channelUserId);
        map.put("MobileInfo", phoneInfo.toString());
        map.put("ChannelUserName", user.channelUserName);
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
                        + user.channelUserName + "&PhoneInfo="
                        + phoneInfo.toString());
        return map;
    }

    public static Map<String, String> getPayInfoRequest(PayParams payParams, HYUser mHYUserVo) {
        Map<String, String> map = new HashMap<>();
        map.put("ChannelId", Constant.CHANNEL_CODE);
        map.put("UserId", mHYUserVo.userId);
        map.put("ProductId", Constant.APPID);
        map.put("ProductOrderId", payParams.getGameOrderId());
        map.put("Amount", payParams.getAmount() + "");
        map.put("DeviceId", PhoneInfoHelper.deviceId);
        map.put("CallbackUrl", payParams.getCallBackUrl());
        map.put("AppExt", payParams.getAppExtInfo());
        map.put("Aid", Constant.PLAN_ID);
        map.put("appversion", AppUtils.APP_VERSION_NAME);
        map.put("BundleId", AppUtils.APP_PACKAGE_NAME);
        Logger.d(
                TAG,
                "支付请求信息: " + HttpUrl.URL_PAY + "?ChannelId="
                        + Constant.CHANNEL_CODE + "&UserId="
                        + mHYUserVo.userId + "&ProductId="
                        + Constant.APPID + "&ProductOrderId="
                        + payParams.getGameOrderId() + "&Amount="
                        + payParams.getAmount() + "&CallbackUrl="
                        + payParams.getCallBackUrl() + "&appversion="
                        + AppUtils.APP_VERSION_NAME + "&BundleId="
                        + AppUtils.APP_PACKAGE_NAME);
        return map;
    }

    public Map<String, String> commonRequestData(Map<String, String> paramsMap) {
        paramsMap.put("type", "0");
        if (!TextUtils.isEmpty(PhoneInfoHelper.deviceId)) {
            paramsMap.put("device", PhoneInfoHelper.deviceId);
        }
        paramsMap.put("aid", Constant.PLAN_ID);
        paramsMap.put("app_id", Constant.APPID);
        paramsMap.put("app", Constant.APPID);
        paramsMap.put("appversion", AppUtils.APP_VERSION_NAME);
        paramsMap.put("imei", PhoneInfoHelper.imei);
        paramsMap.put("channel", Constant.CHANNEL_CODE);
        paramsMap.put("channel_id", Constant.CHANNEL_CODE);
        paramsMap.put("sub_channel", Constant.CHANNEL_ID);
        paramsMap.put("sub_channel_id", Constant.CHANNEL_ID);
        paramsMap.put("sdk_version", Constant.HY_SDK_VERSION_CODE);
        return paramsMap;
    }
}
