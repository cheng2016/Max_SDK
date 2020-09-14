package com.huyu.sdk.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;


public class AppUtils {
    public static final String TAG = AppUtils.class.getSimpleName();
    private static AppUtils instance;

    public static String APP_NAME = "";
    public static String APP_VERSION_NAME = "";
    public static int APP_VERSION_CODE;
    public static String APP_PACKAGE_NAME = "";

    public static AppUtils getInstance() {
        if (instance == null)
            instance = new AppUtils();
        return instance;
    }

    public void init(Context paramContext) {
        this.APP_NAME = getAppName(paramContext);
        this.APP_VERSION_NAME = getVersionName(paramContext);
        this.APP_VERSION_CODE = getVersionCode(paramContext);
        this.APP_PACKAGE_NAME = getPackageName(paramContext);
        Logger.d(TAG, "App 信息 APP_NAME = "
                + APP_NAME + "?APP_VERSION_NAME = "
                + APP_VERSION_NAME + "&APP_VERSION_CODE = "
                + APP_VERSION_CODE + "&APP_PACKAGE_NAME = "
                + APP_PACKAGE_NAME);
    }


    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getPackageName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

