package com.huyu.sdk.view;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.huyu.sdk.listener.CallbackListener;
import com.huyu.sdk.util.ResourceHelper;

/**
 * @author chengzj
 * @time 2020/7/24 11:20
 * Description: WebDialog
 */
public class AccountContractWebDialog extends Dialog {
    public static final String TAG = AccountContractWebDialog.class.getSimpleName();

//    String csUrl = HY_Constants.URL_QYCS_HY+"?device=android";

    String csUrl = "http://huiyaoabc.com/policy.html";

    private Context context;

    private CallbackListener loginListener;

    private ImageView mBackBtn;
    private WebView webView;
    private ProgressBar pro;

    public AccountContractWebDialog(final Context context, final CallbackListener loginListener) {
        super(context, ResourceHelper.getStyleId(context,"base_pop"));
        setContentView(ResourceHelper.getLayoutId(context,"dialog_account_contract_web"));
        this.loginListener = loginListener;
        this.context = context;
        setCanceledOnTouchOutside(false);

        pro = (ProgressBar) findViewById(ResourceHelper.getId(context,"u9game_term_loading"));
        ImageView quite = findViewById(ResourceHelper.getId(context,"u9game_term_quite"));
        quite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        initWebView();
        setDefaultPostion();
    }

    void initWebView(){

        FrameLayout contentView = findViewById(ResourceHelper.getId(context,"u9game_term_webview"));
        webView = new WebView(context.getApplicationContext());
        contentView.addView(webView);
//        webView = findViewById(R.id.u9game_term_webview);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.requestFocusFromTouch();
        webView.loadUrl(csUrl);
        WebSettings webSettings = webView.getSettings();
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        // webSettings.setPluginsEnabled(true);
        webSettings.setSupportZoom(true);// 支持缩放
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);// 关闭WebView中缓存
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    pro.setVisibility(View.GONE);
                    // progressBar.setProgress(newProgress);
                } else {
                    pro.setVisibility(View.VISIBLE);
                    pro.setProgress(newProgress);// 设置加载进度
                }
            }
        });
        webView.setOverScrollMode(View.OVER_SCROLL_NEVER);// 上下阴影
    }

    /**
     * 设置横竖屏时的默认位置
     */
    private void setDefaultPostion() {

        WindowManager.LayoutParams lp = getWindow().getAttributes();

        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // 竖屏
            lp.width = context.getResources().getDisplayMetrics().widthPixels * 9 / 10;
            lp.height = context.getResources().getDisplayMetrics().heightPixels * 2 / 3;
        } else {
            // 横屏
            lp.width = context.getResources().getDisplayMetrics().widthPixels * 2 / 3;

            getWindow().setAttributes(lp);
        }
    }

    @Override
    public void cancel() {
        super.cancel();
        if (webView != null) {
            //加载null内容
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            //清除历史记录
            webView.clearHistory();
            //移除WebView
            ((ViewGroup) webView.getParent()).removeView(webView);
            //销毁VebView
            webView.destroy();
            //WebView置为null
            webView = null;
        }
    }
}
