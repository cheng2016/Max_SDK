package com.huyu.sdk.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.huyu.sdk.R;
import com.huyu.sdk.listener.CallbackListener;

/**
 * @author chengzj
 * @time 2020/7/23 17:37
 * Description: HYAlertDialog
 */
public class AccountAlertDialog extends Dialog   {
    private Context context;

    private CallbackListener loginListener;

    public AccountAlertDialog(Context context, CallbackListener loginListener) {
        super(context, R.style.base_pop);

        this.context = context;
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_account_alert);

        this.loginListener = loginListener;
        this.findViewById(R.id.btn_comfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
