package com.huyu.sdk.data.bean;

import android.text.TextUtils;

/**
 * Author: SimonTse
 * Date:  2018/3/21
 * Email: xiejx_op@foxmail.com
 * Describe:
 */
public class UserInfoVo
{

    /** 登录部分 */
    public static String userId=""; // 互娱的用户id
    private String hyuid; //新版本唯一识别id
    public static String channelUserId=""; // 渠道用户ID。
    public static String channelUserName=""; // 渠道用户账号。
    public static String token=""; // 接入用到的token



    public int loginType = 0;//区分登录类型 0  普通登录 2 fb 登录


    /**个别渠道可能用到，如：魅族）*/
    private String customInfo="";
    private String errorMessage=""; // 错误码和错误信息


    public boolean isValid()
    {
        return !TextUtils.isEmpty(userId);
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }
    
    public String getHYuid()
    {
    	if(TextUtils.isEmpty(hyuid)){
    		return userId;
    	}
        return hyuid;
    }

    public void setHYuid (String hyuid)
    {
        this.hyuid = hyuid;
    }

    public String getChannelUserId()
    {
        return channelUserId;
    }

    public void setChannelUserId(String channelUserId)
    {
        this.channelUserId = channelUserId;
    }

    public String getChannelUserName()
    {
        return channelUserName;
    }

    public void setChannelUserName(String channelUserName)
    {
        this.channelUserName = channelUserName;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }

    public String getCustomInfo()
    {
        return customInfo;
    }

    public void setCustomInfo(String customInfo)
    {
        this.customInfo = customInfo;
    }

    public int getLoginType() {
        return loginType;
    }

    public void setLoginType(int loginType) {
        this.loginType = loginType;
    }

    @Override
    public String toString()
    {
        return "HY_UserInfoVo {userId=" + userId + ", channelUserId="
                + channelUserId + ", channelUserName=" + channelUserName
                + ", token=" + token +  ", customInfo="
                + customInfo + ", errorMessage=" + errorMessage + "}";
    }


    
}
