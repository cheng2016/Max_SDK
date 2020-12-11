package com.huyu.sdk.data.bean;

/*******************************************
 * @AUTHOR:smile
 * @VERSION:v1.0
 *******************************************/
public class PayParams {
    /**
     * amount:充值总金额
     */
    private int amount;
    /**
     * 兑换率
     */
    private int exchange;
    /**
     * U9订单号
     */
    private String orderId;
    /**
     * gameOrderId:游戏订单号
     */
    private String gameOrderId;
    /**
     * 商品描述
     */
    private String body;
    /**
     * appExtInfo:CP方拓展信息，比如区服角色名之类的
     */
    private String appExtInfo;
    /**
     * 　商品id
     */
    private String productId;
    /**
     * 　商品名称
     */
    private String productName;
    /**
     * callBackUrl:支付回调地址（正式环境优先读取客户端传入的地址，如果为空，则从后台配置读取）
     */
    private String callBackUrl;

    /***payPal 支付类型标识***/
    private String payChannel;

    /**
     * u9uid
     */
    private String u9uid;
    /**
     * 　支付地址
     */
    private String payUrl;

    /*****google支付初始化的公钥****/
    private String googlePublicKey;

    /** 切换支付key*/
    private String pgooglePublicKey;
    /** 切换支付的包名 **/
    private String packageName;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getExchange() {
        return exchange;
    }

    public void setExchange(int exchange) {
        this.exchange = exchange;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getGameOrderId() {
        return gameOrderId;
    }

    public void setGameOrderId(String gameOrderId) {
        this.gameOrderId = gameOrderId;
    }

    public String getAppExtInfo() {
        return appExtInfo;
    }

    public void setAppExtInfo(String appExtInfo) {
        this.appExtInfo = appExtInfo;
    }


    public String getCallBackUrl() {
        return callBackUrl;
    }

    public void setCallBackUrl(String callBackUrl) {
        this.callBackUrl = callBackUrl;
    }

    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }

    public String getPayUrl() {
        return payUrl;
    }

    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl;
    }

    public String getU9uid() {
        return u9uid;
    }

    public void setU9uid(String u9uid) {
        this.u9uid = u9uid;
    }

    public String getGooglePublicKey() {
        return googlePublicKey;
    }

    public void setGooglePublicKey(String googlePublicKey) {
        this.googlePublicKey = googlePublicKey;
    }
    public String getPGooglePublicKey() {
        return pgooglePublicKey;
    }

    public void setPGooglePublicKey(String pgooglePublicKey) {
        this.pgooglePublicKey = pgooglePublicKey;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public String toString() {
        return "支付信息:[amount=" + amount + ", gameOrderId=" + gameOrderId
                + ", appExtInfo=" + appExtInfo + ", callBackUrl=" + callBackUrl
                + ",payChannel=" + payChannel + ",payUrl=" + payUrl
                + ",googlePublicKey=" + googlePublicKey + ",u9uid=" + u9uid + "]";
    }

}
