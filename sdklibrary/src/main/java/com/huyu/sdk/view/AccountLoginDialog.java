package com.huyu.sdk.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.huyu.sdk.HYPlatform;
import com.huyu.sdk.data.ResultCode;
import com.huyu.sdk.data.config.SharedPreferenceHelper;
import com.huyu.sdk.listener.CallbackListener;
import com.huyu.sdk.util.ResourceHelper;
import com.huyu.sdk.util.ToastUtils;

/**
 * @author chengzj
 * @time 2020/7/23 18:40
 * Description: AccountLoginDialog
 */
public class AccountLoginDialog extends Dialog {
    private Context context;

    private CallbackListener listener;

    private EditText et_register_account;

    private EditText et_register_psd;

    private Button btn_register;

    private CheckBox checkbox_contract;

    private int loginType;

    public static final int ACCOUNT_LOGIN = 0;
    public static final int ACCOUNT_SWITCH = 1;

    public AccountLoginDialog(Context context, CallbackListener listener) {
        super(context, ResourceHelper.getStyleId(context,"hy_base_pop"));
        setContentView(ResourceHelper.getLayoutId(context,"hy_dialog_account_login"));
        setCanceledOnTouchOutside(false);
        this.context = context;
        this.listener = listener;
        this.loginType = ACCOUNT_SWITCH;
        initView();
    }

    public AccountLoginDialog(Context context, CallbackListener listener, int loginType) {
        super(context, ResourceHelper.getStyleId(context,"hy_base_pop"));
        setContentView(ResourceHelper.getLayoutId(context,"hy_dialog_account_login"));
        setCanceledOnTouchOutside(false);
        this.context = context;
        this.listener = listener;
        this.loginType = loginType;

        initView();

        if (loginType == ACCOUNT_LOGIN){
            et_register_account.setText(SharedPreferenceHelper.getChannelUserName());
            et_register_psd.setText(SharedPreferenceHelper.getUserPassword());
        }
    }

    void initView() {
        this.et_register_account = (EditText) findViewById(ResourceHelper.getId(context,"et_register_account"));
        this.et_register_psd = (EditText) findViewById(ResourceHelper.getId(context,"et_register_psd"));
        this.btn_register = (Button) findViewById(ResourceHelper.getId(context,"btn_register"));
        this.btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        this.checkbox_contract = findViewById(ResourceHelper.getId(context,"checkbox_contract"));
        this.checkbox_contract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new AccountContractWebDialog(context, listener);
                dialog.show();
            }
        });
    }

    private void login() {
        final String account = this.et_register_account.getText().toString().trim();
        final String psd = this.et_register_psd.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            ToastUtils.show(context, ResourceHelper.getStringId(context,"hy_acount_cannot_be_null"));
            return;
        }
        if (TextUtils.isEmpty(psd)) {
            ToastUtils.show(context, ResourceHelper.getStringId(context,"hy_password_cannot_be_null"));
            return;
        }
        if(!this.checkbox_contract.isChecked()){
            ToastUtils.show(context, ResourceHelper.getStringId(context,"hy_check_user_contract"));
            return;
        }
        HYPlatform.getInstance().accountlogin(context, loginType, account, psd, new CallbackListener() {
            @Override
            public void onResult(ResultCode resultCode, String msg, String data) {
                if (resultCode == ResultCode.SUCCESS) {
                    dismiss();
                    listener.onResult(resultCode, msg, data);
                } else {
                    FailPop.getInstance(context, msg);
                }
            }
        });
    }

    @Override
    public void cancel() {
        super.cancel();
        listener.onResult(ResultCode.CANCEL, "取消", "");
    }
}
