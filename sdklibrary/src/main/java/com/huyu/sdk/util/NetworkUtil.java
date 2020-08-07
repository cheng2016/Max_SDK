package com.huyu.sdk.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.List;

/**
 * @author chengzj
 * @time 2020/7/20 17:43
 * Description: NetworkUtil
 */
public class NetworkUtil {
    public static boolean isAvilible(Context paramContext, String paramString) {
        List list = paramContext.getPackageManager().getInstalledPackages(0);
        if (list != null)
            for (byte b = 0; b < list.size(); b++) {
                if (((PackageInfo)list.get(b)).packageName.equals(paramString))
                    return true;
            }
        return false;
    }

    public static boolean isNetworkAvailable(Context paramContext) {
        NetworkInfo networkInfo = ((ConnectivityManager)paramContext.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED);
    }
}
