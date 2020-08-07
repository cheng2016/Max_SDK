package com.huyu.sdk.view;

import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.huyu.sdk.HYPlatform;
import com.huyu.sdk.R;
import com.huyu.sdk.data.ResultCode;
import com.huyu.sdk.data.config.SharedPreferenceHelper;
import com.huyu.sdk.listener.CallbackListener;
import com.huyu.sdk.util.Logger;

/**
 * @author chengzj
 * @time 2020/7/21 19:28
 * Description: AcountCenterDialog
 */
public class AccountCenterDialog extends Dialog {
    public static final String TAG = AccountCenterDialog.class.getSimpleName();
    private Context context;

    private CallbackListener mCallbackListener;

    private View bindLayout;

    private TextView switchTv;

    private TextView bindTv;

    public AccountCenterDialog(Context context) {
        super(context, R.style.base_pop);
        this.context = context;
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_acount_center);
        initView();
        setListener();
    }


    public AccountCenterDialog(Context context, CallbackListener listener) {
        this(context);
        this.mCallbackListener = listener;
    }

    void initView() {
        bindLayout = findViewById(R.id.bind_layout);
        switchTv = findViewById(R.id.switch_tv);
        bindTv = findViewById(R.id.bind_tv);
        String content = getContext().getString(R.string.hy_login_other_account) + "  <font color='#FF6100'>" + getContext().getString(R.string.hy_switch_account) + "</font>";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            switchTv.setText(Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY));
        } else {
            switchTv.setText(Html.fromHtml(content));
        }

        if (SharedPreferenceHelper.getIsBindAccount() == 0) {
            bindLayout.setEnabled(true);
            bindTv.setText(getContext().getString(R.string.hy_bind_account));
        } else {
            bindLayout.setEnabled(false);
            bindTv.setText(getContext().getString(R.string.hy_bound));
        }
    }

    void setListener() {
        bindLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new AccountBindDialog(context, new CallbackListener() {
                    @Override
                    public void onResult(ResultCode resultCode, String msg, String data) {
                        if (mCallbackListener != null)
                            mCallbackListener.onResult(resultCode, msg, data);
                        if (resultCode == ResultCode.SUCCESS) {
                            Logger.i(TAG, "绑定账户成功");
                            dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });
        switchTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSwitchDialog();
            }
        });
    }

    void startSwitchDialog() {
        Dialog dialog = new AccountSwitchDialog(context, new CallbackListener() {
            @Override
            public void onResult(ResultCode resultCode, String msg, String data) {
                if (mCallbackListener != null)
                    mCallbackListener.onResult(resultCode, msg, data);
                if (resultCode == ResultCode.SUCCESS) {
                    Logger.i(TAG, "切换账户成功");
                    dismiss();
                }
            }
        });
        dialog.show();
    }

    @Override
    public void cancel() {
        super.cancel();
        if (mCallbackListener != null)
            mCallbackListener.onResult(ResultCode.CANCEL, "取消", "");
    }
}