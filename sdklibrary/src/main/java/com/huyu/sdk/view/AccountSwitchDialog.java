package com.huyu.sdk.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.huyu.sdk.R;
import com.huyu.sdk.data.ResultCode;
import com.huyu.sdk.listener.CallbackListener;

/**
 * @author chengzj
 * @time 2020/7/23 16:23
 * Description: SwitchAccountDialog
 */
public class AccountSwitchDialog extends Dialog implements View.OnClickListener {
    public static final String TAG = AccountSwitchDialog.class.getSimpleName();
    private Context context;

    private CallbackListener mCallbackListener;

    public AccountSwitchDialog(final Context context, final CallbackListener loginListener) {
        super(context, R.style.base_pop);

        this.context = context;
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_account_switch);

        this.mCallbackListener = loginListener;

        this.findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new AccountLoginDialog(context, new CallbackListener() {
                    @Override
                    public void onResult(ResultCode resultCode, String msg, String data) {
                        if(resultCode == ResultCode.SUCCESS){
                            mCallbackListener.onResult(resultCode,msg,data);
                            dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void cancel() {
        super.cancel();
        mCallbackListener.onResult(ResultCode.CANCEL, "取消", "");
    }
}
