package com.huyu.sdk.impl;

import android.content.Context;
import android.text.TextUtils;

import com.huyu.sdk.U9Platform;
import com.huyu.sdk.data.Constant;
import com.huyu.sdk.data.HttpUrl;
import com.huyu.sdk.data.ResultCode;
import com.huyu.sdk.data.U9_HttpUrl;
import com.huyu.sdk.data.config.ChannelInfoHelper;
import com.huyu.sdk.data.config.PhoneInfoHelper;
import com.huyu.sdk.data.config.XmlConfigHelper;
import com.huyu.sdk.listener.CallbackListener;
import com.huyu.sdk.util.AppUtils;
import com.huyu.sdk.util.HY_Log_TimeUtils;
import com.huyu.sdk.util.Logger;
import com.huyu.sdk.util.OkHttpUtils;
import com.huyu.sdk.util.SharedPreferencesUtils;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * sdk具体实现类
 */
public class Sdk implements ISdk {
    static final String TAG = Sdk.class.getSimpleName();
    private static Sdk instance;

    public static Sdk getInstance() {
        if (instance == null)
            instance = new Sdk();
        return instance;
    }

    public void initApp(Context context) {
        //最好先初始化文件日志类
        Logger.init(context);
        //获取Manifest.xml配置信息
        XmlConfigHelper.getInstance().init(context);
        //获取assets下渠道信息
        ChannelInfoHelper.getInstance().init(context);
        //获取设备信息
        PhoneInfoHelper.getInstance().init(context);
        //获取app信息
        AppUtils.getInstance().init(context);
    }

    private String imeiWeb = "";
    private String deviceWeb = "";

    private String deviceFile = "";
    private String deviceSp = "";

    private String imeiFile = "";
    private String imeiSp = "";

    public void initU9Server(final Context context, Map<String, String> map, final CallbackListener sdkInitListener) {
        OkHttpUtils.postNoLoading(HttpUrl.U9_INIT, map, new OkHttpUtils.SimpleResponseHandler() {
            public void onFailure(Exception param1Exception) {
                if (sdkInitListener != null)
                    sdkInitListener.onResult(ResultCode.Fail, "initServer 初始化失败", param1Exception.toString());
            }

            public void onSuccess(Call param1Call, Response param1Response) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(param1Response.body().string()));
                    int status = jsonObject.optInt("status");
                    String message = jsonObject.optString("message");
                    if (status == 0) {
                        JSONObject dataObject = jsonObject.optJSONObject("data");
                        String fb_url = dataObject.optString("fb_url");
                        Constant.FB_CS_URL = fb_url;

                        imeiWeb = dataObject.optString("imei");
                        deviceWeb = dataObject.optString("device");

                        resetU9Device(context);
                        sdkInitListener.onResult(ResultCode.SUCCESS, "initServer 初始化成功", dataObject.toString());
                        Logger.i(TAG, "SDK 初始化成功");

                        HY_Log_TimeUtils.setInitSDKSuccess();
                        U9Platform.getInstance().logReport("21", "初始化成功", HY_Log_TimeUtils.initSdk-HY_Log_TimeUtils.startGame+"");
                    } else {
                        sdkInitListener.onResult(ResultCode.Fail, message, "");
                        Logger.i(TAG, "SDK 初始化失败");

                        HY_Log_TimeUtils.setInitSDKFail();
                        U9Platform.getInstance().logReport("22", "初始化失败", HY_Log_TimeUtils.initSdk-HY_Log_TimeUtils.startGame+"");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void logReport(Map<String, String> map) {
        final String step_desc = map.get("step_desc");
        OkHttpUtils.postNoLoading(U9_HttpUrl.URL_LOGREPORT, map, new OkHttpUtils.SimpleResponseHandler() {
            public void onFailure(Exception e) {
                Logger.e(TAG, "logReport  " + step_desc + "失败", e);
            }

            public void onSuccess(Call param1Call, Response param1Response) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(param1Response.body().string()));
                    int status = jsonObject.optInt("status");
                    String message = jsonObject.optString("message");
                    if (status == 0) {
                        Logger.i(TAG, "logReport  " + step_desc + " logReport success");
                    } else {
                        Logger.i(TAG, "logReport  " + step_desc + " logReport fail");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void onStart(){}

    public void onResume(){}

    public void onPause() {}

    public void onStop() {}

    public void onDestroy() {}


    private void resetU9Device(Context context) {
        deviceSp = SharedPreferencesUtils.getData(context, "u9device", "");
        imeiSp = SharedPreferencesUtils.getData(context, "u9imei", "");

        deviceFile = PhoneInfoHelper.getFile("u9device" + ".txt");
        imeiFile = PhoneInfoHelper.getFile("u9imei" + ".txt");

        // imei号读取存储
        if (!TextUtils.isEmpty(imeiFile)) {
            // 优先级1 文件存储
            SharedPreferencesUtils.putData(context, "u9imei", imeiFile);
            PhoneInfoHelper.saveFile("u9imei" + ".txt", imeiFile);
            PhoneInfoHelper.imei = imeiFile;

        } else if (!TextUtils.isEmpty(imeiSp)) {
            // 优先级2 storage存储
            PhoneInfoHelper.saveFile("u9imei" + ".txt", imeiSp);
            PhoneInfoHelper.imei = imeiSp;

        } else if (!TextUtils.isEmpty(imeiWeb)) {
            // 优先级3 网络请求
            PhoneInfoHelper.saveFile("u9imei" + ".txt", imeiWeb);
            SharedPreferencesUtils.putData(context, "u9imei", imeiWeb);
            PhoneInfoHelper.imei = imeiWeb;
        }
        // device读取存储
        if (!TextUtils.isEmpty(deviceWeb)) {
            // 优先级3 网络请求
            PhoneInfoHelper.saveFile("u9device" + ".txt", deviceWeb);
            SharedPreferencesUtils.putData(context, "u9device", deviceWeb);
            PhoneInfoHelper.deviceId = deviceWeb;

        } else if (!TextUtils.isEmpty(deviceSp)) {
            // 优先级2 storage存储
            PhoneInfoHelper.saveFile("u9device" + ".txt", deviceSp);
            PhoneInfoHelper.deviceId = deviceSp;
        } else if (!TextUtils.isEmpty(deviceFile)) {
            // 优先级1 文件存储
            SharedPreferencesUtils.putData(context, "u9device", deviceFile);
            PhoneInfoHelper.saveFile("u9device" + ".txt", deviceFile);
            PhoneInfoHelper.deviceId = deviceFile;
        }
        Logger.d("imei:" + PhoneInfoHelper.imei);
        Logger.d("deviceId:" + PhoneInfoHelper.deviceId);
    }
}