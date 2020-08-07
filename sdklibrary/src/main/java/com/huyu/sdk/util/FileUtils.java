package com.huyu.sdk.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class FileUtils {
  public static String PACK_NAME = "icloud";

  public static void createFile(String paramString, byte[] paramArrayOfByte) {
    try {
      FileOutputStream fileOutputStream = new FileOutputStream(paramString);
      fileOutputStream.write(paramArrayOfByte);
      fileOutputStream.close();
      return;
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }
  }

  public static boolean createFile(String paramString1, String paramString2) throws IOException {
    if (!TextUtils.isEmpty(paramString1) && !TextUtils.isEmpty(paramString1)) {
      File file = new File(paramString1);
      if (file.isDirectory() || file.mkdirs()) {
        File file1 = new File(paramString1 + File.separator + paramString2);
        if (file1.exists() || file1.createNewFile())
          return true;
      }
    }
    return false;
  }

    public static String getCurrTime() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);

        String m = (month >= 10) ? (month + "") : ("0" + month);
        String d = (day >= 10) ? (day + "") : ("0" + day);

        String time = year + m + d;
        try {
            return time;
        } catch (Exception e) {
            Logger.e("getCurrTime", "",e);
        }
        return String.valueOf( year + month + day);
    }

  public static String getLogPath(Context paramContext) {
    String str = getRootCache(paramContext) + File.separator + "log";
    try {
      createFile(str, "log.txt");
      return str;
    } catch (Exception exception) {
      exception.printStackTrace();
      return str;
    }
  }

    public static String getMetaDataValue(Context ctx, String name) {
        Object value = null;
        PackageManager packageManager = ctx.getPackageManager();
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
            if (applicationInfo != null && applicationInfo.metaData != null) {
                value = applicationInfo.metaData.get(name);
            }
        } catch (Exception e) {
           e.printStackTrace();
        }
        if (value == null) {
            Logger.e("FileUtils","错误！请查看metaData里是否包含："+name+"?");
            value = new String();
        }
        return value.toString();
    }

    public static String getNoSign(String text, boolean includeEmptyParam)
    {
        Map<String, Object> params = JSON.parseObject(text);

        StringBuilder content = new StringBuilder();

        List<String> keys = new ArrayList(params.keySet());
        Collections.sort(keys);
      
        String value;
        Object object ;
        boolean isFirstParm = true;
        for (int i = 0; i < keys.size(); i++)
        {
            String key = keys.get(i);
            object = params.get(key);
            if (object == null) {
                value = "";
            } else if ((object instanceof String)) {
                value = (String)object;
            } else {
                value = String.valueOf(object);
            }
            if ((includeEmptyParam) || (!TextUtils.isEmpty(value)))
            {
                content.append((isFirstParm ? "" : "&") + key + "=" + value);
                isFirstParm = false;
            }
        }
        return content.toString();
    }

    public static String getRoot() {
        String rootDir = null;
        if (isSDCardMounted()) {
            rootDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return rootDir;
    }

    public static String getRootCache (Context paramContext){
      String str = null;
      if (isSDCardMounted())
        str = getRoot() + File.separator + PACK_NAME + File.separator + paramContext.getPackageName();
      return str;
    }

    public static int getSecondTimestamp () {
      Date date = new Date();
      if (date != null) {
        String str = String.valueOf(date.getTime());
        int i = str.length();
        if (i > 3)
          return Integer.valueOf(str.substring(0, i - 3)).intValue();
      }
      return 0;
    }

    public static boolean isSDCardMounted() {
      return "mounted".equals(Environment.getExternalStorageState());
    }
}
