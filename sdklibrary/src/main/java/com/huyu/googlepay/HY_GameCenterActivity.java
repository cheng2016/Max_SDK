package com.huyu.googlepay;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.huyu.googlepay.util.AppsFlyerActionHelper;
import com.huyu.sdk.HYPlatform;
import com.huyu.sdk.U9Platform;
import com.huyu.sdk.data.Constant;
import com.huyu.sdk.data.ResultCode;
import com.huyu.sdk.data.U9_HttpUrl;
import com.huyu.sdk.data.bean.PayParams;
import com.huyu.sdk.data.config.PhoneInfoHelper;
import com.huyu.sdk.data.config.SharedPreferenceHelper;
import com.huyu.sdk.listener.CallbackListener;
import com.huyu.sdk.util.Logger;
import com.huyu.sdk.util.ToastUtils;


public class HY_GameCenterActivity extends Activity implements OnClickListener {

    private Activity mActivity;
    private ImageView backBtn;

    //支付参数
    private PayParams mPayParams;
    private CallbackListener mPayCallBack;

    private WebView payWebView;
    //    webView 进度条
    private ProgressBar webview_prb;
    private String payUrl;

    private String payOrder;

    private ProgressDialog prd;
    private TextView tvTitle;
    private boolean isBackCenter = false;
    // private boolean isStartCenter = false;
    private boolean isUrlFinish = false;
//	 private HYGame_GetPayResult result;


    // TODO 支付方式开关

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        mActivity = this;
        setContentView(getLayoutId(mActivity,
                "hygame_gamecenter_layout"));
        // isStartCenter = false;
        isBackCenter = false;
        isUrlFinish = false;

        mPayParams = U9Platform.mPayParams;
        mPayCallBack = U9Platform.payCallback;

        initPayUrl();
        initView();
    }

    /**
     * 初始化支付方式
     */
    private void initPayUrl() {
        String getPayInfoStr = getStringId(mActivity, "u9pay_get_pay_info");
       /* prd = HY_ProgressUtil.showByString(mActivity, getPayInfoStr,
                new OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface arg0) {
                        // TODO Auto-generated method stub

                    }
                });*/
    }

    /**
     * 初始化组件
     */
    private void initView() {
        backBtn = (ImageView) findViewById(getId(mActivity,
                "u9pay_pay_back_btn"));

        payWebView = (WebView) mActivity.findViewById(getId(mActivity,
                "u9pay_webview"));
        webview_prb = (ProgressBar) mActivity.findViewById(getId(
                mActivity, "u9pay_webview_prb"));


        tvTitle = (TextView) mActivity.findViewById(getId(mActivity, "hygame_title_text"));
        String payStr = getStringId(mActivity, "u9pay_pay");
        tvTitle.setText(payStr);

        // ---------------------------- 分割线 界面设置----------------------------

        if (mPayParams == null) {
            String no_payparamsMsg = getStringId(mActivity, "hygame_no_pay_params");
            ToastUtils.show(mActivity, no_payparamsMsg);
            this.finish();
            return;
        }

        payUrl = mPayParams.getPayUrl();
        payOrder = payUrl.substring(payUrl.indexOf("=") + 1, payUrl.length());
        if (!TextUtils.isEmpty(PhoneInfoHelper.deviceId)) {
            payUrl = payUrl + "&device=" + PhoneInfoHelper.deviceId;
        }

        payUrl = payUrl + "&guid=" + SharedPreferenceHelper.getChannelUserId();
        payUrl = payUrl + "&token=" + SharedPreferenceHelper.getAccessToken();
        payUrl = payUrl + "&u9uid=" + mPayParams.getU9uid();

        // //渠道code
        payUrl = payUrl + "&channel=" + Constant.CHANNEL_CODE;
        // 渠道id
        payUrl = payUrl + "&sub_channel=" + Constant.CHANNEL_ID;
        // sdk版本控制
        payUrl = payUrl + "&sdk_version=" + Constant.HY_SDK_VERSION_CODE;
        // 计划id
        payUrl = payUrl + "&aid=" + Constant.PLAN_ID;
        Logger.d("channel:" + Constant.CHANNEL_CODE);
        Logger.d("sub_channel:" + Constant.CHANNEL_ID);

        payUrl = payUrl + "&app=" + Constant.APPID + "&app_order_id="
                + mPayParams.getOrderId() + "&amount="
                + mPayParams.getAmount() + "&subject="
                + mPayParams.getProductName();

        if (!TextUtils.isEmpty(mPayParams.getBody())) {
            payUrl = payUrl + "&body=" + mPayParams.getBody();
        } else {
            payUrl = payUrl + "&body=" + mPayParams.getProductName();
        }
        payUrl = payUrl + "&app_ext=" + mPayParams.getAppExtInfo();

        payUrl = payUrl + "&productId=" + mPayParams.getProductId();

        Logger.i("payUrl:" + payUrl);
        Logger.i("payOrder:" + payOrder);

        payWebView.loadUrl(payUrl);
        payWebView.getSettings().setJavaScriptEnabled(true);
        payWebView.getSettings().setDomStorageEnabled(true);

        payWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
//                    HY_ProgressUtil.dismiss(prd);
                    webview_prb.setVisibility(View.GONE);
                } else {
                    webview_prb.setVisibility(View.VISIBLE);
                    webview_prb.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        payWebView.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                // TODO Auto-generated method stub
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        payWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                Logger.d("webView", "请求地址" + url);
                if (url.contains("hygoogleplay")) {
                    //拦截请求路径打开google支付

                    String[] productIdArray = url.split("productId=");
                    if (productIdArray != null && productIdArray.length > 1) {

                        String produceId = productIdArray[1];
                        opendGoogPay(produceId);
                        Logger.d("webView", "拦截请求路径打开google支付" + "produceId=" + productIdArray[1]);
                    }
                    return true;
                }

                Logger.d("webView", "请求地址不拦截");
                return super.shouldOverrideUrlLoading(view, url);
            }

            // 页面加载完成
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // 这个realUrl即为重定向之后的地址
                String realUrl = url;
                Logger.i("realUrl:" + realUrl);
                // if (realUrl.indexOf("http://api.hygame.cc/pay/index") != -1)
                // {
                // isStartCenter = true;
                // }
                if (realUrl.indexOf(U9_HttpUrl.URL_GAMECENTER) != -1) {
                    isUrlFinish = true;
                    if (isBackCenter) {
                        //checkResult();
                    } else {
                        //checkResultNoBack();
                    }
                }
            }
        });


        //payWebView.loadUrl("http://oversea.hyhygame.com/pay.html");
        Logger.d("url:" + payWebView.getUrl());
        // ---------------------------- 分割线 功能设置----------------------------
        backBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        // 按钮控制-3s
        if (!isFastDoubleClick()) {
            if (v == backBtn) {
                back();// 返回按钮
            }
        }
    }

    private long lastClickTime;

    public boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 500) { // 500毫秒内按钮无效，这样可以控制快速点击，自己调整频率
            return true;
        }
        lastClickTime = time;
        return false;
    }

    @Override
    public void onBackPressed() {
        if (payWebView.canGoBack()) {
            payWebView.goBack();
        } else {
            back();
        }
    }

    /**
     * 打开google支付
     *
     * @param produceId
     */
    private void opendGoogPay(String produceId) {
        String googlePublicKey = mPayParams.getGooglePublicKey();
        Logger.d("webView", "打开google支付publicKey>>>" + googlePublicKey);
        googlePublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzPmOXXYMTHFWflHCkUwAEGOdvqkpjngjolR2PdgVMLAPC5w6tU0Quzml72noqTmMa3n+DSbS1wZ+nAjNdxlSF1HID4h155BzkBiRYRFevdAII+uKr9CoI9jBcB9Y+yYPMHAzBvtVJIUa1Ii6+GGfWHcia6HPL0jCuF9WmGvS3BIiNnW2LFuFBhHW0MQxwMFfa8vL7T+S4oJ9RkU/4l1zXx0bajl7jpfdxKN/noiU/U0hBt5hobECAdA83iSLkQvxmuzbu1JpTN5rp+l7o+FX3kQu+gTFKSCQwmp537Q9jtmwstJjqFRowlVh0MM1F3bYufnHbhVqRJtiw2S/OyvXywIDAQAB";
        Logger.d("webView", "打开google支付publicKey>>>" + googlePublicKey);
        GooglePlayPayManager googlePlayPayManager = new GooglePlayPayManager(mActivity);
        googlePlayPayManager.doPay(googlePublicKey, produceId);
    }


    Handler handler = new Handler();

    boolean isCheckSecond = false;

    boolean isClickClose = false;

    int count;

    /**
     * 支付结果检查
     */
    private void checkResult() {
        count++;
        Logger.d("第 " + count + " 次校验");
        HYPlatform.getInstance().checkPayResult(this, payOrder, new CallbackListener() {
            @Override
            public void onResult(ResultCode resultCode, String msg, String data) {
                if (resultCode == ResultCode.SUCCESS) {
                    //appFlyer 购买事件上报
                    if (mPayParams != null) {
                        AppsFlyerActionHelper.buyEvent(mActivity, mPayParams.getAmount() + "", mPayParams.getProductId());
                    }
                    Logger.d("第 " + count + " 次校验 成功");
                    HY_GameCenterActivity.this.finish();
                } else {
                    Logger.d("第 " + count + " 次校验 失败");
                    if (!isCheckSecond) {
                        isCheckSecond = true;
                        handler.postDelayed(checkResultRunnable, 3000);
                    }
                }
                if (isClickClose) {
                    HY_GameCenterActivity.this.finish();
                }
            }
        });
    }

    Dialog dialog;

    /**
     * 退出支付
     */
    private void back() {
        if (dialog == null) {
            Builder builder = new Builder(HY_GameCenterActivity.this);
            String exitPayHint = getStringId(mActivity, "u9pay_exit_pay_hint");
            String confirmStr = getStringId(mActivity, "u9pay_confirm_btn");
            String cancelStr = getStringId(mActivity, "u9pay_cannel_btn");
            builder.setTitle(exitPayHint);
            builder.setNegativeButton(confirmStr, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    isClickClose = true;
                    // 检查支付结果
                    checkResult();
                    U9Platform.payCallback.onResult(ResultCode.CANCEL, "支付取消", "");
                }
            });
            builder.setPositiveButton(cancelStr, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Logger.d("继续支付");
                }
            });
            dialog = builder.create();
        }
        dialog.show();
        //finish();
    }

    CheckResultRunnable checkResultRunnable = new CheckResultRunnable();

    private final class CheckResultRunnable implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            checkResult();
        }
    }


    @Override
    public void onRestart() {
        super.onRestart();
        Logger.d("onRestart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.d("onResume");
        isBackCenter = true;
        // 根据回到Activity的状态,判断订单是否完成
        if (isUrlFinish) {
            checkResult();
            return;
        }
        checkResult();
    }

    @Override
    public void onPause() {
        super.onPause();
        Logger.d("onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Logger.d("onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.d("onDestroy");
        handler.removeCallbacks(checkResultRunnable);
//        HYPlatform.getInstance().onDestrery();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.d("onActivityResult");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Logger.d("onConfigurationChanged");
    }

    public static String getStringId(Context paramActivity, String id) {
        String packageName = paramActivity.getPackageName();
        return paramActivity.getResources().getString(
                paramActivity.getResources().getIdentifier(id, "string",
                        packageName));
    }

    public static int getId(Context paramActivity, String id) {
        String packageName = paramActivity.getPackageName();
        return paramActivity.getResources()
                .getIdentifier(id, "id", packageName);
    }

    public static int getLayoutId(Context paramActivity, String id) {
        String packageName = paramActivity.getPackageName();
        return paramActivity.getResources().getIdentifier(id, "layout",
                packageName);
    }
}
