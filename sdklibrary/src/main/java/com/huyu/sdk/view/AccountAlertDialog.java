package com.huyu.sdk.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.huyu.sdk.listener.CallbackListener;
import com.huyu.sdk.util.ResourceHelper;

/**
 * @author chengzj
 * @time 2020/7/23 17:37
 * Description: HYAlertDialog
 */
public class AccountAlertDialog extends Dialog {
    private Context context;

    private CallbackListener loginListener;

    public AccountAlertDialog(Context context, CallbackListener loginListener) {
        super(context, ResourceHelper.getStyleId(context, "base_pop"));
        setContentView(ResourceHelper.getLayoutId(context, "hy_dialog_account_alert"));

        this.context = context;
        setCanceledOnTouchOutside(false);

        this.loginListener = loginListener;
        this.findViewById(ResourceHelper.getId(context,"btn_comfirm")).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
