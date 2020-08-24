package com.huyu.sdk.impl;

import android.content.Context;

import com.huyu.sdk.data.HttpUrl;
import com.huyu.sdk.data.ResultCode;
import com.huyu.sdk.data.U9_HttpUrl;
import com.huyu.sdk.listener.CallbackListener;
import com.huyu.sdk.util.OkHttpUtils;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class Pay implements IPay {
    public static final String TAG = Pay.class.getSimpleName();
    private static Pay instance;


    public static Pay getInstance() {
        if (instance == null)
            instance = new Pay();
        return instance;
    }

    public void checkPaymentMethod(Context context, Map<String, String> map, final CallbackListener listener) {
        OkHttpUtils.postNoLoading(HttpUrl.URL_CHECK_USER, map, new OkHttpUtils.SimpleResponseHandler() {
            public void onFailure(Exception param1Exception) {
                listener.onResult(ResultCode.Fail, "createPaymentOrder onFailure", param1Exception.toString());
            }

            public void onSuccess(Call param1Call, Response param1Response) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(param1Response.body().string()));
                    int status = jsonObject.optInt("status");
                    String message = jsonObject.optString("message");
                    if (status == 0) {
                        listener.onResult(ResultCode.SUCCESS, message, jsonObject.optString("data"));
                    } else {
                        listener.onResult(ResultCode.Fail, message, "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void startPay(Context context, Map<String, String> map, final CallbackListener listener) {
        OkHttpUtils.postLoading(context, HttpUrl.URL_PAY, map, new OkHttpUtils.SimpleResponseHandler() {
            public void onFailure(Exception param1Exception) {
                listener.onResult(ResultCode.Fail, "startPay onFailure", param1Exception.toString());
            }

            public void onSuccess(Call param1Call, Response param1Response) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(param1Response.body().string()));
                    int status = jsonObject.optInt("Code");
                    String message = jsonObject.optString("Message");
                    if (status == 0) {
                        String OrderId = jsonObject.optString("OrderId");
                        listener.onResult(ResultCode.SUCCESS, OrderId, jsonObject.optJSONObject("Ext").optString("data"));
                    } else {
                        listener.onResult(ResultCode.Fail, message, "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void checkPayResult(Context context, Map<String, String> map, final CallbackListener listener) {
        OkHttpUtils.postNoLoading(U9_HttpUrl.URL_CHECKPAY, map, new OkHttpUtils.SimpleResponseHandler() {
            public void onFailure(Exception param1Exception) {
                listener.onResult(ResultCode.Fail, "checkPayResult onFailure", param1Exception.toString());
            }

            public void onSuccess(Call param1Call, Response response) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(response.body().string()));
                    int status = jsonObject.optInt("status");
                    String message = jsonObject.optString("message");
                    if (status == 0) {
                        listener.onResult(ResultCode.SUCCESS, message, "");
                    } else {
                        listener.onResult(ResultCode.Fail, message, "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void orderServerVerify(Context context,String url , Map<String, String> map, final CallbackListener listener) {
        OkHttpUtils.postNoLoading(url, map, new OkHttpUtils.SimpleResponseHandler() {
            @Override
            public void onSuccess(Call call, Response response) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(response.body().string()));
                    int status = jsonObject.optInt("status");
                    String message = jsonObject.optString("message");
                    if (status == 0) {
                        listener.onResult(ResultCode.SUCCESS, message, jsonObject.optString("data"));
                    } else {
                        listener.onResult(ResultCode.Fail, message, "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Exception error) {
                listener.onResult(ResultCode.Fail, "orderServerVerify onFailure", error.toString());
            }
        });
    }
}
