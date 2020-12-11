package com.huyu.googlepay;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
import android.widget.Toast;

import com.huyu.game.googlePlay_Channel;
import com.huyu.googlepay.util.AppsFlyerActionHelper;
import com.huyu.sdk.HYPlatform;
import com.huyu.sdk.data.Constant;
import com.huyu.sdk.data.ResultCode;
import com.huyu.sdk.data.U9_HttpUrl;
import com.huyu.sdk.data.bean.PayParams;
import com.huyu.sdk.data.config.PhoneInfoHelper;
import com.huyu.sdk.data.config.SharedPreferenceHelper;
import com.huyu.sdk.listener.CallbackListener;
import com.huyu.sdk.util.HY_Utils;
import com.huyu.sdk.util.Logger;
import com.huyu.sdk.util.ResourceHelper;
import com.huyu.sdk.util.ToastUtils;

import java.util.List;


public class HY_GameCenterActivity extends Activity implements OnClickListener {
    public static final String TAG = HY_GameCenterActivity.class.getSimpleName();
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

    private final int RESULT_CODE = 10086;
    private final String GOOGLE_PLAY = "com.android.vending";

    // TODO 支付方式开关

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        mActivity = this;
        setContentView(ResourceHelper.getLayoutId(mActivity,
                "hygame_gamecenter_layout"));
        // isStartCenter = false;
        isBackCenter = false;
        isUrlFinish = false;

        mPayParams = googlePlay_Channel.mPayParams;
        mPayCallBack = googlePlay_Channel.mPayCallback;

        initPayUrl();
        initView();
    }

    /**
     * 初始化支付方式
     */
    private void initPayUrl() {
        String getPayInfoStr = ResourceHelper.getStringId(mActivity, "u9pay_get_pay_info");
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
        backBtn = (ImageView) findViewById(ResourceHelper.getId(mActivity,
                "u9pay_pay_back_btn"));

        payWebView = (WebView) mActivity.findViewById(ResourceHelper.getId(mActivity,
                "u9pay_webview"));
        webview_prb = (ProgressBar) mActivity.findViewById(ResourceHelper.getId(
                mActivity, "u9pay_webview_prb"));


        tvTitle = (TextView) mActivity.findViewById(ResourceHelper.getId(mActivity, "hygame_title_text"));
        String payStr = ResourceHelper.getStringId(mActivity, "u9pay_pay");
        tvTitle.setText(payStr);

        // ---------------------------- 分割线 界面设置----------------------------

        if (mPayParams == null) {
            String no_payparamsMsg = ResourceHelper.getStringId(mActivity, "u9pay_no_pay_params");
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

                Logger.d(TAG, "webView 请求地址 : " + url);
                if (url.contains("hygoogleplay")) {
                    //拦截请求路径打开google支付
                    String[] productIdArray = url.split("productId=");

                    if (!mPayParams.getGooglePublicKey().equals("")) {
                        Logger.d(TAG, "商品id : " + mPayParams.getProductId() + "==" + mPayParams.getAmount());

                        Logger.d(TAG, "商品public key : " + mPayParams.getGooglePublicKey());

                        Logger.d(TAG, "商品gpublic key : " + mPayParams.getPGooglePublicKey());

                        Logger.d(TAG, "商品的插件PackageName ： " + mPayParams.getPackageName());

                        String goUrl = "scheme://" + (TextUtils.isEmpty(mPayParams.getPackageName()) ? "com.cedsdes.migecs.elgtsgs" : mPayParams.getPackageName());
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(goUrl));
                        intent.putExtra("id", mPayParams.getProductId());
                        intent.putExtra("order", mPayParams.getGameOrderId());
                        intent.putExtra("url", HY_Utils.getPayCallbackUrl(mActivity));
                        intent.putExtra("amount", mPayParams.getAmount() + "");
                        intent.putExtra("key", mPayParams.getPGooglePublicKey());
                        try {
                            startActivityForResult(intent, RESULT_CODE);
                        } catch (Exception e) {
                            goThirdPay(HY_GameCenterActivity.this);
                        }
                    } else if (productIdArray != null && productIdArray.length > 1) {
                        String produceId = productIdArray[1];
                        opendGoogPay(produceId);
                        Logger.d(TAG, "webView 拦截请求路径打开google支付" + "produceId=" + productIdArray[1]);
                    }
                    return true;
                }else if(url.contains("googleinappbilling")){
                    String[]  productIdArray =  url.split("productId=");
                    String produceId = productIdArray[1];
                    opendGoogPay(produceId);
                    return true;
                }

                Logger.d(TAG, "webVie 请求地址不拦截");
                return super.shouldOverrideUrlLoading(view, url);
            }

            // 页面加载完成
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // 这个realUrl即为重定向之后的地址
                String realUrl = url;
                Logger.i(TAG, "realUrl:" + realUrl);
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
        Logger.d(TAG, "url:" + payWebView.getUrl());
        // ---------------------------- 分割线 功能设置----------------------------
        backBtn.setOnClickListener(this);
    }

    /**
     * 判断是否存在google play
     * yse  存在则跳转对应详情页
     * no  不存在  --  判断是否存在浏览器 -- 存在则跳转浏览器  不存在则显示报错
     *
     * @param context
     */
    public void goThirdPay(final Context context) {
        String packageName = (TextUtils.isEmpty(mPayParams.getPackageName()) ? "com.cedsdes.migecs.elgtsgs" : mPayParams.getPackageName());
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + packageName));
            intent.setPackage(GOOGLE_PLAY);//这里对应的是谷歌商店，跳转别的商店改成对应的即可
            if (intent.resolveActivity(context.getPackageManager()) != null && isGooglePlayAvilible(context)) {
                Logger.d(TAG,"--------------------这里对应的是谷歌商店，跳转别的商店改成对应的即可");
                context.startActivity(intent);
            } else {//没有应用市场，通过浏览器跳转到Google Play
                Logger.d(TAG,"-------------------------没有应用市场，通过浏览器跳转到Google Play");
                Intent intent2 = new Intent(Intent.ACTION_VIEW);
                intent2.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + packageName));
                if (intent2.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent2);
                } else {
                    //没有Google Play 也没有浏览器
                    Toast.makeText(context, "sorry,there isn't google and browser", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (ActivityNotFoundException activityNotFoundException1) {
            Toast.makeText(context, "sorry,there isn't google store", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 检测是否安装GooglePlay商店
     * @param context
     * @return
     */
    public static boolean isGooglePlayAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.android.vending")) {
                    return true;
                }
            }
        }
        return false;
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
        Logger.d(TAG, "webView 打开google支付publicKey>>>" + googlePublicKey);
        if (TextUtils.isEmpty(googlePublicKey)) {
            googlePublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzPmOXXYMTHFWflHCkUwAEGOdvqkpjngjolR2PdgVMLAPC5w6tU0Quzml72noqTmMa3n+DSbS1wZ+nAjNdxlSF1HID4h155BzkBiRYRFevdAII+uKr9CoI9jBcB9Y+yYPMHAzBvtVJIUa1Ii6+GGfWHcia6HPL0jCuF9WmGvS3BIiNnW2LFuFBhHW0MQxwMFfa8vL7T+S4oJ9RkU/4l1zXx0bajl7jpfdxKN/noiU/U0hBt5hobECAdA83iSLkQvxmuzbu1JpTN5rp+l7o+FX3kQu+gTFKSCQwmp537Q9jtmwstJjqFRowlVh0MM1F3bYufnHbhVqRJtiw2S/OyvXywIDAQAB";
            Logger.d(TAG, "webView 打开默认google支付publicKey>>>" + googlePublicKey);
        }
        googlePlay_Channel.mGooglePlayPayManager = new GooglePlayPayManager(mActivity).doPay(googlePublicKey, produceId);
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
        Logger.d(TAG, "第 " + count + " 次校验");
        HYPlatform.getInstance().checkPayResult(this, payOrder, new CallbackListener() {
            @Override
            public void onResult(ResultCode resultCode, String msg, String data) {
                if (resultCode == ResultCode.SUCCESS) {
                    //appFlyer 购买事件上报
                    if (mPayParams != null) {
                        AppsFlyerActionHelper.buyEvent(mActivity, mPayParams.getAmount() + "", mPayParams.getProductId());
                    }
                    Logger.d(TAG, "第 " + count + " 次校验 成功");
                    HY_GameCenterActivity.this.finish();
                } else {
                    Logger.d(TAG, "第 " + count + " 次校验 失败 isCheckSecond ： " + isCheckSecond);
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
            String exitPayHint = ResourceHelper.getStringId(mActivity, "u9pay_exit_pay_hint");
            String confirmStr = ResourceHelper.getStringId(mActivity, "u9pay_confirm_btn");
            String cancelStr = ResourceHelper.getStringId(mActivity, "u9pay_cannel_btn");
            builder.setTitle(exitPayHint);
            builder.setNegativeButton(confirmStr, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    isClickClose = true;
                    // 检查支付结果
                    mPayCallBack.onResult(ResultCode.CANCEL, "支付取消", "");
                    checkResult();
                }
            });
            builder.setPositiveButton(cancelStr, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Logger.d(TAG, "继续支付");
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
        Logger.d(TAG, "onRestart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.d(TAG, "onResume");
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
        Logger.d(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Logger.d(TAG, "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.d(TAG, "onDestroy");
        handler.removeCallbacks(checkResultRunnable);
//        HYPlatform.getInstance().onDestrery();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.d(TAG, "onActivityResult");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Logger.d(TAG, "onConfigurationChanged");
    }
}
