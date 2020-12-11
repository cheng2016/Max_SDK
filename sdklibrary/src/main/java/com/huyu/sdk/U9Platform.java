package com.huyu.sdk;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

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
import com.huyu.sdk.util.HY_Log_TimeUtils;
import com.huyu.sdk.util.Logger;
import com.huyu.sdk.util.NetworkUtil;
import com.huyu.sdk.util.OkHttpUtils;
import com.huyu.sdk.util.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * U9Platform  平台
 */
public class U9Platform {
    public static final String TAG = U9Platform.class.getSimpleName();
    private static U9Platform instance;

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
        Sdk.getInstance().initU9Server(context, Utils.commonRequestData(map), sdkInitListener);
    }


    //最终登录入口
    public void verifylogin(Context context, CallbackListener listener) {
        Logger.i(TAG, "verifylogin");
        User.getInstance().verifylogin(context, Utils.getLoginInfoRequest(), listener);
    }

    public void verifyChannelLogin(Context context, HYUser user, CallbackListener listener) {
        Logger.i(TAG, "verifyChannelLogin");
        User.getInstance().verifylogin(context, Utils.getLoginInfoRequest(user), listener);
    }

    public void startPay(final Context context, final PayParams payParams, final HYUser user, final CallbackListener payListener) {
        Logger.i(TAG, "startVerifyPay");
        Pay.getInstance().startPay(context, Utils.getPayParamsMap(payParams, user), payListener);
    }

    public void roleReport(Context context, GameRoleInfo gameRoleInfo, CallbackListener listener) {
        Logger.i(TAG, "roleReport");
        Map<String, String> paramsMap = new HashMap<>();
        Map<String, String> map = Utils.commonRequestData(paramsMap);
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
}
