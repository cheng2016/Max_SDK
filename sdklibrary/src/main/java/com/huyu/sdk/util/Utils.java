package com.huyu.sdk.util;

import android.text.TextUtils;

import com.huyu.sdk.data.Constant;
import com.huyu.sdk.data.bean.HYUser;
import com.huyu.sdk.data.bean.PayParams;
import com.huyu.sdk.data.config.PhoneInfoHelper;
import com.huyu.sdk.data.config.SharedPreferenceHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Utils {
    public static final String TAG = Utils.class.getSimpleName();

    private Utils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static Map<String, String> getLoginInfoRequest() {
        HYUser user = new HYUser();
        user.token = SharedPreferenceHelper.getAccessToken();
        user.channelUserId = SharedPreferenceHelper.getChannelUserId();
        user.channelUserName = SharedPreferenceHelper.getChannelUserName();
        return getLoginInfoRequest(user);
    }

    public static Map<String, String> getLoginInfoRequest(HYUser user) {
        JSONObject phoneInfo = new JSONObject();
        try {
            phoneInfo.put("Imei", PhoneInfoHelper.imei);
            phoneInfo.put("Imsi", PhoneInfoHelper.imsi);
        } catch (JSONException e) {
            Logger.e(TAG, "getLoginInfoRequest commonlogin ", e);
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
        map.put("BundleId", AppUtils.APP_PACKAGE_NAME);
        map.put("LoginType", SharedPreferenceHelper.getLoginType() + "");
//        map.put("Ext", SharedPreferenceHelper.getChannelUserId());
        return map;
    }

    public static Map<String, String> getPayParamsMap(PayParams payParams) {
        HYUser user = new HYUser();
        user.userId = SharedPreferenceHelper.getUserId();
        return getPayParamsMap(payParams, user);
    }

    public static Map<String, String> getPayParamsMap(PayParams payParams, HYUser user) {
        Map<String, String> map = new HashMap<>();
        map.put("ChannelId", Constant.CHANNEL_CODE);
        map.put("UserId", user.userId);
        map.put("ProductId", Constant.APPID);
        map.put("ProductOrderId", payParams.getGameOrderId());
        map.put("Amount", payParams.getAmount() + "");
        map.put("DeviceId", PhoneInfoHelper.deviceId);
        map.put("CallbackUrl", payParams.getCallBackUrl());
        map.put("AppExt", payParams.getAppExtInfo());
        map.put("Aid", Constant.PLAN_ID);
        map.put("PayChannel", payParams.getPayChannel());
        map.put("IsSwitchPayChannel", SharedPreferenceHelper.getIsSwitchPayChannel() + "");
        map.put("appversion", AppUtils.APP_VERSION_NAME);
        map.put("BundleId", AppUtils.APP_PACKAGE_NAME);
        map.put("CommodityId", payParams.getProductId());
        return map;
    }

    public static Map<String, String> commonRequestData(Map<String, String> paramsMap) {
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
        paramsMap.put("BundleId", AppUtils.APP_PACKAGE_NAME);
        return paramsMap;
    }
}
