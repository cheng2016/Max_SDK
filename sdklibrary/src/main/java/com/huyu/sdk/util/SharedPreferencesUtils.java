package com.huyu.sdk.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.text.TextUtils;

import com.huyu.sdk.data.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author chengzj
 * @time 2020/7/20 10:13
 * Description: SharedPreferences 数据管理类
 */
public class SharedPreferencesUtils {
    private SharedPreferencesUtils(){}

    /**
     * 简单数据存储(存)
     **/
    @SuppressLint("CommitPrefEdits")
    public static void putData(Context context, String key, String data) {
        getSharedPreferences(context).edit().putString(key, data).commit();
    }

    public static void putData(Context context, String key, JSONObject data) {
        putData(context,key, data.toString());
    }

    public static void putData(Context context, String key, boolean data) {
        getSharedPreferences(context).edit().putBoolean(key, data).commit();

    }

    public static void putData(Context context, String key, int data) {
        getSharedPreferences(context).edit().putInt(key, data).commit();
    }

    /**
     * 简单数据存储(取)
     **/
    public static String getData(Context context, String key, String default_data) {
        return getSharedPreferences(context).getString(key, default_data);
    }

    public static boolean getData(Context context, String key, boolean default_data) {
        return getSharedPreferences(context).getBoolean(key, default_data);
    }

    public static int getData(Context context, String key, int default_data) {
        return getSharedPreferences(context).getInt(key, default_data);
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("u9", Context.MODE_PRIVATE);
    }


    public static boolean realName(Activity mActivity, String userName) {
        Constant.realNameJson = getData(mActivity, "RealName_Data", "");
        Logger.d("校验-->realNameJson:"
                + Constant.realNameJson);
        Logger.d("校验-->" + userName + "_RealName:"
                + getData(mActivity, userName + "_RealName", true));
        if (getData(mActivity, userName + "_RealName", true)) {
            putData(mActivity, userName + "_RealName", false);
            return true;
        }
        return false;
    }

    public static boolean realNameType(Activity mActivity, String jsonString,
                                       String userName) {
        String type = "";
        Logger.i("jsonString:"+jsonString);
        Logger.i("userName:"+userName);
        try {
            if (!TextUtils.isEmpty(jsonString)) {
                JSONObject json = new JSONObject(jsonString);
                type = json.getString(userName);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Logger.e(e.toString());
            return false;
        }
        if (!TextUtils.isEmpty(type)) {
            return true;
        }
        return false;
    }


    public static String getChannelId(Context context) {
        String channel = Constant.CHANNEL_ID;
        if (!TextUtils.isEmpty(channel) && !"0".equals(channel)) {
            return channel;
        }

        final String start_flag = "META-INF/sub_channel_";
        ApplicationInfo appinfo = context.getApplicationInfo();
        String sourceDir = appinfo.sourceDir;
        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(sourceDir);
            Enumeration<?> entries = zipfile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String entryName = entry.getName();
                if (entryName.contains(start_flag)) {
                    channel = entryName.replace(start_flag, "");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (channel == null || channel.length() <= 0) {
            channel = "0";// 读不到渠道号就默认是官方渠道
        }

        String channelIdStorage =  getData(context, "sub_channel", "");

        // 渠道Id 读取优先级 存储数据 > 读取配置
        if (!TextUtils.isEmpty(channelIdStorage)
                && !"0".equals(channelIdStorage)) {
            channel = channelIdStorage;
        } else if (!TextUtils.isEmpty(channel) && !"0".equals(channel)) {
            putData(context, "sub_channel", channel);
        }

        return channel;
    }

    public static String getPlanId(Context context) {
        String planId = Constant.PLAN_ID;
        if (!TextUtils.isEmpty(planId) && !"0".equals(planId)) {
            Logger.d("直接返回 planId:" + planId);
            return planId;
        }

        final String start_flag = "META-INF/hy_plan_";
        ApplicationInfo appinfo = context.getApplicationInfo();
        String sourceDir = appinfo.sourceDir;
        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(sourceDir);
            Enumeration<?> entries = zipfile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String entryName = entry.getName();
                if (entryName.contains(start_flag)) {
                    planId = entryName.replace(start_flag, "");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (planId == null || planId.length() <= 0) {
            planId = "0";// 读不到渠道号就默认是官方渠道
            Logger.e("读不到渠道号就默认是官方渠道:0");
        }

        String planIdStorage = getData(context, "planid", "");

        // 渠道Id 读取优先级 存储数据 > 读取配置
        if (!TextUtils.isEmpty(planIdStorage) && !"0".equals(planIdStorage)) {
            planId = planIdStorage;
            Logger.d("planIdStorage读取成功:" + planId);
        } else if (!TextUtils.isEmpty(planId) && !"0".equals(planId)) {
            Logger.d("planId读取成功:" + planId);
            putData(context, "planid", planId);
        } else {
            Logger.e("planId读取异常:" + planId);
        }
        Logger.d("planId:" + planId);
        return planId;
    }
}
