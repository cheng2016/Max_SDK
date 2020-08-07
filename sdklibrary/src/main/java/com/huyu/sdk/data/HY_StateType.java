package com.huyu.sdk.data;

public class HY_StateType {
	/** 登录服务器成功-指进入游戏内 */
	public final static int ENTER_SERVER = 0;
	/** 创建角色 */
	public final static int CREATE_ROLE = 1;
	/** 角色升级 */
	public final static int LEVEL_UP = 2;
	/***新手引导完成*/
	public final static int ROLE_GUIDE_END = 3;

	/** 退出 */
	public final static int EXIT_QUIT = 3;
	/** 登录 */
	public final static int DO_LOGIN = 0;
	/** 切换账号 */
	public final static int SWITCH_ACCOUNT = 1;
	/** 支付 */
	public final static int DO_PAY = 2;
	
	public class Mode{
		/** formal */
		public final static int HYSDK_FORMAL = 0;
		/** test */
		public final static int HYSDK_TEST = 2;
		/** formal_log */
		public final static int HYSDK_FORMAL_WITH_LOG = 3;

	}
}