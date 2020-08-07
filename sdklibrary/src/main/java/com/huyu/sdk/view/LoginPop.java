package com.huyu.sdk.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.huyu.sdk.HYPlatform;
import com.huyu.sdk.R;
import com.huyu.sdk.data.ResultCode;
import com.huyu.sdk.listener.CallbackListener;
import com.huyu.sdk.util.Logger;

/**
 * @author chengzj
 * @time 2020/7/20 20:36
 * Description: LoginPop 登录框
 */
public class LoginPop extends Dialog implements View.OnClickListener {
    public static final String TAG = LoginPop.class.getSimpleName();
    private Context context;

    private CallbackListener loginListener;

    View btn_login;

    public LoginPop(Context context, CallbackListener loginListener) {
        super(context, R.style.base_pop);

        this.context = context;
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_login);
        initView();
        setListener();
        this.loginListener = loginListener;
    }

    void initView() {
        this.btn_login =  findViewById(R.id.btn_login);
    }

    void setListener() {
        this.btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_login){
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

    }

    @Override
    public void cancel() {
        super.cancel();
        loginListener.onResult(ResultCode.CANCEL, "登录取消", "");
    }
}
