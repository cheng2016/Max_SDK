package com.huyu.sdk.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;

import com.huyu.sdk.U9Platform;
import com.huyu.sdk.util.SDKParams;
import com.huyu.sdk.util.SDKTools;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;


@SuppressLint({"UseSparseArrays"})
public class PluginFactory {
    private static PluginFactory instance;
    private Map<Integer, String> supportedPlugins = new HashMap();


    public static PluginFactory getInstance() {
        if (instance == null) {
            instance = new PluginFactory();
        }

        return instance;
    }


    private boolean isSupportPlugin(int type) {
        return this.supportedPlugins.containsKey(Integer.valueOf(type));
    }


    private String getPluginName(int type) {
        if (this.supportedPlugins.containsKey(Integer.valueOf(type))) {
            return (String) this.supportedPlugins.get(Integer.valueOf(type));
        }
        return null;
    }


    public Bundle getMetaData(Context context) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);

            if (appInfo != null && appInfo.metaData != null) {
                return appInfo.metaData;
            }
        } catch (PackageManager.NameNotFoundException nameNotFoundException) {
        }


        return new Bundle();
    }

    public SDKParams getSDKParams(Context context) {
        Map<String, String> configs = SDKTools.getAssetPropConfig(context, "hy_developer_config.properties");
        return new SDKParams(configs);
    }


    public Object initPlugin( int type) {
        Class localClass = null;
        try {
            if (!isSupportPlugin(type)) {
                if (type == 1 || type == 2) {
                    Log.e("HYSDK", "The config of the HYSDK is not support plugin type:" + type);
                } else {
                    Log.w("HYSDK", "The config of the HYSDK is not support plugin type:" + type);
                }
                return null;
            }
            String pluginName = getPluginName(type);
            localClass = Class.forName(pluginName);
        } catch (ClassNotFoundException e) {

            e.printStackTrace();
            return null;
        }

        try {
            return localClass.getDeclaredConstructor(new Class[]{android.app.Activity.class}).newInstance(new Object[]{U9Platform.getInstance().getContext()});
        } catch (Exception e) {


            try {
                return localClass.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            } catch (Exception e1) {
                e1.printStackTrace();

                e.printStackTrace();

                return null;
            }
        }
    }

    public void loadPluginInfo(Context context) {
        String xmlPlugins = SDKTools.getAssetConfigs(context, "u8_plugin_config.xml");

        if (xmlPlugins == null) {

            Log.e("HYSDK", "fail to load plugin_config.xml");

            return;
        }
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(new StringReader(xmlPlugins));

            int eventType = parser.getEventType();
            while (eventType != 1) {
                String tag;
                switch (eventType) {
                    case 2:
                        tag = parser.getName();
                        if ("plugin".equals(tag)) {
                            String name = parser.getAttributeValue(0);
                            int type = Integer.parseInt(parser.getAttributeValue(1));
                            this.supportedPlugins.put(Integer.valueOf(type), name);
                            Log.d("HYSDK", "Curr Supported Plugin: " + type + "; name:" + name);
                        }
                        break;
                }
                eventType = parser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


