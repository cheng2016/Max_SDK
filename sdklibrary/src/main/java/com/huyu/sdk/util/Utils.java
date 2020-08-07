package com.huyu.sdk.util;

import android.content.Context;
import android.text.TextUtils;

public class Utils {

    private static String packageName;


    public static String getStringId(Context paramActivity, String id) {
        String packageName = getPackageName(paramActivity);
        return paramActivity.getResources().getString(
                paramActivity.getResources().getIdentifier(id, "string",
                        packageName));
    }

    public static String getPackageName(Context context) {
        if(!TextUtils.isEmpty(packageName)){
            return packageName;
        }
        packageName = context.getPackageName();
        return packageName;
    }

    public static String generatingSign(String paramString){
        return "";
    }
}
