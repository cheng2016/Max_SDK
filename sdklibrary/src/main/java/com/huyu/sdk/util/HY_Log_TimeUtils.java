package com.huyu.sdk.util;

import java.util.Date;

public class HY_Log_TimeUtils {
	// 启动游戏
	public static long startGame = 0;
	// 初始化SDK
	public static long initSdk = 0;
	// 初始化失败
	public static long initSdkFail = 0;
	// 初始化成功
	public static long initSdkSuccess = 0;
	// 开始登录
	public static long startLogin = 0;
	// 登录成功
	public static long loginSuccess = 0;
	// 登录失败
	public static long loginFail = 0;
	// 进入游戏
	public static long enterGame = 0;
	// 创建角色
	public static long createRole = 0;

	// 启动游戏onCreate
	public static void setStartGame() {
		startGame = new Date().getTime() / 1000;
	}

	// 初始化sdk
	public static void setInitSDK() {
		initSdk = new Date().getTime() / 1000;
	}

	// 初始化sdk
	public static void setInitSDKFail() {
		initSdkFail = new Date().getTime() / 1000;
	}

	// 初始化sdk
	public static void setInitSDKSuccess() {
		initSdkSuccess = new Date().getTime() / 1000;
	}

	// 开始登陆
	public static void setStartLogin() {
		startLogin = new Date().getTime() / 1000;
	}

	// 登陆成功
	public static void setLoginSuccess() {
		loginSuccess = new Date().getTime() / 1000;
	}

	// 登陆失败
	public static void setLoginFail() {
		loginFail = new Date().getTime() / 1000;
	}

	// 进入游戏
	public static void setEnterGame() {
		enterGame = new Date().getTime() / 1000;
	}

	// 创建角色
	public static void setCreateTime() {
		createRole = new Date().getTime() / 1000;
	}
}