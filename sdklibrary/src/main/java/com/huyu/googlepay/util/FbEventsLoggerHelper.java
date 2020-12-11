package com.huyu.googlepay.util;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;

import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.huyu.sdk.util.Logger;

import java.math.BigDecimal;
import java.util.Currency;

public class FbEventsLoggerHelper {
    public static final String TAG = FbEventsLoggerHelper.class.getSimpleName();
    private static FbEventsLoggerHelper fbEventsLoggerHelper;


    private AppEventsLogger appEventsLogger;

    public static FbEventsLoggerHelper getInstance(Activity mActivity){
        if (fbEventsLoggerHelper == null){
            fbEventsLoggerHelper = new FbEventsLoggerHelper(mActivity);
        }
        return fbEventsLoggerHelper;
    }


    private FbEventsLoggerHelper(Activity mActivity){
        appEventsLogger = AppEventsLogger.newLogger(mActivity);
    }

    public void loginEvent(){
        //fb登录事件
        if (appEventsLogger != null){
            Logger.d(TAG, "登录事件上报>>>");
            appEventsLogger.logEvent("login");
        }

    }

    public void registerEvent() {
        //fb 完成注册事件
        if (appEventsLogger != null) {
            Logger.d(TAG, "注册事件上报>>>");
            appEventsLogger.logEvent(AppEventsConstants.EVENT_NAME_COMPLETED_REGISTRATION);
        }
    }

    public void buyEvent(String money, String contentId){
        //Map<String, Object> eventValue = new HashMap<String, Object>();
        Double dMoney = 0.0;
        if (!TextUtils.isEmpty(money)) {
            dMoney = Double.parseDouble(money)/100;
            Logger.d(TAG, "Fb上传充值金额>>>"+dMoney);
        }
        Bundle parameters = new Bundle();
        parameters.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID,contentId);
        if (appEventsLogger != null) {
            Logger.d(TAG, "Fb充值事件上报>>>");
            appEventsLogger.logPurchase(BigDecimal.valueOf(dMoney), Currency.getInstance("USD"), parameters);
        }
    }
}
