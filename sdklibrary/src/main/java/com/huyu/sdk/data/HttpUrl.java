package com.huyu.sdk.data;

public class HttpUrl {

	public static String GOOGLE_PAY = "GOOGLEPAY ";

	/** 接口地址(公用一个) */
	public static String URL_HOST = "" + "";

	// 登录地址
	public static String URL_LOGIN = "";
	// 支付地址
	public static String URL_PAY = "";
	// 支付回调
	public static String URL_PAY_CALLBACK = "";
	// 登录校验地址
	public static String URL_CHECKLOGIN = "";
	// Check user
	public static String URL_CHECK_USER = "";
	// 角色信息
	public static String URL_ROLE_REPORT = "";
	// u9初始化
	public static String U9_INIT = "";
	// qqvip_url
	public static String U9_QQVIP = "";
	//获取订单支付信息
	public static String URL_GET_ORDER_LIST = "";

	// 角色信息
	public static String URL_BIND_ACCOUNT = "";


	public static void initURL() {
		// check_user
		URL_CHECK_USER = URL_HOST + "/pay/check";
		// 角色信息
		URL_ROLE_REPORT = URL_HOST + "/user/roleReport";
		// u9初始化
		U9_INIT = URL_HOST + "/u9/init";
		// u9 vip
		U9_QQVIP = URL_HOST + "/qqvip/getUrl";

		// 登录地址 /
		URL_LOGIN = URL_HOST + "/login/loginRequest";
		// 登录校验
		URL_CHECKLOGIN = URL_HOST + "/validateLogin/index";
		// 支付地址
		URL_PAY = URL_HOST + "/payRequest/payRequest";
		// 支付回调
		URL_PAY_CALLBACK = URL_HOST + "/payNotify/channelPayNotify";
		//获取订单信息
		URL_GET_ORDER_LIST = URL_HOST + "/user/listOrder";

		//绑定账户
		URL_BIND_ACCOUNT = URL_HOST + "/user/bindAccount";
		// if (mode == HY_StateType.Mode.HYSDK_SZ
		// || mode == HY_StateType.Mode.HYSDK_SZ_LOG) {
		// URL_PAY_CALLBACK =
		// "http://api1.hygame.cc/payNotify/channelPayNotify";
		// }
	}
}
