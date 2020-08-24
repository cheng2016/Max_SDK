package com.huyu.sdk.splash;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.huyu.sdk.util.HY_Utils;
import com.huyu.sdk.util.Logger;

import java.io.IOException;
import java.util.Arrays;

/**
 * @CLASS:HY_SplashBaseActivity
 * @AUTHOR:smile
 **/

public abstract class HY_SplashActivity extends Activity {

	private static final String TAG = "HY";
	private RelativeLayout mRel_lin;
	private ImageView imageView;
	private HY_SplashSequence sequence = new HY_SplashSequence();

	protected void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		// 全屏无标题
		HY_GameInit.initHYGameInfo(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		if(getIsLandscape()){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}else{
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		// 相对全布局
		this.mRel_lin = new RelativeLayout(this);
		// 设置背景颜色,回调给用户进行设置
		this.mRel_lin.setBackgroundColor(getBackgroundColor());
		this.mRel_lin.setVisibility(4);
		RelativeLayout.LayoutParams mRel_lin_params = new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		
		// 相对布局内的图片布局
		this.imageView = new ImageView(this);
		this.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		RelativeLayout.LayoutParams imageViewParams = new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

		this.mRel_lin.addView(this.imageView, imageViewParams);

		// 需要展示的闪屏图片保存到assets文件下assetDir下面
		String assetDir = "splash_photo";
		String[] assetPaths = new String[0];
		try {
			assetPaths = getAssets().list(assetDir);
		} catch (IOException e) {
			Logger.e(TAG, "load assets splash error" + e.toString());
		}
		Logger.d(TAG, "assetsPaths size " + assetPaths.length);
		int count = 0;
		// 对数组assetPaths进行排序，按照顺序显示
		Arrays.sort(assetPaths);
		for (String str : assetPaths) {
			Logger.d(TAG, "assets splash " + str);
		}

		// 资源文件前缀
		String resourcePrefix = "splash_photo";
		count = 0;
		while (true) {
			if (count < assetPaths.length) {
				this.sequence.addSplash(new HY_SplashAsset(this.mRel_lin,
						this.imageView, assetDir + "/" + assetPaths[count]));
			} else {
				int id = getResources().getIdentifier(resourcePrefix + count,
						"drawable", getPackageName());
				if (id == 0) {
					break;
				}
				Logger.d(TAG, "此代码不执行，因为闪屏图片默认都要求用户保存到assets下，并非drawable下");
			}
			count++;
		}

		setContentView(this.mRel_lin, mRel_lin_params);
	}

	protected void onResume() {
		super.onResume();
		Logger.d(TAG, "Splash--->onResume");

		this.sequence.play(this, new HY_SplashListener() {
			public void onFinish() {
				HY_SplashActivity.this.onSplashStop();
			}
		});
	}

	/**
	 * @description: 当闪屏PNG图片无法铺满部分机型的屏幕时，设置与闪屏颜色配合的背景色会给用户更好的体验
	 * @author:smile
	 * @return:int
	 * @return
	 */

	private String splash_color = "";

	public int getBackgroundColor() {
		// 从AndroidManifest中获取渠道颜色配置
		splash_color = HY_Utils.getManifestMeta(this, "HY_GAME_COLOR");
		if (!TextUtils.isEmpty(splash_color)) {
			return Color.parseColor(splash_color);
		} else {
			return Color.WHITE;
		}

	}
	
	/**
	 * @description:闪屏结束后，启动游戏的Activity
	 * @author:smile
	 * @return:void
	 */

	public abstract boolean getIsLandscape();

	/**
	 * @description:闪屏结束后，启动游戏的Activity
	 * @author:smile
	 * @return:void
	 */

	public abstract void onSplashStop();


}
