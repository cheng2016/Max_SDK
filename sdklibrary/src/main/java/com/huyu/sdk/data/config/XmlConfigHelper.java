package com.huyu.sdk.data.config;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.huyu.sdk.data.Constant;
import com.huyu.sdk.data.HttpUrl;
import com.huyu.sdk.util.Logger;
import com.huyu.sdk.util.SharedPreferencesUtils;

/**
 * @author chengzj
 * @time 2020/7/20 16:00
 * Description: XML、ASSETS、CHANNEL等数据工具类
 */
public class XmlConfigHelper {
    public static final String TAG = XmlConfigHelper.class.getSimpleName();
    private static XmlConfigHelper instance;

    public String CHANNEL_ID = "default";

    public String GAME_ID = "default";

    private String PLAN_ID = "default";

    public String CHANNEL_CODE = "default";

    public String HY_CHANNEL_TYPE = "default";

    public static XmlConfigHelper getInstance() {
        if (instance == null)
            instance = new XmlConfigHelper();
        return instance;
    }

    public void init(Context paramContext) {
        this.GAME_ID = TextUtils.isEmpty(getManifestMetaData(paramContext, "HY_GAME_ID")) ? "1000" : getManifestMetaData(paramContext, "HY_GAME_ID");
        this.CHANNEL_CODE = TextUtils.isEmpty(getManifestMetaData(paramContext, "HY_CHANNEL_CODE")) ? "100" : getManifestMetaData(paramContext, "HY_CHANNEL_CODE");
        this.HY_CHANNEL_TYPE = getManifestMetaData(paramContext, "HY_CHANNEL_TYPE");
        initData(paramContext);
    }

    private void initData(Context context) {
        // 渠道id
        Logger.d("-----------------开始初始化渠道信息-------------------");
        CHANNEL_ID = SharedPreferencesUtils.getChannelId(context);
        PLAN_ID = SharedPreferencesUtils.getPlanId(context);
        Constant.APPID = this.GAME_ID;
        Constant.CHANNEL_ID = this.CHANNEL_ID;
        Logger.d("sub_channel:" + Constant.CHANNEL_ID);
        Constant.CHANNEL_CODE = this.CHANNEL_CODE;
        Constant.PLAN_ID = this.PLAN_ID;
    }

    public static String getManifestMetaData(Context ctx, String key) {
        Object value = null;
        PackageManager packageManager = ctx.getPackageManager();
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
            if (applicationInfo != null && applicationInfo.metaData != null) {
                value = applicationInfo.metaData.get(key);
            }
        } catch (Exception e) {
            Logger.e(TAG, "-->getMetaDataValue Exception<--", e);
        }
        if (value == null) {
            Logger.w(TAG, "错误！请查看Manifest metaData里是否包含：" + key + "?");
            value = new String();
        }
        return value.toString();
    }

    public static String getPayCallbackUrl(Activity paramActivity) {
        String gameCode =  getManifestMetaData(paramActivity, "HY_GAME_ID");
        String channelCode =  getManifestMetaData(paramActivity, "HY_CHANNEL_CODE");
        String url = HttpUrl.URL_PAY_CALLBACK + "/" + gameCode + "/" + channelCode;
        return url;
    }
}
