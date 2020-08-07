package com.huyu.sdk.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.huyu.sdk.HYPlatform;
import com.huyu.sdk.R;
import com.huyu.sdk.data.ResultCode;
import com.huyu.sdk.listener.CallbackListener;
import com.huyu.sdk.util.ToastUtils;

/**
 * @author chengzj
 * @time 2020/7/23 16:22
 * Description: BindAccountDialog
 */
public class AccountBindDialog extends Dialog   {
    private Context context;

    private CallbackListener mCallbackListener;

    private EditText et_register_account;

    private EditText et_register_psd;

    private Button btn_register;

    private CheckBox checkbox_contract;

    public AccountBindDialog(Context context, CallbackListener listener) {
        super(context, R.style.base_pop);

        this.context = context;
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_account_bind);

        this.mCallbackListener = listener;

        initView();
    }

    void initView() {
        this.et_register_account = (EditText) findViewById(R.id.et_register_account);
        this.et_register_psd = (EditText) findViewById(R.id.et_register_psd);
        this.btn_register = (Button) findViewById(R.id.btn_register);
        this.btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        this.checkbox_contract = findViewById(R.id.checkbox_contract);
        this.checkbox_contract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new AccountContractWebDialog(context,mCallbackListener);
                dialog.show();
            }
        });
    }

    private void register() {
        final String account = this.et_register_account.getText().toString().trim();
        final String psd = this.et_register_psd.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            ToastUtils.show(context, "账号不能为空");
            return;
        }
        if (TextUtils.isEmpty(psd)) {
            ToastUtils.show(context, "密码不能为空");
            return;
        }
        HYPlatform.getInstance().bindAccount(context, account, psd, new CallbackListener() {
            @Override
            public void onResult(ResultCode resultCode, String msg, String data) {
                if (resultCode == ResultCode.SUCCESS) {
                    dismiss();
                    mCallbackListener.onResult(resultCode, msg, data);
                    Dialog dialog = new AccountAlertDialog(context, mCallbackListener);
                    dialog.show();
                } else {
                    FailPop.getInstance(context, msg);
                }
            }
        });
    }


}
