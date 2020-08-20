package com.huyu.sdk.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.huyu.sdk.HYPlatform;
import com.huyu.sdk.data.ResultCode;
import com.huyu.sdk.listener.CallbackListener;
import com.huyu.sdk.util.Logger;
import com.huyu.sdk.util.ResourceHelper;

/**
 * @author chengzj
 * @time 2020/7/20 20:36
 * Description: LoginPop 登录框
 */
public class LoginPop extends Dialog   {
    public static final String TAG = LoginPop.class.getSimpleName();
    private Context context;

    private CallbackListener loginListener;

    View btn_login;

    public LoginPop(Context context, CallbackListener loginListener) {
        super(context, ResourceHelper.getStyleId(context,"base_pop"));
        setContentView(ResourceHelper.getLayoutId(context,"dialog_login"));

        this.context = context;
        setCanceledOnTouchOutside(false);
        initView();
        setListener();
        this.loginListener = loginListener;
    }

    void initView() {
        this.btn_login =  findViewById(ResourceHelper.getId(context,"btn_login"));
    }

    void setListener() {
        this.btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HYPlatform.getInstance().guestLogin(context,new CallbackListener() {
                    @Override
                    public void onResult(ResultCode resultCode, String msg, String data) {
                        loginListener.onResult(resultCode,msg,data);
                        if(resultCode == ResultCode.SUCCESS){
                            Logger.i(TAG,"游客登录成功");
                            dismiss();
                        }
                        if (resultCode == ResultCode.Fail) {
                            FailPop.getInstance(LoginPop.this.context, msg);
                            return;
                        }
                    }
                });
            }
        });
    }


    @Override
    public void cancel() {
        super.cancel();
        loginListener.onResult(ResultCode.CANCEL, "登录取消", "");
    }
}
