package com.huyu.sdk.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class AppUtils {
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


/* Location:              C:\Users\mitni\Desktop\gitwork\AndroidTool\classes-dex2jar.jar!\com\icloud\sd\\utils\AppUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.6
 */
