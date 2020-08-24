package com.huyu.googlepay;

import android.app.Activity;
import android.text.TextUtils;

import com.huyu.googlepay.util.AppsFlyerActionHelper;
import com.huyu.googlepay.util.IabHelper;
import com.huyu.googlepay.util.IabResult;
import com.huyu.googlepay.util.Inventory;
import com.huyu.googlepay.util.Purchase;
import com.huyu.sdk.HYPlatform;
import com.huyu.sdk.U9Platform;
import com.huyu.sdk.data.ResultCode;
import com.huyu.sdk.data.bean.PayParams;
import com.huyu.sdk.data.config.XmlConfigHelper;
import com.huyu.sdk.listener.CallbackListener;
import com.huyu.sdk.util.Logger;
import com.huyu.sdk.util.ToastUtils;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class GooglePlayPayManager {
    public static String GOOGLE_PAY = "GOOGLEPAY ";
    private static final String TAG = "GooglePay";
    /**
     * 支付参数
     */
    private PayParams mPayParsms;

    public CallbackListener payCallback;
    //标识 是否是 纯google 支付 ，来确定数据Af 购买数据上报（有 网页中打开的google支付，有 直接调起的 google支付，直接调起的 在此类支付完成需上报）
    public boolean isOnlyGooglePay = false;

    public Activity mActivity;

    public static GooglePlayPayManager instance;

    public GooglePlayPayManager(Activity mActivity) {
        this.mActivity = mActivity;
    }


    /********发起支付*****/
    public void doPay(String googlePublicKey, String productId) {
        this.mPayParsms = U9Platform.mPayParams;
        Logger.d(TAG, "打开谷歌支付并且初始化>>productId传入》" + productId);
        GooglePayInit(googlePublicKey, productId);
    }

    /*************************************** googlePay start ********************************************************/

    String googlePublicKey = "";
    String sku;
    IabHelper mHelper;

    public IabHelper getmHelper() {
        return mHelper;
    }

    //googlePay 支付
    public void GooglePayInit(String googlePublicKey, String productId) {
        if (TextUtils.isEmpty(googlePublicKey)) {
            ToastUtils.show(mActivity, "Init GoooglePay Fail");
            return;
        }
        sku = productId;//商品id
        Logger.d(TAG, "googlePay商品id" + sku);
        mHelper = new IabHelper(mActivity, googlePublicKey);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    Logger.i(TAG, "Problem setting up In-app Billing: " + result);
                    ToastUtils.show(mActivity, "Init GoooglePay Fail");

                    Logger.d(TAG, "初始化googlePay失败");
                    return;
                }
                if (mHelper == null) return;
                //初始化成功
                try {
                    //查询可购买的商品，并实现查询方法的回调
                    mHelper.queryInventoryAsync(mGotInventoryListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    e.printStackTrace();
                    //ToastUtils.show(mActivity,"密钥参数错误");
                }
            }
        });
    }

    /**
     * 查询商品的回调
     */
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            //非空
            if (mHelper == null) return;
            //库存商品查询成功
            if (result.isSuccess()) {
                //输出日志，便于跟踪代码
                System.out.println("--------------查询成功");
                Logger.d(TAG, "查询库存信息成功");
                //库存商品查询成功后，进行与当前sku的匹配，确保当前传入的sku在库存中是否存在
                Purchase premiumPurchase = inventory.getPurchase("" + sku);
                //匹配到库存中有当前的sku，说明查询到了需要消耗的商品，执行消耗操作
                if (premiumPurchase != null) {
                    System.out.println("--------------查询需要消耗的商品之后执行消耗操作");
                    Logger.d(TAG, "查询到此商品需要消耗操作");
                    //google商品消耗请求方法，并实现消耗回调 （消耗请求）
                    consumeProduce(premiumPurchase, true);
                    //没有查询到要消耗的商品（可能已经消耗成功，或者该商品没有购买过）
                } else {
                    //那就直接执行购买操作
                    Logger.d(TAG, "此商品不需要消耗操作直接进行googlePay支付");
                    googlePay();
                }
            }
            //select失败
            if (result.isFailure()) {
                Logger.d(TAG, "查询此商品在库存中失败");
                System.out.println("--------------查询失败");
            }
        }
    };


    /********
     * 用于标识 消耗完 商品 是否 购买
     */
    boolean isAbleBuy = false;

    /*******消耗商品******/
    private void consumeProduce(Purchase purchase, boolean isBuy) {

        isAbleBuy = isBuy;
        try {

            String sku = purchase.getSku();
            Logger.d(TAG, "购买完成消耗商品ID66>>>" + sku);

            mHelper.consumeAsync(purchase, mConsumeFinishedListener);

        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        }
    }


    /**
     * 消耗回调
     */
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            //非空
            if (mHelper == null) return;

            String message = result.getMessage();

            Logger.d(TAG, "消耗商品返回msg>" + message);
            //消耗成功方法
            if (result.isSuccess()) {
                //当消耗成功后，purchase为消耗成功的对象，判断purchase的sku是否和当前的sku相同，确保消耗的商品正确
                if (purchase.getSku().equals("" + sku)) {
                    System.out.println("------------消耗成功");
                    Logger.d(TAG, "消耗" + sku + "商品成功");
                    //因为google明确要求，需要先消耗后购买管理商品，所以当消耗成功后，发起购买
                    //购买方法
                    if (isAbleBuy) {
                        googlePay();
                    }
                }
            } else {
                Logger.d(TAG, "消耗" + sku + "失败");
            }
        }
    };


    //google 支付请求码
    static final int RC_REQUEST = 10001;

    /**
     * google内购支付购买方法
     */
    protected void googlePay() {
        //sku  当前商品的内购ID
        //RC_REQUEST  这个是作为支付请求代码传
        //mPurchaseFinishedListener  购买回调
        //payload作为透传参数，我这里传的是订单号。
        //因为订单号不能唯一，所以使用当前时间生成订单号
        try {
            Logger.d(TAG, "进行支付");
            mHelper.launchPurchaseFlow(mActivity, sku, RC_REQUEST, mPurchaseFinishedListener, mPayParsms.getOrderId());
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        }
    }


    /**
     * 购买回调
     */
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            //返回购买回调结果
            Logger.i("TAG", "-----购买回调:" + result);
            //返回购买成功的商品
            Logger.i("TAG", "-----购买回调:" + purchase);
            //非空
            if (mHelper == null) return;
            //失败方法


            if (!result.isSuccess()) {
                Logger.d(TAG, "---------------GP购买失败>>>" + result.getMessage());

            } else {
                Logger.d(TAG, "---------------GP购买成功>>>" + result.getMessage());
                //支付成功之后如果需要重复购买的话，需要进行消耗商品
                //（这里的消耗和查询的消耗是一样的方法，之所以调用两次消耗方法，是因为害怕如果因为网络延迟在在某一步没有消耗，所以做的一个加固消耗操作）
                if (purchase.getSku().equals("" + sku)) {
                    System.out.println("-------------支付成功之后如果需要重复购买的话，需要进行消耗商品");
                    //执行下号方法
                    //mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                    //进行商品消耗
                    orderServerVerify(purchase);//去我们服务器进行订单校验
                    Logger.d(TAG, "---------------购买成功商品消耗");
                    consumeProduce(purchase, false);
                }
            }
        }
    };


    /****
     * 谷歌支付完成 参数通知给辉耀服务器
     * @param
     */
    public void orderServerVerify(Purchase purchase) {
        String payNotifyUrl = XmlConfigHelper.getPayCallbackUrl(mActivity);
        Map<String, String> params = new HashMap<>();
        params.put("order_id", mPayParsms.getOrderId());
        params.put("pay_amount", mPayParsms.getAmount() + "");
        params.put(" inapp_purchase_data", purchase.getOriginalJson());
        if (!TextUtils.isEmpty(purchase.getSignature())) {
            params.put("inapp_data_signature", URLEncoder.encode(purchase.getSignature()));
        }
        HYPlatform.getInstance().orderServerVerify(mActivity, payNotifyUrl, params, new CallbackListener() {
            @Override
            public void onResult(ResultCode resultCode, String msg, String data) {
                if (resultCode == ResultCode.SUCCESS) {
                    if (payCallback != null) {
                        //appFlyer 购买事件上报
                        if (isOnlyGooglePay) {
                            AppsFlyerActionHelper.buyEvent(mActivity, mPayParsms.getAmount() + "", mPayParsms.getProductId());
                        }
                        Logger.d(TAG, "支付验证成功");
                        payCallback.onResult(ResultCode.SUCCESS, "Pay check success !", "");
                    }
                } else if (resultCode == ResultCode.Fail) {
                    if (payCallback != null) {
                        payCallback.onResult(ResultCode.Fail, "Pay check fail !", "");
                    }
                }
            }
        });
    }


    /**
     * 销毁 谷歌支付 Helper 连接类
     */
    public void release() {
        if (mHelper != null) {
            try {
                mHelper.dispose();
            } catch (IabHelper.IabAsyncInProgressException e) {
                e.printStackTrace();
            }
            mHelper = null;
        }
    }






   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (mHelper == null) return;
        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        }
        else {
            Logger.i(TAG, "onActivityResult handled by IABUtil.");
        }
    }*/




   /* @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHelper != null) {
            try {
                mHelper.dispose();
            } catch (IabHelper.IabAsyncInProgressException e) {
                e.printStackTrace();
            }
            mHelper = null;
        }
    }*/
};

