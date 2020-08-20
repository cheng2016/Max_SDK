package com.huyu.sdk.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;



public class AppUtils {
    private static AppUtils instance;

    public static String APP_NAME = "";
    public static int APP_VERSION_CODE;

    public static AppUtils getInstance() {
        if (instance == null)
            instance = new AppUtils();
        return instance;
    }

    public void init(Context paramContext) {
        this.APP_NAME = getVersionName(paramContext);
        this.APP_VERSION_CODE = getVersionCode(paramContext);
    }

    public static int getVersionCode(Context context) {
        int versionCode = 0;
        PackageManager pm = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = pm.getPackageInfo(context.getApplicationContext().getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        if (info != null) {
            versionCode = info.versionCode;
        }
        return versionCode;
    }
  
    public static String getVersionName(Context context) {
        String versionName = null;
        PackageManager pm = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = pm.getPackageInfo(context.getApplicationContext().getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        if (info != null) {
            versionName = info.versionName;
        }
        return versionName;
    }
}

