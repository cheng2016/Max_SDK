package com.huyu.sdk.data;

public class U9_HttpUrl {
	
	public static String URL_U9_HOST = ""; // 正式1.

	/** 初始化 */
	public static String URL_INIT = "";
	/** 登录地址 */
	public static String URL_LOGIN = "";
	/** 手机注册 */
	public static String URL_PHONE_LOGIN = "";
	/** 手机注册 - 获取验证码 */
	public static String URL_PHONE_GET = "";
	/** 绑定手机号 */
	public static String URL_BIND_PHONE = "";
	/** 修改密码 */
	public static String URL_REVISE_PSD = "";
	/** 注册 */
	public static String URL_REGISTER = "";
	/** 一键注册 */
	public static String URL_ONEKEYREGISTER = "";
	/** 检查登录 */
	public static String URL_LOGIN_CHECKLOGIN = "";
	/** 注销 */
	public static String URL_LOGOUT =  "";
	/** 支付 */
	public static String URL_PAY = "";
	/** 获取支付方式 */
	public static String URL_GET_PAYTYPE = "";
	/** 检查支付结果 */
	public static String URL_CHECKPAY = "";
	/** 获取用户信息 */
	public static String URL_GETUSER = "";
	/** 获取服务信息 */
	public static String URL_GETSERVER = "";
	public static String URL_RESETPASS_PHONE = "";
	/** 客服信息 */
	public static String URL_PRODUCT_INFO = "";
	/** 超级权限登录 */
	public static String URL_SUPER_ADMIN = "";
	/** 检查支付方式 */
	public static String URL_GET_PAYPYTE = "";
	/** 检查登录结果 */
	public static String URL_CHECK_TOKEN = "";
	/** GameCenter */
	public static String URL_GAMECENTER = "";
	/** logReport */
	public static String URL_LOGREPORT = "";
	/** QGSK 分享内容请求 */
	public static String URL_QGSK_SHARE_REQ = "";
	/** QGSK 分享结果通知 */
	public static String URL_QGSK_SHARE_NOTIFY = "";
	/** QGSK QQ会员请求 */
	public static String URL_QGSK_VIP_REQ = "";
	/**辉耀客服系统请求*/
	public static String URL_QYCS_HY="";

	public static String URL_CHECK_PASSWORD = "";
	public static String URL_REVISE_PASSWORD = "";
	
	public static void initURL(){
		/** 初始化 */
		URL_INIT = URL_U9_HOST + "/hy/init";
		/** 登录地址 */
		URL_LOGIN = URL_U9_HOST + "/user/login";
		/** 手机注册 */
		URL_PHONE_LOGIN = URL_U9_HOST + "/user/mobileRegister";
		/** 手机注册 - 获取验证码 */
		URL_PHONE_GET = URL_U9_HOST + "/public/getVerifyCode";
		/** 绑定手机号 */
		URL_BIND_PHONE = URL_U9_HOST + "/user/bindMobile";
		/** 修改密码 */
		URL_REVISE_PSD = URL_U9_HOST + "/user/updatePassword";
		/** 注册 */
		URL_REGISTER = URL_U9_HOST + "/user/register";
		/** 一键注册 */
		URL_ONEKEYREGISTER = URL_U9_HOST + "/user/onekeyRegister";
		/** 检查登录 */
		URL_LOGIN_CHECKLOGIN = URL_U9_HOST + "/user/checkToken";
		/** 注销 */
		URL_LOGOUT = URL_U9_HOST + "/user/logout";
		/** 支付 */
		URL_PAY = URL_U9_HOST + "/pay/getOrder";
		/** 获取支付方式 */
		URL_GET_PAYTYPE = URL_U9_HOST + "/pay/getChannelList";
		/** 检查支付结果 */
		URL_CHECKPAY = URL_U9_HOST + "/pay/getNotifyStatus";
		/** 获取用户信息 */
		URL_GETUSER = URL_U9_HOST + "/user/getUserInfo";
		/** 获取服务信息 */
		URL_GETSERVER = URL_U9_HOST + "/hy/getServiceInfo";
		URL_RESETPASS_PHONE = URL_U9_HOST + "/user/resetPassword";
		/** 客服信息 */
		URL_PRODUCT_INFO = URL_U9_HOST + "/u9/about";
		/** 超级权限登录 */
		URL_SUPER_ADMIN = URL_U9_HOST + "/admin/login";
		/** 检查支付方式 */
		URL_GET_PAYPYTE = URL_U9_HOST + "/u9/checkPay";
		/** 检查登录结果 */
		URL_CHECK_TOKEN = URL_U9_HOST + "/user/checkToken";
		/** GameCenter */
		URL_GAMECENTER = URL_U9_HOST + "/pay/finish";
		/** logReport */
		URL_LOGREPORT = URL_U9_HOST + "/hy/logReport";
		/** QGSK 分享内容请求 */
		URL_QGSK_SHARE_REQ = URL_U9_HOST + "/qgsk/reqShare";
		/** QGSK 分享结果通知 */
		URL_QGSK_SHARE_NOTIFY = URL_U9_HOST + "/qgsk/shareNotify";
		/** QGSK QQ会员请求 */
		URL_QGSK_VIP_REQ = URL_U9_HOST + "/qgsk/reqQQvip";
		/**辉耀客服系统*/
		URL_QYCS_HY="http://cs.huiyaohuyu.cc/test.html";

		URL_CHECK_PASSWORD = "http://api.huiyaoabc.com/user/bindEmail";
		URL_REVISE_PASSWORD = "http://api.huiyaoabc.com/user/forgetPassword";
	}
}
