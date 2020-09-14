package com.huyu.sdk.data.config;

import android.content.Context;
import android.util.Log;

import com.huyu.sdk.data.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AssetsConfigHelper {
    private static final String TAG = "AssetsJsonConfig";
    private static AssetsConfigHelper instance;
    private Map<String, String> map = new HashMap<String, String>();

    public static AssetsConfigHelper getInstance(Context context) {
        if (instance == null)
            instance = new AssetsConfigHelper(context);
        return instance;
    }

    private AssetsConfigHelper(Context context) {
        try {
            InputStream is = context.getAssets().open(Constant.HY_GAME_CONFIG);

            InputStreamReader reader = new InputStreamReader(is);
            char[] buf = new char[1024];
            StringBuffer buffer = new StringBuffer();
            while (reader.read(buf) > 0) {
                buffer.append(buf);
            }
            is.close();
            JSONObject json = new JSONObject(buffer.toString());
            Iterator<?> iter = json.keys();
            String key = null;
            while (iter.hasNext()) {
                key = (String) iter.next();
                Log.d(TAG, key);
                this.map.put(key, json.get(key).toString());
            }
            Log.d(TAG, "assets下已经存在渠道参数配置的文件：" + Constant.HY_GAME_CONFIG);

        } catch (IOException e) {
            Log.d(TAG, "打包工具没有将信息 " + Constant.HY_GAME_CONFIG
                    + "配置在assets下");
            throw new IllegalStateException(e);
        } catch (JSONException e) {
            throw new IllegalStateException(e);
        }
    }

    public String get(String key) {
        return (String) this.map.get(key);
    }
}
