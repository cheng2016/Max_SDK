package com.huyu.sdk.splash;

//import com.hy.game.bugly.HY_Bugly;

import android.content.Context;
import android.text.TextUtils;

import com.huyu.sdk.data.Constant;
import com.huyu.sdk.util.HY_Utils;
import com.huyu.sdk.util.Logger;


public class HY_GameInit {

	private static final String TAG = "HY";
	private static String channelType = "";
	private static String channelCode = "";
	private static String hyGameId = "";

	public static void initHYGameInfo(Context context) {
		Logger.d(TAG, "SDK版本:" + Constant.HY_SDK_VERSION);
		init(context);
		Logger.d(TAG, "HY_GanmeInit--->Application 调用");
	}

	private static void init(Context context) {
		channelCode = HY_Utils.getHYChannelCode(context);
		channelType = HY_Utils.getHYChannelType(context);
		hyGameId = HY_Utils.getHYGameId(context);

		if ((TextUtils.isEmpty(Constant.CHANNEL_CODE)
				|| TextUtils.isEmpty(Constant.CHANNEL_TYPE) || TextUtils
					.isEmpty(Constant.APPID))
				|| ("0".equals(Constant.CHANNEL_CODE) || "0"
						.equals(Constant.APPID))) {
			Constant.CHANNEL_CODE = channelCode;
			Constant.CHANNEL_TYPE = channelType;
			Constant.APPID = hyGameId;
		} else {
			return;
		}
		for (int i = 0; i < 10; i++) {
			if (TextUtils.isEmpty(Constant.CHANNEL_CODE)
					|| "0".equals(Constant.CHANNEL_CODE)) {
				Constant.CHANNEL_CODE = HY_Utils.getHYChannelCode(context);
				continue;
			} else {
				break;
			}
		}
		for (int i = 0; i < 10; i++) {
			if (TextUtils.isEmpty(Constant.CHANNEL_TYPE)) {
				Constant.CHANNEL_TYPE = HY_Utils.getHYChannelType(context);
				continue;
			} else {
				break;
			}
		}
		for (int i = 0; i < 10; i++) {
			if (TextUtils.isEmpty(Constant.APPID)
					|| "0".equals(Constant.APPID)) {
				Constant.APPID = HY_Utils.getHYGameId(context);
				continue;
			} else {
				break;
			}
		}

		Logger.d(TAG, "HY_GameInit--->channelCode--->"
				+ Constant.CHANNEL_CODE + "channelType--->"
				+ Constant.CHANNEL_TYPE + ",HYGameId--->"
				+ Constant.APPID);
		if (TextUtils.isEmpty(channelCode)) {
			Logger.d(TAG, "请在AndroidManifest.xml文件中配置渠道信息(HY_CHANNEL_CODE)");
		}
		if (TextUtils.isEmpty(channelType)) {
			Logger.d(TAG, "请在AndroidManifest.xml文件中配置渠道信息(HY_CHANNEL_TYPE)");
		}
		if (TextUtils.isEmpty(hyGameId)) {
			Logger.d(TAG, "请在AndroidManifest.xml文件中配置渠道信息(HY_GAME_ID)");
		}
	}
}
