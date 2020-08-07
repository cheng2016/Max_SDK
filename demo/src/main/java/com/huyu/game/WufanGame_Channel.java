/*
package com.huyu.game;

import android.app.Activity;
import android.app.Application;
import android.text.TextUtils;

import com.huyu.sdk.base.IChannelProxy;
import com.huyu.sdk.data.HY_StateType;
import com.huyu.sdk.data.bean.U9GameRoleInfo;
import com.huyu.sdk.data.bean.U9PayParams;
import com.huyu.sdk.data.bean.U9User;
import com.huyu.sdk.data.config.AssetsConfigHelper;
import com.huyu.sdk.listener.CallbackListener;
import com.huyu.sdk.listener.LoginCallBackListener;
import com.huyu.sdk.listener.PayCallbackListener;
import com.huyu.sdk.util.Logger;
import com.huyu.sdk.util.SDKTools;
import com.mokredit.payment.StringUtils;
import com.papa91.pay.callback.PPLoginCallBack;
import com.papa91.pay.callback.PPayCallback;
import com.papa91.pay.callback.PpaLogoutCallback;
import com.papa91.pay.pa.activity.PaayActivity;
import com.papa91.pay.pa.business.LoginResult;
import com.papa91.pay.pa.business.PPayCenter;
import com.papa91.pay.pa.business.PaayArg;
import com.papa91.pay.pa.business.PayArgsCheckResult;
import com.papa91.pay.pa.dto.LogoutResult;

*/
/**
 * 文件名：SDK_Manager
 * 创建日期：2020/8/4 18:45
 * 描述：TODO
 *//*

public class WufanGame_Channel extends IChannelProxy {
    public static final String TAG = WufanGame_Channel.class.getSimpleName();

    static WufanGame_Channel instance;

    public static WufanGame_Channel getInstance() {
        if (instance == null) {
            instance = new WufanGame_Channel();
        }
        return instance;
    }


    private int openUid;

    @Override
    public void initConfig(Application application) {
        PPayCenter.initConfig(application);
    }

    @Override
    public void init(Activity activity, CallbackListener listener) {

        PPayCenter.init(activity);
    }

    U9User user;

    @Override
    public void login(final Activity activity, final LoginCallBackListener listener) {
        */
/**
         * 登录游戏
         *//*

        PPayCenter.login(activity, new PPLoginCallBack() {
            @Override
            public void onLoginFinish(LoginResult result) {
                switch (result.getCode()) {
                    case LoginResult.LOGIN_CODE_APPID_NOT_FOUND:
                        //没找到appid
                        break;
                    case LoginResult.LOGIN_CODE_SUCCESS://登录成功
                        openUid = result.getOpenUid();
                        user = new U9User();
                        user.channelUserId = result.getOpenUid() + "";
                        user.token = result.getToken();
                        listener.onLoginSuccess(user);
                        PPayCenter.loginFinish(activity);
                        break;
                    case LoginResult.LOGIN_CODE_FAILED://登录失败
                        String message = result.getMessage();//失败详情
                        listener.onLoginFailed(message);
                        break;
                    case LoginResult.LOGIN_CODE_CANCEL:// 登录取消
                        listener.onLoginCancel();
                        break;
                    case LoginResult.NOT_INIT://没有调用 PPayCenter.init(activity);
                        listener.onLoginFailed("SDK 未初始化");
                        break;

                }
            }
        });
    }

    @Override
    public void logout(Activity activity) {

    }

    @Override
    public void switchAccount(Activity activity) {
        PPayCenter.changeAccount(new PPLoginCallBack() {
            @Override
            public void onLoginFinish(LoginResult loginResult) {
                if (loginResult.getCode() == LoginResult.LOGIN_CODE_SUCCESS) {
                    getUserListener().onLogout();
                }
            }
        });
    }

    @Override
    public void showAccountCenter(Activity activity) {
        PPayCenter.userCenter(activity);
    }

    U9GameRoleInfo u9GameRoleInfo;

    @Override
    public void roleReport(Activity activity, U9GameRoleInfo u9GameRoleInfo, CallbackListener listener) {
        this.u9GameRoleInfo = u9GameRoleInfo;
        if (u9GameRoleInfo.getTypeId() == HY_StateType.CREATE_ROLE) {
            PPayCenter.createRole(u9GameRoleInfo.getRoleName(), u9GameRoleInfo.getZoneName());
        } else if (u9GameRoleInfo.getTypeId() == HY_StateType.ENTER_SERVER) {
            PPayCenter.enterGame(u9GameRoleInfo.getRoleName(), u9GameRoleInfo.getZoneName());
        }

        Logger.d(TAG, u9GameRoleInfo.toString());
        Logger.d(TAG, "MethodManager-->setExtData");
    }

    @Override
    public void pay(Activity activity, final U9PayParams u9PayParams, final PayCallbackListener mPayCallBack) {
        PaayArg paayArg = new PaayArg();
        paayArg.APP_NAME = SDKTools.getAppName(activity);

        paayArg.APP_ORDER_ID = u9PayParams.getOrderId();
//        paayArg.APP_DISTRICT = 2;
//        paayArg.APP_SERVER = 1;

        paayArg.GAME_SERVER = TextUtils.isEmpty(u9GameRoleInfo.getZoneName()) ? "1" : u9GameRoleInfo.getZoneName();
        paayArg.APP_USER_ID = TextUtils.isEmpty(u9GameRoleInfo.getRoleId()) ? "1" : u9GameRoleInfo.getRoleId();
        paayArg.APP_USER_NAME = TextUtils.isEmpty(u9GameRoleInfo.getRoleName()) ? "1" : u9GameRoleInfo.getRoleName();

        if (StringUtils.isEmpty(u9PayParams.getAmount() + "")) {
            paayArg.MONEY_AMOUNT = "1.00";
        } else {
            paayArg.MONEY_AMOUNT = u9PayParams.getAmount() / 100 + "";
        }
//        paayArg.MONEY_AMOUNT = "0.01";
//        paayArg.NOTIFY_URI = "http://sdkapi.papa91.com/index.php/pay_center/test";

        paayArg.NOTIFY_URI = AssetsConfigHelper.getInstance(activity).get("channelCallbackUrl");

        paayArg.PRODUCT_ID = u9PayParams.getProductId();
        paayArg.PRODUCT_NAME = u9PayParams.getProductName();
        paayArg.PA_OPEN_UID = openUid;//调用登录方法，得到该值

        paayArg.APP_EXT1 = TextUtils.isEmpty(u9PayParams.getAppExtInfo()) ? "ext1 : " + u9PayParams.getGameOrderId() : u9PayParams.getAppExtInfo();
        paayArg.APP_EXT2 = TextUtils.isEmpty(u9PayParams.getAppExtInfo()) ? "ext2 : " + u9PayParams.getGameOrderId() : u9PayParams.getAppExtInfo();

        PPayCenter.pay(paayArg, new PPayCallback() {
            @Override
            public void onPayFinished(int status) {
                switch (status) {
                    case PayArgsCheckResult.CHECK_RESULT_PAY_CALLBACK_NULL:
//                                mmm = "参数错误:回调函数未配置";
                        mPayCallBack.onPayFailed("支付失败,参数错误:回调函数未配置");
                        break;
                    case PayArgsCheckResult.CHECK_RESULT_PAY_INVALID_AMOUNT:
//                                mmm = "参数错误:金额无效";
                        mPayCallBack.onPayFailed("支付失败,参数错误:金额无效");
                        break;
                    case PayArgsCheckResult.CHECK_RESULT_PAY_INVALID_APP_NAME:
//                                mmm = "参数错误:游戏名称无效";
                        mPayCallBack.onPayFailed("支付失败,参数错误:游戏名称无效");
                        break;
                    case PayArgsCheckResult.CHECK_RESULT_PAY_INVALID_ORDER_ID:
//                                mmm = "参数错误:APP_APP_ORDER_ID无效";
                        mPayCallBack.onPayFailed("支付失败,参数错误:APP_APP_ORDER_ID无效");
                        break;
                    case PayArgsCheckResult.CHECK_RESULT_PAY_INVALID_APP_USER_ID:
//                                mmm = "参数错误:APP_USER_ID无效";
                        mPayCallBack.onPayFailed("支付失败,参数错误:APP_USER_ID无效");
                        break;
                    case PayArgsCheckResult.CHECK_RESULT_PAY_INVALID_APP_USER_NAME:
//                                mmm = "参数错误:APP_USER_NAME无效";
                        mPayCallBack.onPayFailed("支付失败,参数错误:APP_USER_NAME无效");
                        break;
                    case PayArgsCheckResult.CHECK_RESULT_PAY_INVALID_NOTIFY_URI:
//                                mmm = "参数错误:NOTIFY_URI无效";
                        mPayCallBack.onPayFailed("支付失败,参数错误:NOTIFY_URI无效");
                        break;
                    case PayArgsCheckResult.CHECK_RESULT_PAY_INVALID_OPEN_UID:
//                                mmm = "参数错误:OPEN_UID无效";
                        mPayCallBack.onPayFailed("支付失败,参数错误:OPEN_UID无效");
                        break;
                    case PayArgsCheckResult.CHECK_RESULT_PAY_INVALID_PRODUCT_ID:
//                                mmm = "参数错误:PRODUCT_ID无效";
                        mPayCallBack.onPayFailed("支付失败,参数错误:PRODUCT_ID无效");
                        break;
                    case PayArgsCheckResult.CHECK_RESULT_PAY_INVALID_PRODUCT_NAME:
//                                mmm = "参数错误:PRODUCT_NAME无效";
                        mPayCallBack.onPayFailed("支付失败,参数错误:PRODUCT_NAME无效");
                        break;
                    case PayArgsCheckResult.CHECK_RESULT_PAY_INVALID_APP_KEY:
//                                mmm = "参数错误:APP_KEY无效";
                        mPayCallBack.onPayFailed("支付失败,参数错误:APP_KEY无效");
                        break;
                    case PaayActivity.PAPAPay_RESULT_CODE_SUCCESS:
//                                mmm = "支付成功";
                        mPayCallBack.onPaySuccess(u9PayParams);
                        break;
                    case PaayActivity.PAPAPay_RESULT_CODE_FAILURE:
//                                mmm = "支付失败";
                        mPayCallBack.onPayFailed("支付失败");
                        break;
                    case PaayActivity.PAPAPay_RESULT_CODE_CANCEL:
//                                mmm = "支付取消";
                        mPayCallBack.onPayCancel();
                        break;
                    case PaayActivity.PAPAPay_RESULT_CODE_WAIT:
//                                mmm = "支付等待";
                        mPayCallBack.onPayFailed("支付失败,支付等待");
                        break;
                }
            }
        });
    }


    @Override
    public void exit(final Activity activity) {
        PPayCenter.loginOut(activity, openUid, new PpaLogoutCallback() {
            @Override
            public void onLoginOut(LogoutResult logoutResult) {
                switch (logoutResult.getCode()) {
                    case LogoutResult.LOGOUT_CODE_OUT:
                        //调用游戏得 具体退出逻辑并完成退出
                        activity.finish();
//                        System.exit(0);
                        break;
                    case LogoutResult.LOGOUT_CODE_BBS:

                        break;
                }
            }
        });
    }

    @Override
    public void onCreate(Activity context) {

    }

    @Override
    public void onStart(Activity context) {

    }

    @Override
    public void onResume(Activity context) {
        PPayCenter.onResume(context);
    }

    @Override
    public void onPause(Activity context) {
        PPayCenter.onPause(context);
    }

    @Override
    public void onStop(Activity context) {

    }

    @Override
    public void onDestroy(Activity context) {
        PPayCenter.destroy();
    }
}
*/
