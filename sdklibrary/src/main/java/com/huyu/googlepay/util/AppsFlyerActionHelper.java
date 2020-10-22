package com.huyu.googlepay.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.appsflyer.AppsFlyerLib;
import com.huyu.sdk.data.config.XmlConfigHelper;
import com.huyu.sdk.util.Logger;

import java.util.HashMap;
import java.util.Map;

/***
 * AppsFlyer 数据上报 工具类
 */
public class AppsFlyerActionHelper {
    public static final String TAG = AppsFlyerActionHelper.class.getSimpleName();

    /**
     * 新手引导完成
     */
    public static String ROLE_GUIDEEND = "af_role_guideend";
    /**
     * 创建角色
     */
    public static String CREATE_ROLE = "af_role_created";

    public static void initAppsFlyerSdk(Application context) {
        String devKey = XmlConfigHelper.getManifestMetaData(context, "AF_DEV_KEY");
        if (!TextUtils.isEmpty(devKey)) {
            AppsFlyerLib.getInstance().startTracking(context, devKey);
            AppsFlyerLib.getInstance().setDebugLog(true);
            Logger.d("HY", "AppFlyer初始化");
        } else {
            Logger.d("HY", "AppFlyer未初始化,未获取到dev_key");
        }
    }


    public static void buyEvent(Activity context, String money, String contentId) {
        Logger.d(TAG, "buyEvent   money ： " + money + " , contentId : " + contentId);
        Map<String, Object> eventValue = new HashMap<String, Object>();
        if (!TextUtils.isEmpty(money)) {
            Double dMoney = Double.parseDouble(money) / 100;
            Logger.d("HY", "AppFlyer上传充值金额>>>" + dMoney);
            eventValue.put(AFInAppEventParameterName.REVENUE, dMoney);
        }
        //eventValue.put(AFInAppEventParameterName.CONTENT_TYPE,"category_a");
        eventValue.put(AFInAppEventParameterName.CONTENT_ID, contentId);
        eventValue.put(AFInAppEventParameterName.CURRENCY, "USD");
        AppsFlyerLib.getInstance().trackEvent(context, AFInAppEventType.PURCHASE, eventValue);
        FbEventsLoggerHelper.getInstance(context).buyEvent(money,contentId);
    }


    public static void loginEvent(Activity context) {
        Logger.d(TAG, "loginEvent");
        AppsFlyerLib.getInstance().trackEvent(context, AFInAppEventType.LOGIN, null);
        FbEventsLoggerHelper.getInstance(context).loginEvent();
    }


    public static void registerEvent(Activity context) {
        Logger.d(TAG, "registerEvent");
        AppsFlyerLib.getInstance().trackEvent(context, AFInAppEventType.COMPLETE_REGISTRATION, null);
        FbEventsLoggerHelper.getInstance(context).registerEvent();
    }

    /**
     * 角色事件上报
     */
    public static void roleEvent(Context context, String roleEvent) {
        Logger.d(TAG, "roleEvent ： " + roleEvent);
        AppsFlyerLib.getInstance().trackEvent(context, roleEvent, null);
    }
}

