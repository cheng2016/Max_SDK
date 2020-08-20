package com.huyu.sdk.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class SDKTools {

    public static String getAppName(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            String packageNames = (String) info.applicationInfo
                    .loadLabel(context.getPackageManager());
            return packageNames;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "找不到应用名称";

    }

    public static boolean isNetworkAvailable(Context activity) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService("connectivity");
            NetworkInfo localNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return (localNetworkInfo != null && localNetworkInfo.isAvailable());
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }


    public static boolean isNullOrEmpty(String str) {
        return (str == null || str.trim().length() == 0);
    }


    public static String getAssetConfigs(Context context, String assetsFile) {
        InputStreamReader reader = null;
        BufferedReader br = null;
        try {
            reader = new InputStreamReader(context.getAssets().open(assetsFile));
            br = new BufferedReader(reader);

            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                    br = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                    reader = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        return null;
    }


    public static Map<String, String> getAssetPropConfig(Context context, String assetsPropertiesFile) {
        try {
            Properties pro = new Properties();
            pro.load(new InputStreamReader(context.getAssets().open(assetsPropertiesFile), "UTF-8"));
            Map<String, String> result = new HashMap<String, String>();
            for (Map.Entry<Object, Object> entry : pro.entrySet()) {
                String keyStr = entry.getKey().toString().trim();
                String keyVal = entry.getValue().toString().trim();
                if (!result.containsKey(keyStr)) {
                    result.put(keyStr, keyVal);
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String getMetaData(Context ctx, String key) {
        try {
            ApplicationInfo appInfo = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(), 128);
            if (appInfo != null && appInfo.metaData != null && appInfo.metaData.containsKey(key)) {
                return "" + appInfo.metaData.get(key);
            }
            Log.e("U8SDK", "The meta-data key is not exists." + key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void copyToClipBoard(final Activity activity, final String s) {
        activity.runOnUiThread(new Runnable() {
            @SuppressLint({"NewApi"})
            public void run() {
                ClipboardManager cmb = (ClipboardManager) activity.getSystemService("clipboard");
                cmb.setText(s);
            }
        });
    }


    public static Map<String, String> collectDeviceInfo(Context ctx) {
        Map<String, String> info = new HashMap<>();
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo p = pm.getPackageInfo(ctx.getPackageName(), 1);
            if (p != null) {
                String versionName = (p.versionName == null) ? "null" : p.versionName;
                String versionCode = p.versionCode + "";
                info.put("versionName", versionName);
                info.put("versionCode", versionCode);
            }

            Field[] fields = android.os.Build.class.getDeclaredFields();
            for (Field f : fields) {
                try {
                    f.setAccessible(true);
                    Object obj = f.get(null);
                    info.put(f.getName(), (obj == null) ? "null" : obj.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            Log.e("U8SDK", "an error occured when collect package info...");
            e.printStackTrace();
        }

        return info;
    }


    public static String getSystemKeyboard(final Activity activity) {
        FutureTask<String> futureResult = new FutureTask<>(new Callable<String>() {
            @SuppressLint({"NewApi"})
            public String call() throws Exception {
                ClipboardManager cmb = (ClipboardManager) activity.getSystemService("clipboard");
                if (cmb.hasText()) {
                    return cmb.getText().toString();
                }
                return "";
            }
        });

        activity.runOnUiThread(futureResult);
        try {
            return (String) futureResult.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return "";
    }


    public static String getLogicChannel(Context context, String prefix) {
        ApplicationInfo appInfo = context.getApplicationInfo();
        String sourceDir = appInfo.sourceDir;
        String key = "META-INF/" + prefix;
        ZipFile zip = null;
        try {
            zip = new ZipFile(sourceDir);
            Enumeration entries = zip.entries();
            String ret = null;
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String entryName = entry.getName();
                if (entryName.startsWith(key)) {
                    ret = entryName;

                    break;
                }
            }
            if (!TextUtils.isEmpty(ret)) {

                String[] split = ret.split("_");
                if (split != null && split.length >= 2) {
                    return ret.substring(split[0].length() + 1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (zip != null) {
                try {
                    zip.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    /**
     * getMainClassByChannelName (通过渠道名获取主业务类的方法)
     *
     * @param
     * @return
     */
    public static Object getMainClassByChannelName(Context context)
            throws ClassNotFoundException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException {
        String hy_channelName = getMetaData(context, "HY_CHANNEL_TYPE");
        String className = "com.huyu.game" + "." + hy_channelName + "_Channel";
        Log.i("Logger ChannelName", "className : " + className);
        Class channelNameclazz = Class.forName(className);
//            Method method = channelNameclazz.getMethod("getInstance");
        Object object = channelNameclazz.newInstance();
        return object;
    }


    public static String getApplicationName(Context context) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = context.getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            applicationInfo = null;
        }
        return (String) packageManager.getApplicationLabel(applicationInfo);
    }

    /**
     * 适配 Android 10 根据硬件信息拼凑出uuid
     *
     * @return
     */
   /* public static String getUUID() {
        String serial = null;
        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10; //13 位
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                serial = android.os.Build.getSerial();
            } else {
                serial = Build.SERIAL;
            }
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }*/
    public static ProgressDialog showProgressTip(Activity context, String title) {
        ProgressDialog loadingActivity = new ProgressDialog(context);
        loadingActivity.setIndeterminate(true);
        loadingActivity.setCancelable(true);
        loadingActivity.setMessage(title);
        loadingActivity.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface arg0) {
            }
        });


        loadingActivity.show();
        return loadingActivity;
    }


    public static void hideProgressTip(ProgressDialog dialog) {
        if (dialog == null) {
            return;
        }
        dialog.dismiss();
        dialog = null;
    }

    public static int getId(Context paramActivity, String id) {
        String packageName = paramActivity.getPackageName();
        return paramActivity.getResources()
                .getIdentifier(id, "id", packageName);
    }

    public static int getLayoutId(Context paramActivity, String id) {
        String packageName = paramActivity.getPackageName();
        return paramActivity.getResources().getIdentifier(id, "layout",
                packageName);
    }
}


