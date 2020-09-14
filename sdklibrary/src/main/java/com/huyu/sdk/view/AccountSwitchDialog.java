package com.huyu.sdk.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.huyu.sdk.data.ResultCode;
import com.huyu.sdk.listener.CallbackListener;
import com.huyu.sdk.util.ResourceHelper;

/**
 * @author chengzj
 * @time 2020/7/23 16:23
 * Description: SwitchAccountDialog
 */
public class AccountSwitchDialog extends Dialog   {
    public static final String TAG = AccountSwitchDialog.class.getSimpleName();
    private Context context;

    private CallbackListener mCallbackListener;

    public AccountSwitchDialog(final Context context, final CallbackListener loginListener) {
        super(context, ResourceHelper.getStyleId(context,"base_pop"));
        setContentView(ResourceHelper.getLayoutId(context,"hy_dialog_account_switch"));

        this.context = context;
        setCanceledOnTouchOutside(false);

        this.mCallbackListener = loginListener;

        this.findViewById(ResourceHelper.getId(context,"btn_login")).setOnClickListener(new View.OnClickListener() {
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
    public void cancel() {
        super.cancel();
        mCallbackListener.onResult(ResultCode.CANCEL, "取消", "");
    }
}
