package com.huyu.game;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.text.TextUtils;

import com.facebook.FacebookSdk;
import com.huyu.googlepay.GooglePlayPayManager;
import com.huyu.googlepay.HY_GameCenterActivity;
import com.huyu.googlepay.util.AppsFlyerActionHelper;
import com.huyu.googlepay.util.IabHelper;
import com.huyu.sdk.HYPlatform;
import com.huyu.sdk.U9Platform;
import com.huyu.sdk.base.BaseChannel;
import com.huyu.sdk.data.ResultCode;
import com.huyu.sdk.data.bean.GameRoleInfo;
import com.huyu.sdk.data.bean.HYUser;
import com.huyu.sdk.data.bean.PayParams;
import com.huyu.sdk.data.config.SharedPreferenceHelper;
import com.huyu.sdk.listener.CallbackListener;
import com.huyu.sdk.listener.LoginCallBackListener;
import com.huyu.sdk.listener.PayCallbackListener;
import com.huyu.sdk.util.Logger;
import com.huyu.sdk.util.ResourceHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 文件名：DefaultChannel
 * 创建日期：2020/8/4 11:13
 * 描述：TODO
 */
public class googlePlay_Channel extends BaseChannel {
    public static final String TAG = googlePlay_Channel.class.getSimpleName();

//    public static googlePlay_Channel instance;

/*    public static googlePlay_Channel getInstance() {
        if (instance == null) {
            instance = new googlePlay_Channel();
        }
        return instance;
    }*/

    @Override
    public void initApplication(Application atc) {
        //初始化AppsFlyer 数据来源统计
        AppsFlyerActionHelper.initAppsFlyerSdk(atc);
        //fackBook sdk  初始化
        FacebookSdk.sdkInitialize(atc, new FacebookSdk.InitializeCallback() {
            @Override
            public void onInitialized() {

            }
        });
    }

    @Override
    public void initActivity(Activity activity, CallbackListener listener) {
        Logger.d(TAG, "  init success");
        listener.onResult(ResultCode.SUCCESS, TAG + " init success", null);
        HYPlatform.getInstance().init(activity, new HYPlatform.SDKListener() {
            @Override
            public void onSwitchAccount(HYUser user) {
                Logger.d(TAG, "onSwitchAccount");
                mHYSDKListener.onSwitchAccount(user);
//                //cp更新数据，并重新跳转到登录界面进行登录操作
            }
        });
    }

    @Override
    public void login(final Activity activity, final LoginCallBackListener listener) {
        Logger.d(TAG, "  login  ");
        HYPlatform.getInstance().login(activity, new CallbackListener() {
            @Override
            public void onResult(ResultCode resultCode, String msg, String data) {
                if (resultCode == ResultCode.SUCCESS) {
                    final HYUser user = new HYUser();
                    user.token = SharedPreferenceHelper.getAccessToken();
                    user.hyuid = SharedPreferenceHelper.getHyUserId();
                    user.userId = SharedPreferenceHelper.getUserId();
                    user.channelUserId = SharedPreferenceHelper.getChannelUserId();
                    user.channelUserName = SharedPreferenceHelper.getChannelUserName();
                    user.info = "default_info";
                    listener.onLoginSuccess(user);
                    //登录上报
                    AppsFlyerActionHelper.loginEvent(activity);
                } else if (resultCode == ResultCode.Fail) {
                    listener.onLoginFailed(msg);
                } else if (resultCode == ResultCode.CANCEL) {
                    listener.onLoginCancel();
                }
            }
        });
    }


    public void logout(Activity activity) {
        Logger.d(TAG, "  logout  ");
        HYPlatform.getInstance().logout(activity, new CallbackListener() {
            @Override
            public void onResult(ResultCode resultCode, String msg, String data) {
                if (resultCode == ResultCode.SUCCESS) {
                    mHYSDKListener.onLogout();
                }
            }
        });
    }

    @Override
    public void switchAccount(Activity activity) {

    }

    @Override
    public void showAccountCenter(Activity activity) {
        Logger.d(TAG, "  showAccountCenter  ");
        HYPlatform.getInstance().showAccountCenter(activity);
    }

    @Override
    public void roleReport(Activity activity, GameRoleInfo gameRoleInfo, final CallbackListener listener) {
        Logger.d(TAG, "  roleReport  ");
        switch (gameRoleInfo.typeId) {
            case 1:
                //选择服务器
                //创建角色
                AppsFlyerActionHelper.roleEvent(activity,AppsFlyerActionHelper.CREATE_ROLE);
                Logger.d(TAG, "上传af事件类型》》创建角色事件");
                break;
            case 2:
                //升级
                break;
            case 3:
                //完成新手指引
                AppsFlyerActionHelper.roleEvent(activity,AppsFlyerActionHelper.ROLE_GUIDEEND);
                Logger.d(TAG, "上传af事件类型》》完成新手引导");
                break;
            case 4:
                //等级提升
                break;
            case 5:
                //退出游戏
                break;
        }
    }

    @Override
    public void pay(final Activity activity, final PayParams payParams, final PayCallbackListener payCallbackListener) {
        Logger.d(TAG, " pay  ");
        HYPlatform.getInstance().checkPaymentMethod(activity, new CallbackListener() {
            @Override
            public void onResult(ResultCode resultCode, String msg, String data) {
                if (resultCode == ResultCode.SUCCESS) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(data);
                        String url = jsonObject.optString("url");
                        String order_id = jsonObject.optString("order_id");
                        payParams.setPayChannel("GOOGLEPAY");
                        payParams.setOrderId(order_id);
                        payParams.setPayUrl(url);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    choosePay(activity, payParams, payCallbackListener);
                } else {
                    googlePay(activity, payParams, payCallbackListener);
                }
            }
        });
    }


    public void choosePay(final Activity activity, final PayParams payParams, final PayCallbackListener payCallbackListener) {
        HYPlatform.getInstance().startPay(activity, payParams, new CallbackListener() {
            @Override
            public void onResult(ResultCode resultCode, String msg, String data) {
                if (resultCode == ResultCode.SUCCESS) {
                    payParams.setOrderId(msg);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(data);
                        String public_key = jsonObject.optString("public_key");
                        payParams.setGooglePublicKey(public_key);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startChoosePayActivity(activity, payParams, new CallbackListener() {
                        @Override
                        public void onResult(ResultCode resultCode, String msg, String data) {
                            if (resultCode == ResultCode.SUCCESS) {
                                payCallbackListener.onPaySuccess(payParams);
                            } else if (resultCode == ResultCode.Fail) {
                                payCallbackListener.onPayFailed(msg);
                            } else if (resultCode == ResultCode.CANCEL) {
                                payCallbackListener.onPayCancel();
                            }
                        }
                    });
                } else {
                    payCallbackListener.onPayFailed(msg);
                }
            }
        });
    }

    public void startChoosePayActivity(Context context, PayParams params, CallbackListener payListener) {
        Logger.d(TAG, "startGooglePayActivity");
        params.setU9uid(SharedPreferenceHelper.getChannelUserId());
        U9Platform.payCallback = payListener;
        U9Platform.mPayParams = params;
        Intent intent = new Intent();
        intent.setClass(context, HY_GameCenterActivity.class);
        context.startActivity(intent);
    }


    private void googlePay(final Activity activity, final PayParams payParams, final PayCallbackListener payCallbackListener) {
        payParams.setPayChannel("GOOGLEPAY");
        HYPlatform.getInstance().startPay(activity, payParams, new CallbackListener() {
            @Override
            public void onResult(ResultCode resultCode, String msg, String data) {
                if (resultCode == ResultCode.SUCCESS) {
                    payParams.setOrderId(msg);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(data);
                        String public_key = jsonObject.optString("public_key");
                        payParams.setGooglePublicKey(public_key);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    openGooglePay(activity, payParams, payCallbackListener);
                } else {
                    payCallbackListener.onPayFailed(msg);
                }
            }
        });
    }

    GooglePlayPayManager mGooglePlayPayManager;

    private void openGooglePay(Activity activity, final PayParams payParams, final PayCallbackListener payCallbackListener) {
        String googlePublicKey = payParams.getGooglePublicKey();
        Logger.d("webView", "打开google支付publicKey>>>" + googlePublicKey);
        if (TextUtils.isEmpty(googlePublicKey)) {
            googlePublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzPmOXXYMTHFWflHCkUwAEGOdvqkpjngjolR2PdgVMLAPC5w6tU0Quzml72noqTmMa3n+DSbS1wZ+nAjNdxlSF1HID4h155BzkBiRYRFevdAII+uKr9CoI9jBcB9Y+yYPMHAzBvtVJIUa1Ii6+GGfWHcia6HPL0jCuF9WmGvS3BIiNnW2LFuFBhHW0MQxwMFfa8vL7T+S4oJ9RkU/4l1zXx0bajl7jpfdxKN/noiU/U0hBt5hobECAdA83iSLkQvxmuzbu1JpTN5rp+l7o+FX3kQu+gTFKSCQwmp537Q9jtmwstJjqFRowlVh0MM1F3bYufnHbhVqRJtiw2S/OyvXywIDAQAB";
            Logger.d("webView", "打开google支付publicKey>>>" + googlePublicKey);
        }
        mGooglePlayPayManager = new GooglePlayPayManager(activity,true);
        U9Platform.mPayParams = payParams;
        mGooglePlayPayManager.doPay(googlePublicKey, payParams.getProductId());
        mGooglePlayPayManager.payCallback = new CallbackListener() {
            @Override
            public void onResult(ResultCode resultCode, String msg, String data) {
                if (resultCode == ResultCode.SUCCESS) {
                    payCallbackListener.onPaySuccess(payParams);
                } else if (resultCode == ResultCode.Fail) {
                    payCallbackListener.onPayFailed(msg);
                }
            }
        };
    }

    private void checkPayResult(Activity activity, final PayParams payParams, final PayCallbackListener listener) {
        HYPlatform.getInstance().checkPayResult(activity, payParams.getOrderId(), new CallbackListener() {
            @Override
            public void onResult(ResultCode resultCode, String msg, String data) {
                if (resultCode == ResultCode.SUCCESS) {
                    listener.onPaySuccess(payParams);
                } else if (resultCode == ResultCode.Fail) {
                    listener.onPayFailed(msg);
                }
            }
        });
    }

    @Override
    public void applicationDestroy(Activity activity) {

    }

    @Override
    public void exit(final Activity activity) {
        Logger.d(TAG, " exit  ");
        new AlertDialog.Builder(activity)
                .setTitle(ResourceHelper.getStringId(activity, "u9pay_exit_game_hint"))
                .setNegativeButton(ResourceHelper.getStringId(activity, "u9pay_confirm_btn"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        activity.finish();
                        System.exit(0);
                    }
                }).setPositiveButton(ResourceHelper.getStringId(activity, "u9pay_cannel_btn"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create().show();
    }

    @Override
    public void onCreate(Activity context) {
        Logger.d(TAG, " onCreate");
    }

    @Override
    public void onNewIntent(Intent intent) {
        Logger.d(TAG, " onNewIntent");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.d(TAG, " onActivityResult requestCode = " + requestCode + " , resultCode = " + resultCode);
        if(mGooglePlayPayManager == null){
            return;
        }
        IabHelper mHelper = mGooglePlayPayManager.getmHelper();

        if (mHelper == null) return;
        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            onActivityResult(requestCode, resultCode, data);
        }
        else {
            Logger.i(TAG, "onActivityResult handled by IABUtil.");
        }
        Logger.d(TAG, "支付返回到这里");
    }

    @Override
    public void onStart(Activity context) {
        Logger.d(TAG, " onStart");
    }

    @Override
    public void onResume(Activity context) {
        Logger.d(TAG, " onResume");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Logger.d(TAG, " onConfigurationChanged");
    }

    @Override
    public void onPause(Activity context) {
        Logger.d(TAG, " onPause");
    }

    @Override
    public void onStop(Activity context) {
        Logger.d(TAG, " onStop");
    }

    @Override
    public void onDestroy(Activity context) {
        Logger.d(TAG, " onDestroy");
        if (mGooglePlayPayManager != null){
            mGooglePlayPayManager.release();
        }
    }
}
