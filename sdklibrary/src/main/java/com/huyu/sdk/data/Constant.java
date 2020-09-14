package com.huyu.sdk.data;

/**
 * @author chengzj
 * @time 2020/7/25 10:54
 * Description: Constans 真正的常量类，所以变量都用 static final 修饰
 */
public final class Constant {
    //海外正式接口域名
    public static final String URL_HOST_FORMAL = "" + "http://api.huiyaoabc.com";
    //海外测试接口域名
    public static final String URL_HOST_OVERSEAS_TEST = "" + "http://test.api.gdbygame.com";

    /** 接口地址(SZ测试) */
    public static final String URL_HOST_TEST = "" + "http://test.u9php.hygame.cc";
    /**接口地址（GZ测试）*/
    public static final String URL_HOST_GZ_TEST = "" + "http://test.u9php.huiyaohuyu.cc";


    public static final class U9URL {

        /** 接口地址(gf) */
        public static String URL_HOST_GF = "http://api.huiyaohuyu.cn";
        public static String URL_HOST_U9API_GF = "http://u9api.huiyaohuyu.cn"; // 正式1.
    }

    /**
     * 闪屏assets下文件夹路径
     */
    public static final String SPLASH_PIC_ASSETS = "splash_photo";
    /**
     * HU_CONFIG_FILENAME sdk的配置信息 ,保存到assets下面"wx_onekey_config_"+
     * HYChannelName + ".json
     */
    public static final String HY_GAME_CONFIG = "hy_game.json";
    /**
     * 系统设置文件名
     */
    public static final String SYS_FILE_NAME = "sysfile";
    /**
     * 身份标志
     */
    public static final String L_KEY = "LKEY";
    /**
     * 预留接口 SDK 版本信息
     **/
    public static final String HY_SDK_VERSION = "1.5.1";
    public static final String HY_SDK_VERSION_CODE = "2";

    public static final String IS_REQUEST_PERMISSION = "isRequestPermission";

    /**
     * 0:不启用 1:启用
     */
    public static String OPEN_ONLY_MOBILE_REG = "0";
    /**
     * 0:不启用 1:启用
     */
    public static String OPEN_REAL_NAME_AUTH = "0";
    /**
     *
     */
    public static String realNameJson = "";


    //faceBook辉耀客服主页主页地址
    public static String FB_CS_URL = "";
    /**
     * 测试环境
     */
    public static String isDebug = "false";// true为测试环境

    public static String APPID = "0";
    // 渠道id,推广渠道id
    public static String CHANNEL_ID = "0";
    // 渠道标识,例如:百度:110
    public static String CHANNEL_CODE = "0";

    // 广告计划id,与 推广id进行绑定
    public static String PLAN_ID = "0";

    // 渠道类型,例如百度:baidu
    public static String CHANNEL_TYPE = "";
}
