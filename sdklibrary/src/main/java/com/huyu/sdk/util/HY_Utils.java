package com.huyu.sdk.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * 文件名：HY_Utils
 * 创建日期：2020/8/6 12:29
 * 描述：TODO
 */
public class HY_Utils {
    private static final String TAG = "HY_Utils";

    /**
     *
     * getHYChannelName(获取渠道名称)
     */
    public static String getHYChannelType(Context context) {
        return getManifestMeta(context, "HY_CHANNEL_TYPE");// 渠道名
    }

    /**
     *
     * getHYChannelCode(获取游戏渠道号)
     */
    public static String getHYChannelCode(Context context) {
        return getManifestMeta(context, "HY_CHANNEL_CODE");// 渠道号

    }

    /**
     *
     * getHYGameId(获取游戏号)
     */
    public static String getHYGameId(Context context) {

        return getManifestMeta(context, "HY_GAME_ID");// 游戏号，用于区分游戏

    }

    public static String getManifestMeta(Context context, String key) {
        String result;
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), 128);

            result = appInfo.metaData.get(key) + "";// 可能为null
        } catch (PackageManager.NameNotFoundException e) {
            throw new IllegalStateException("read meta " + key + " error", e);
        } catch (Exception e) {
            result = "";
            // throw new IllegalStateException("read meta " + key + " error",
            // e);
        }
        if ((result == null) || (result.length() == 0)) {
            // throw new IllegalStateException("no meta " + key + " found");
            Logger.d(TAG, "-->IllegalStateException(no meta " + key + " found");
        }
        if (result.equalsIgnoreCase("null")) {
            result = "";
        }
        return result;
    }
}
