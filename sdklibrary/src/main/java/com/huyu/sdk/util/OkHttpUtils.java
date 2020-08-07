package com.huyu.sdk.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.huyu.sdk.view.LoadingBar;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by admin on 2019/6/14.
 * <p>
 * 模仿Async-Http通信库写出的okhttp子线程跨主线程通信类
 * <p>
 * 主要参考AsyncHttpClient、AsyncHttpResponseHandler类
 * 参考Retofit2中OkHttpCall类、okhttp3中RealCall类
 * 主要采用了适配器模式、模板模式
 * <p>
 * loadingBar 加载框
 */

public class OkHttpUtils {
    private static final String TAG = "OkHttpUtil";
    private static volatile LoadingBar loadingBar;
    private static volatile boolean isSetLoading = false;

    private static volatile boolean isShowLoading = false;
    private static OkHttpClient client;

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    static {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        client = new OkHttpClient.Builder().writeTimeout(30 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                .connectTimeout(15 * 1000, TimeUnit.MILLISECONDS)
                //设置拦截器，显示日志信息
                .addNetworkInterceptor(httpLoggingInterceptor)
                .build();
    }

    private OkHttpUtils() {
    }

    public static OkHttpClient getClient() {
        return client;
    }

    public static void post(final Context context, String url, String jsonStr, final SimpleResponseHandler responseHandler) {
        isSetLoading = true;
        loadingBar = new LoadingBar(context);
        Logger.d(TAG, "post url:" + url + "\njsonStr:" + jsonStr);
        MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(mediaType, Utils.generatingSign(jsonStr).toString()))
                .build();
//        client.newCall(request).enqueue(responseHandler);
        Call call = client.newCall(request);
        EXECUTOR_SERVICE.execute(new ResponseRunnable(call, responseHandler));
    }

/*  public static void postNoLoading(String url, JSONObject jsonStr, final SimpleResponseHandler responseHandler) {
    isSetLoading = false;
    Logger.d(TAG, "postNoLoading url:" + url + "\njsonStr:" + jsonStr);
//    MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
    MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
    Request request = new Request.Builder()
            .url(url)
            .post(RequestBody.create(mediaType, jsonStr.toString()))
            .build();
//    client.newCall(request).enqueue(responseHandler);
    Call call = client.newCall(request);
    EXECUTOR_SERVICE.execute(new ResponseRunnable(call, responseHandler));
  }*/

    public static void postLoading(Context context, String url, Map<String, String> paramsMap, final SimpleResponseHandler responseHandler) {
        Logger.d("postLoading", "url:" + url + " requestData: " + Arrays.asList(paramsMap).toString());
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : paramsMap.keySet()) {
            //追加表单信息
            builder.add(key, paramsMap.get(key));
        }
        postFormLoading(context, url, builder.build(), responseHandler);
    }

    public static void postNoLoading(String url, Map<String, String> paramsMap, final SimpleResponseHandler responseHandler) {
        Logger.d("postNoLoading", "url:" + url + " requestData: " + Arrays.asList(paramsMap).toString());
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : paramsMap.keySet()) {
            //追加表单信息
            builder.add(key, paramsMap.get(key));
        }
        postFormNoLoading(url, builder.build(), responseHandler);
    }

    public static void postFormLoading(Context context, String url, FormBody formBody, final SimpleResponseHandler responseHandler) {
        isSetLoading = true;
        loadingBar = new LoadingBar(context);
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
//        client.newCall(request).enqueue(responseHandler);
        Call call = client.newCall(request);
        EXECUTOR_SERVICE.execute(new ResponseRunnable(call, responseHandler));
    }

    /**
     * 表单请求参数
     *
     * @param url
     * @param formBody
     * @param responseHandler //创建表单请求参数
     *                        FormBody.Builder builder = new FormBody.Builder();
     *                        builder.add("cityName", cityName);
     *                        FormBody formBody = builder.build();
     */
    public static void postFormNoLoading(String url, FormBody formBody, final SimpleResponseHandler responseHandler) {
        isSetLoading = false;
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
//        client.newCall(request).enqueue(responseHandler);
        Call call = client.newCall(request);
        EXECUTOR_SERVICE.execute(new ResponseRunnable(call, responseHandler));
    }

    public static void get(String url, final SimpleResponseHandler responseHandler) {
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
//    client.newCall(request).enqueue(responseHandler); 异步
        Call call = client.newCall(request);
        EXECUTOR_SERVICE.execute(new ResponseRunnable(call, responseHandler));
    }

    public static void release() {
        if (loadingBar != null && loadingBar.isShowing()) {
            loadingBar.cancel();
        }
        loadingBar = null;
    }

    private static class ResponseRunnable implements Runnable {
        private Call call;
        private SimpleResponseHandler callback;
        private long start, end;

        public ResponseRunnable(Call call, SimpleResponseHandler callback) {
            this.call = call;
            this.callback = callback;
        }

        @Override
        public void run() {
            try {
                start = System.currentTimeMillis();
                callback.sendStartMessage();
                Response response = call.execute();
                end = System.currentTimeMillis();
                Logger.i(TAG, "SimpleResponseHandler request complete time : " + (end - start) + "ms");
                callback.onResponse(call, response);
            } catch (IOException e) {
                callback.onFailure(call, e);
            }
            callback.sendFinishMessage();
        }
    }

    /**
     * 模板模式-----定义算法的步骤，并把这些实现延迟到子类
     */
    public abstract static class SimpleResponseHandler implements Callback {
        private Handler handler;

        public SimpleResponseHandler() {
            Looper looper = Looper.myLooper();
            this.handler = new ResultHandler(this, looper);
        }

        public void handleMessage(Message message) {
//      Log.d(TAG, "SimpleResponseHandler  handleMessage current Thread: " + Thread.currentThread().getName() + ", message.what() == " + message.what);
            switch (message.what) {
                case -1:
                    onStart();
                    break;
                case 0:
                    Object[] objects = (Object[]) message.obj;
                    onSuccess((Call) objects[0], (Response) objects[1]);
                    break;
                case 1:
                    onFailure((Exception) message.obj);
                    break;
                case 2:
                    onFinish();
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            Logger.i(TAG, "SimpleResponseHandler   onResponse current Thread: " + Thread.currentThread().getName() + " , ThreadId : " + Thread.currentThread().getId());
            if (response.code() < 200 || response.code() >= 300) {
                sendFailuerMessage(new IOException(response.message()));
            } else {
                sendSuccessMessage(response.code(), call, response);
            }
        }

        @Override
        public void onFailure(Call call, IOException e) {
            Log.i(TAG, "SimpleResponseHandler   onFailure current Thread: " + Thread.currentThread().getName() + " , ThreadId : " + Thread.currentThread().getId());
            sendFailuerMessage(e);
        }

        void sendStartMessage() {
            this.handler.sendMessage(obtainMessage(-1, null));
        }

        void sendSuccessMessage(int code, Call call, Response response) {
            this.handler.sendMessage(obtainMessage(0, new Object[]{call, response}));
        }

        void sendFailuerMessage(Throwable throwable) {
            this.handler.sendMessage(obtainMessage(1, throwable));
        }

        void sendFinishMessage() {
            this.handler.sendMessage(obtainMessage(2, null));
        }

        Message obtainMessage(int responseMessageId, Object responseMessageData) {
            return Message.obtain(this.handler, responseMessageId, responseMessageData);
        }

        public void onStart() {
            Logger.d(TAG, "SimpleResponseHandler    onStart");
            if (isSetLoading && loadingBar != null && !loadingBar.isShowing()) {
                Logger.d(TAG, "SimpleResponseHandler    loadingBar show");
                loadingBar.show();
                isShowLoading = true;
            }
        }

        public void onFinish() {
            Logger.d(TAG, "SimpleResponseHandler    onFinish");
            if ((isSetLoading && loadingBar != null) || isShowLoading) {
                Logger.e(TAG, "SimpleResponseHandler    loadingBar hide");
                loadingBar.dismiss();
                loadingBar = null;
                isShowLoading = false;
            }
        }

        public abstract void onSuccess(Call call, Response response);

        public abstract void onFailure(Exception error);
    }

    /**
     * 类适配器模式------将一个类的接口，转换为客户期盼的另一个接口。通过继承的方式.
     */
    private static class ResultHandler extends Handler {
        SimpleResponseHandler responseHandler;

        ResultHandler(SimpleResponseHandler handler, Looper looper) {
            super(looper);
            this.responseHandler = handler;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            responseHandler.handleMessage(msg);
        }
    }
}