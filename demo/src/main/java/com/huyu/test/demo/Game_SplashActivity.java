package com.huyu.test.demo;

import android.content.Intent;

import com.huyu.sdk.splash.HY_SplashActivity;

/**
 * Author: SimonTse
 * Date:  2018/4/10
 * Email: xiejx_op@foxmail.com
 * Describe:
 */

public class Game_SplashActivity extends HY_SplashActivity
{

    @Override
    public boolean getIsLandscape() {
        //横竖屏设置
        // true 横屏   false竖屏
        return false;
    }


    @Override
    public void onSplashStop()
    {
        //闪屏播放结束，跳转游戏主Activity，并结束闪屏Activity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}
