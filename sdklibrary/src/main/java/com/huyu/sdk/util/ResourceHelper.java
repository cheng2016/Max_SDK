package com.huyu.sdk.util;

import android.content.Context;
import android.text.TextUtils;

/**
 * 文件名：ResourceHelper
 * 创建日期：2020/8/13 18:48
 * 描述：资源查找帮助类
 */


public class ResourceHelper {

    public static int getLayoutId(Context paramActivity, String id) {
        String packageName = getPackageName(paramActivity);
        return paramActivity.getResources().getIdentifier(id, "layout",
                packageName);
    }

    public static int getId(Context paramActivity, String id) {
        String packageName = getPackageName(paramActivity);
        return paramActivity.getResources()
                .getIdentifier(id, "id", packageName);
    }

    public static int getStyleId(Context paramActivity, String id) {
        String packageName = getPackageName(paramActivity);
        return paramActivity.getResources().getIdentifier(id, "style",
                packageName);
    }

    public static String getStringId(Context paramActivity, String id) {
        String packageName = getPackageName(paramActivity);
        return paramActivity.getResources().getString(paramActivity.getResources().getIdentifier(id, "string",
                        packageName));
    }

    public static String getPackageName(Context context) {
        if(!TextUtils.isEmpty(packageName)){
            return packageName;
        }
        packageName = context.getPackageName();
        return packageName;
    }

    private static String packageName;
}


