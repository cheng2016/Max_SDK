package com.huyu.sdk.view;

import android.app.Dialog;
import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.huyu.sdk.util.ResourceHelper;


public class FailPop extends Dialog {
    public static FailPop instance;

    private Button btn_return;

    Context context;

    private String text;

    private TextView tv_fail;

    private FailPop(Context context, String paramString) {
        super(context, ResourceHelper.getStyleId(context, "hy_base_pop"));
        setContentView(ResourceHelper.getLayoutId(context, "hy_pop_fail"));
        this.context = context;
        this.text = paramString;
        setCanceledOnTouchOutside(false);
        initview();
        setListener();
    }

    public static void getInstance(Context paramContext, String paramString) {
        instance = new FailPop(paramContext, paramString);
        instance.show();
    }

    private void initview() {
        this.tv_fail = (TextView) findViewById(ResourceHelper.getId(context, "tv_fail"));
        this.btn_return = (Button) findViewById(ResourceHelper.getId(context, "btn_return"));

        this.tv_fail.setText(this.text);
        (new InterThread(6000L, 1000L)).start();
    }

    private void setListener() {
        this.btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }

    public void cancel() {
        super.cancel();
    }

    class InterThread extends CountDownTimer {
        public InterThread(long param1Long1, long param1Long2) {
            super(param1Long1, param1Long2);
        }

        public void onFinish() {
            FailPop.instance.cancel();
        }

        public void onTick(long param1Long) {
            FailPop.this.btn_return.setText("返回（" + (param1Long / 1000L) + "s）");
        }
    }
}