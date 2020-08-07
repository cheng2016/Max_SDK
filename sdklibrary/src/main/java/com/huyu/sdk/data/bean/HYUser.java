package com.huyu.sdk.data.bean;

/**
 * 文件名：HY_User
 * 创建日期：2020/8/4 11:14
 * 描述：TODO
 */
public class HYUser {
    public String token;
    //u9用户id,用于登录、支付校验（重要）
    public String userId;
    //辉耀用户id
    public String hyuid;
    //渠道返回的用户id
    public String channelUserId;

    public String channelUserName;

    public String info;

    public String extInfo;

    public int is_bind_account;
}
