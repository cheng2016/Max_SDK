package com.huyu.sdk.data.config;

import android.content.Context;
import android.text.TextUtils;

import com.huyu.sdk.data.Constant;
import com.huyu.sdk.data.HY_StateType;
import com.huyu.sdk.data.HttpUrl;
import com.huyu.sdk.data.U9_HttpUrl;
import com.huyu.sdk.util.Logger;

/**
 * @author chengzj
 * @time 2020/7/20 16:22
 * Description: ChannelInfo 渠道信息
 */
public class ChannelInfoHelper {

    private int mode = 0;
    // 对外是否隐藏悬浮小球
    private String isShowFloat;
    // 修改请求地址
    private String host;
    // 渠道回调地址
    private String channelCallbackUrl;
    // 可选参数，获取其他参数信息1
    private String mReservedParam1;

    private static ChannelInfoHelper instance;

    public static ChannelInfoHelper getInstance(){
        if (instance == null)
            instance = new ChannelInfoHelper();
        return instance;
    }

    public void init (Context context){
        String modeString = AssetsConfigHelper.getInstance(context).get("mode");
        
        if(!TextUtils.isEmpty(modeString)){
            try{
                mode = Integer.valueOf(modeString);
            }catch(Exception e){
                mode = 0;
            }
        }
        isShowFloat = AssetsConfigHelper.getInstance(context).get( "isShowFloat");
        channelCallbackUrl = AssetsConfigHelper.getInstance(context).get("channelCallbackUrl");
        mReservedParam1  = AssetsConfigHelper.getInstance(context).get("isGameQuite");

        Logger.i("","");

        initMode();

        if (TextUtils.isEmpty(channelCallbackUrl)) {
            this.channelCallbackUrl = HttpUrl.URL_PAY_CALLBACK + "/" + Constant.APPID + "/" + Constant.CHANNEL_CODE;
            Logger.d("HY", "url Init:" + this.channelCallbackUrl);
        }
    }


    private void initMode() {
        switch (mode) {
            case HY_StateType.Mode.HYSDK_FORMAL:
                // sz 正式
                Logger.isDebug = false;
                Constant.isDebug = "false";
                HttpUrl.URL_HOST = Constant.URL_HOST_FORMAL;
                // sz 正式
                U9_HttpUrl.URL_U9_HOST = Constant.URL_HOST_FORMAL;
                break;
            case HY_StateType.Mode.HYSDK_TEST:
                // 测试模式
                Logger.isDebug = true;
                Constant.isDebug = "true";
                HttpUrl.URL_HOST = Constant.URL_HOST_OVERSEAS_TEST;
                // 测试
                U9_HttpUrl.URL_U9_HOST = Constant.URL_HOST_OVERSEAS_TEST;
                break;
            case HY_StateType.Mode.HYSDK_FORMAL_WITH_LOG:
                // sz Log
                Logger.isDebug = true;
                Constant.isDebug = "true";
                HttpUrl.URL_HOST = Constant.URL_HOST_FORMAL;
                // sz log
                U9_HttpUrl.URL_U9_HOST = Constant.URL_HOST_FORMAL;
                break;
        }

        // 初始化url
        HttpUrl.initURL();
        // 初始化url
        U9_HttpUrl.initURL();
    }

}
