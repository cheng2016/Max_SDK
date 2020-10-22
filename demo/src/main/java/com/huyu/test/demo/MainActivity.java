package com.huyu.test.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.huyu.sdk.HYSDK;
import com.huyu.sdk.data.ResultCode;
import com.huyu.sdk.data.bean.GameRoleInfo;
import com.huyu.sdk.data.bean.HYUser;
import com.huyu.sdk.data.bean.PayParams;
import com.huyu.sdk.listener.CallbackListener;
import com.huyu.sdk.listener.ExitCallback;
import com.huyu.sdk.listener.HYSDKListener;
import com.huyu.sdk.listener.LoginCallBackListener;
import com.huyu.sdk.listener.PayCallbackListener;
import com.huyu.sdk.util.Logger;
import com.huyu.sdk.util.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity implements OnClickListener {
    protected static final String TAG = "MainActivity";
    private Button fbShareBtn;
    private Button mLogonBtn;
    private Button mPayBtn;
    private Button mUpRoleBtn;
    private Button mLogoffBtn;
    private Button mExitBtn;
    private Button mSettingBtn;
    private TextView mUserInfoTv;

    /**
     * 提示:demo的提示信息使用的是Toast,游戏接入的时候,请替换成游戏Toast. 否则可能去与渠道Toast提示 冲突,导致崩溃
     **/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Logger.i("MainActivity", "onCreate");
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId(this, "hygame_activity"));

        //fbLoginBtn = findViewById(R.id.fb_login_button);
        fbShareBtn = (Button) findViewById(getId(this, "fb_share_button"));
        mLogonBtn = (Button) findViewById(getId(this, "login_button"));
        mPayBtn = (Button) findViewById(getId(this, "pay_button"));
        mLogoffBtn = (Button) findViewById(getId(this, "logout_button"));
        mUpRoleBtn = (Button) findViewById(getId(this, "up_role_button"));
        mExitBtn = (Button) findViewById(getId(this, "exit_button"));
        mSettingBtn = (Button) findViewById(getId(this, "setting_button"));
        mUserInfoTv = (TextView) findViewById(getId(this, "user_info_text"));

        mLogonBtn.setOnClickListener(this);
        mPayBtn.setOnClickListener(this);
        mUpRoleBtn.setOnClickListener(this);
        mLogoffBtn.setOnClickListener(this);
        mExitBtn.setOnClickListener(this);
        mSettingBtn.setOnClickListener(this);
        //fbLoginBtn.setOnClickListener(this);

        fbShareBtn.setOnClickListener(this);

        HYSDK.getInstance().onCreate(this);
        HYSDK.getInstance().initActivity(this, new CallbackListener() {
            @Override
            public void onResult(ResultCode resultCode, String msg, String data) {
                if (resultCode == ResultCode.SUCCESS) {
                    Logger.i(TAG," ------------- 初始化成功 ----------------");
                } else {
                    Logger.i(TAG,"初始化失败");
                }
            }
        });

        HYSDK.getInstance().setHYSDKListener(new HYSDKListener() {
            @Override
            public void onSwitchAccount(HYUser user) {
                ToastUtils.show(MainActivity.this, "切换账号成功");
                mUserInfoTv.setText("userId: " + user.userId
                        + "\nhyuid:" + user.hyuid + "\n渠道用户id:"
                        + user.channelUserId + "\n渠道用户名: "
                        + user.channelUserName + "\ntoken: "
                        + user.token
                        + "\n登录校验地址:");
            }

            @Override
            public void onLogout() {
                ToastUtils.show(MainActivity.this, "已注销");
                mUserInfoTv.setText("已注销");
            }
        });
    }

    @Override
    public void onBackPressed() {
        HYSDK.getInstance().exit(this);
    }

    @Override
    public void onClick(View v) {
        if (mLogonBtn == v) {
            doLogin();
        } else if (mPayBtn == v) {
            doPay();
        } else if (fbShareBtn == v) {
            //shareLinkToFaceBook();
        } else if (mLogoffBtn == v) {
            doLogOff();
        } else if (mExitBtn == v) {
            doExit();
        } else if (mUpRoleBtn == v) {
            uploadRoleData();
        } else if (mSettingBtn == v) {
            HYSDK.getInstance().showAccountCenter(MainActivity.this);
        }
    }

    void doLogOff() {
        HYSDK.getInstance().logout(MainActivity.this);
    }

    void uploadRoleData() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String time = dateFormat.format(new Date());
        //HY_Constants.ENTER_SERVER 登录服务器
        // HY_Constants.LEVEL_UP 升级
        // HY_Constants.CREATE_ROLE 创建角色 请在不同的场景分别调用
        GameRoleInfo gameRoleInfo = new GameRoleInfo();
        gameRoleInfo.setTypeId(3);
        gameRoleInfo.setRoleId("1235698465");
        gameRoleInfo.setRoleName("欧阳锋");
        gameRoleInfo.setRoleLevel("11");
        gameRoleInfo.setZoneId("1358");
        gameRoleInfo.setZoneName("狂战一区");
        gameRoleInfo.setBalance(10);
        gameRoleInfo.setVip("3");
        gameRoleInfo.setPartyName("丐帮");
        gameRoleInfo.setCreateTime(time);
        HYSDK.getInstance().roleReport(MainActivity.this, gameRoleInfo, new CallbackListener() {
            @Override
            public void onResult(ResultCode resultCode, String msg, String data) {
                if (resultCode == ResultCode.SUCCESS) {
                    ToastUtils.show(MainActivity.this, "上传角色成功");
                } else {
                    ToastUtils.show(MainActivity.this, "上传角色失败");
                }
            }
        });
    }

    private void doLogin() {
        HYSDK.getInstance().login(MainActivity.this, new LoginCallBackListener() {
            @Override
            public void onLoginSuccess(HYUser user) {
                Logger.i(TAG, "onLoginSuccess");
                mUserInfoTv.setText("userId: " + user.userId
                        + "\nhyuid:" + user.hyuid + "\n渠道用户id:"
                        + user.channelUserId + "\n渠道用户名: "
                        + user.channelUserName + "\ntoken: "
                        + user.token
                        + "\n登录校验地址:");
            }

            @Override
            public void onLoginCancel() {
                Logger.i(TAG, "onLoginCancel");
                Toast.makeText(MainActivity.this,"onLoginCancel",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoginFailed(String message) {
                Logger.i(TAG, "onLoginFailed");
                Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void doPay() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String time = dateFormat.format(new Date());
        PayParams payParams = new PayParams();
        payParams.setAmount(99);// 充值金额
        payParams.setExchange(10);// 兑换率
        payParams.setProductId("com.mon20190902.gp299.99");// google后台配置的商品id
        payParams.setProductName("60钻石");// 商品名称
        payParams.setBody("获得60钻石");// 商品描述
        payParams.setCallBackUrl("");// 回调地址
        payParams.setGameOrderId("game" + time);// 订单号
        payParams.setAppExtInfo("支付回调拓展字段");
//        payParams.setPayChannel("");

        HYSDK.getInstance().pay(MainActivity.this, payParams, new PayCallbackListener() {
            @Override
            public void onPaySuccess(PayParams params) {
                ToastUtils.show(MainActivity.this, "支付成功");
            }

            @Override
            public void onPayCancel() {
                ToastUtils.show(MainActivity.this, "支付取消");
            }

            @Override
            public void onPayFailed(String msg) {
                ToastUtils.show(MainActivity.this, "支付失败 msg : " + msg);
            }
        });
    }

    /**
     * 退出接口说明：
     * <p>
     * 当前activity
     * 退出回调
     */
    private void doExit() {
//        HYSDK.getInstance().exit(this);
        HYSDK.getInstance().exit(this, new ExitCallback() {
            @Override
            public void onGameExit(Activity activity) {
                Toast.makeText(activity,"-- unity游戏退出接口 onGameExit --",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static int getId(Context paramActivity, String id) {
        String packageName = paramActivity.getPackageName();
        return paramActivity.getResources()
                .getIdentifier(id, "id", packageName);
    }

    public static int getLayoutId(Context paramActivity, String id) {
        String packageName = paramActivity.getPackageName();
        return paramActivity.getResources().getIdentifier(id, "layout",
                packageName);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        HYSDK.getInstance().onNewIntent(intent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        HYSDK.getInstance().onConfigurationChanged(newConfig);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        HYSDK.getInstance().onActivityResult(requestCode,resultCode,data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        HYSDK.getInstance().onStart(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        HYSDK.getInstance().onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        HYSDK.getInstance().onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        HYSDK.getInstance().onStop(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HYSDK.getInstance().onDestroy(this);
    }
}
