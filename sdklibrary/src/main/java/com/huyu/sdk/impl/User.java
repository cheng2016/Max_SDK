package com.huyu.sdk.impl;

import android.app.Activity;
import android.content.Context;

import com.huyu.googlepay.util.AppsFlyerActionHelper;
import com.huyu.sdk.U9Platform;
import com.huyu.sdk.data.HttpUrl;
import com.huyu.sdk.data.ResultCode;
import com.huyu.sdk.data.U9_HttpUrl;
import com.huyu.sdk.data.bean.HYUser;
import com.huyu.sdk.data.config.SharedPreferenceHelper;
import com.huyu.sdk.listener.CallbackListener;
import com.huyu.sdk.listener.LoginCallBackListener;
import com.huyu.sdk.util.HY_Log_TimeUtils;
import com.huyu.sdk.util.Logger;
import com.huyu.sdk.util.OkHttpUtils;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class User implements IUser {
    public static final String TAG = User.class.getSimpleName();

    public static final int ACCOUNT_LOGIN = 0;

    public static final int ONEKEY_LOGIN = 1;

    private static User instance;

    public static User getInstance() {
        if (instance == null)
            instance = new User();
        return instance;
    }

    //游客登录
    @Override
    public void guestLogin(final Context context, Map map, final CallbackListener listener) {
        OkHttpUtils.postNoLoading(U9_HttpUrl.URL_ONEKEYREGISTER, map, new OkHttpUtils.SimpleResponseHandler() {
            public void onSuccess(Call param1Call, Response param1Response) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(param1Response.body().string()));
                    int status = jsonObject.optInt("status");
                    String message = jsonObject.optString("message");
                    if (status == 0) {
                        JSONObject data = jsonObject.optJSONObject("data");

//                        UserInfoHelper.USER_ID = data.optString("uid");
//                        UserInfoHelper.CHANNEL_USER_ID = data.optString("guid");
//                        UserInfoHelper.CHANNEL_USER_NAME = data.optString("username");
//                        UserInfoHelper.ACCESS_TOKEN = data.optString("token");

                        SharedPreferenceHelper.setHyUserId(data.optString("uid"));
                        SharedPreferenceHelper.setChannelUserId(data.optString("guid"));
                        SharedPreferenceHelper.setChannelUserName(data.optString("username"));
                        SharedPreferenceHelper.setAccessToken(data.optString("token"));
                        SharedPreferenceHelper.setUserPassword(data.optString("password"));
                        SharedPreferenceHelper.setLoginType(ONEKEY_LOGIN);

                        if (data.optInt("is_create") == 1) {
                            AppsFlyerActionHelper.registerEvent((Activity) context);
                        }

                        U9Platform.getInstance().verifylogin(context, listener);
                    } else {
                        listener.onResult(ResultCode.Fail, message, "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(Exception param1Exception) {
                listener.onResult(ResultCode.Fail, "guestLogin onFailure", param1Exception.toString());
            }
        });
    }

    //自动登录
    @Override
    public void autoLogin(final Context context, Map<String, String> map, final CallbackListener listener) {
        OkHttpUtils.postNoLoading(U9_HttpUrl.URL_CHECK_TOKEN, map, new OkHttpUtils.SimpleResponseHandler() {
            public void onFailure(Exception param1Exception) {
                listener.onResult(ResultCode.Fail, "autoLogin onFailure", param1Exception.toString());
            }

            public void onSuccess(Call param1Call, Response param1Response) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(param1Response.body().string()));
                    int status = jsonObject.optInt("status");
                    String message = jsonObject.optString("message");
                    if (status == 0) {
                        U9Platform.getInstance().verifylogin(context, listener);
                    } else {
                        SharedPreferenceHelper.setAccessToken("");
                        listener.onResult(ResultCode.Fail, message, "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void accountlogin(final Context context, final Map<String, String> map, final LoginCallBackListener listener) {
        OkHttpUtils.postNoLoading(U9_HttpUrl.URL_LOGIN, map, new OkHttpUtils.SimpleResponseHandler() {
            public void onFailure(Exception param1Exception) {
                listener.onLoginFailed("accountlogin onFailure" + param1Exception.toString());
            }

            public void onSuccess(Call param1Call, Response param1Response) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(param1Response.body().string()));
                    int status = jsonObject.optInt("status");
                    final String message = jsonObject.optString("message");
                    if (status == 0) {
                        JSONObject data = jsonObject.optJSONObject("data");

                        if (data.optInt("is_create") == 1) {
                            AppsFlyerActionHelper.registerEvent((Activity) context);
                        }

                        final HYUser user = new HYUser();
                        SharedPreferenceHelper.setHyUserId(data.optString("uid"));
                        SharedPreferenceHelper.setChannelUserId(data.optString("guid"));
                        SharedPreferenceHelper.setChannelUserName(data.optString("username"));
                        SharedPreferenceHelper.setAccessToken(data.optString("token"));
                        SharedPreferenceHelper.setUserPassword(map.get("password"));
                        SharedPreferenceHelper.setLoginType(ACCOUNT_LOGIN);

                        user.hyuid = data.optString("uid");
                        user.channelUserId = data.optString("guid");
                        user.channelUserName = data.optString("username");
                        user.token = data.optString("token");
                        U9Platform.getInstance().verifylogin(context, new CallbackListener() {
                            @Override
                            public void onResult(ResultCode resultCode, String msg, String data) {
                                if (resultCode == ResultCode.SUCCESS) {
                                    user.userId = SharedPreferenceHelper.getUserId();
                                    user.is_bind_account = SharedPreferenceHelper.getIsBindAccount();
                                    listener.onLoginSuccess(user);
                                } else {
                                    listener.onLoginFailed(message);
                                }
                            }
                        });
                    } else {
                        listener.onLoginFailed(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void verifylogin(Context context, Map<String, String> map, final CallbackListener listener) {
        OkHttpUtils.postLoading(context, HttpUrl.URL_LOGIN, map, new OkHttpUtils.SimpleResponseHandler() {
            public void onFailure(Exception param1Exception) {
                listener.onResult(ResultCode.Fail, "verifylogin onFailure", param1Exception.toString());
            }

            public void onSuccess(Call param1Call, Response param1Response) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(param1Response.body().string()));
                    int status = jsonObject.optInt("Code");
                    String message = jsonObject.optString("Message");
                    if (status == 0) {

                        SharedPreferenceHelper.setUserId(jsonObject.optString("UserId"));
                        SharedPreferenceHelper.setIsBindAccount(jsonObject.optInt("is_visitor"));

                        listener.onResult(ResultCode.SUCCESS, message, "");

                        HY_Log_TimeUtils.setLoginSuccess();
                        U9Platform.getInstance().logReport("4", "登录成功", HY_Log_TimeUtils.loginSuccess - HY_Log_TimeUtils.startLogin + "");
                    } else {
                        listener.onResult(ResultCode.Fail, message, "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void switchLogin(Map<String, String> map, CallbackListener listener) {

    }

    @Override
    public void roleReport(Context context, Map<String, String> map, final CallbackListener listener) {
        OkHttpUtils.postLoading(context, HttpUrl.URL_ROLE_REPORT, map, new OkHttpUtils.SimpleResponseHandler() {
            public void onSuccess(Call param1Call, Response param1Response) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(param1Response.body().string()));
                    int status = jsonObject.optInt("Code");
                    String message = jsonObject.optString("Message");
                    if (status == 0) {
                        listener.onResult(ResultCode.SUCCESS, message, "");
                        Logger.i(TAG, "roleReport 上传角色成功");
                    } else {
                        listener.onResult(ResultCode.Fail, message, "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(Exception param1Exception) {
                listener.onResult(ResultCode.Fail, "roleReport onFailure", param1Exception.toString());
            }
        });
    }

    public void bindAccount(Context context, Map<String, String> map, final CallbackListener listener) {
        OkHttpUtils.postLoading(context, HttpUrl.URL_BIND_ACCOUNT, map, new OkHttpUtils.SimpleResponseHandler() {
            public void onSuccess(Call param1Call, Response param1Response) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(param1Response.body().string()));
                    int status = jsonObject.optInt("status");
                    String message = jsonObject.optString("message");
                    if (status == 0) {
                        SharedPreferenceHelper.setIsBindAccount(1);
//                        SharedPreferenceHelper.setIsBindAccount(jsonObject.optJSONObject("data").optInt("is_visitor"));
                        listener.onResult(ResultCode.SUCCESS, message, "");
                        Logger.i(TAG, "bindAccount  成功");
                    } else {
                        listener.onResult(ResultCode.Fail, message, "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(Exception param1Exception) {
                listener.onResult(ResultCode.Fail, "bindAccount onFailure", param1Exception.toString());
            }
        });
    }

    @Override
    public void submitExtraData(UserExtraData paramUserExtraData, CallbackListener listener) {

    }

    @Override
    public void logout(final Context context, Map<String, String> map, final CallbackListener listener) {
        OkHttpUtils.postNoLoading(U9_HttpUrl.URL_LOGOUT, map, new OkHttpUtils.SimpleResponseHandler() {
            public void onSuccess(Call param1Call, Response param1Response) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(param1Response.body().string()));
                    int status = jsonObject.optInt("status");
                    String message = jsonObject.optString("message");
                    if (status == 0) {

                        SharedPreferenceHelper.setUserId("");
                        SharedPreferenceHelper.setHyUserId("");
                        SharedPreferenceHelper.setChannelUserId("");
//                        SharedPreferenceHelper.setChannelUserName("");
                        SharedPreferenceHelper.setAccessToken("");
                        SharedPreferenceHelper.setIsBindAccount(0);

                        listener.onResult(ResultCode.SUCCESS, message, "");
                    } else {
                        listener.onResult(ResultCode.Fail, message, "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(Exception param1Exception) {
                listener.onResult(ResultCode.Fail, "logout onFailure", param1Exception.toString());
            }
        });
    }
}
