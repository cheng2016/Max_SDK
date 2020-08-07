package com.huyu.sdk.splash;

import android.app.Activity;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.huyu.sdk.util.Logger;

public abstract class HY_SplashBaseImage implements HY_ISplash {
	protected static final String TAG = "HY";
	private View layout;
	private ImageView imageView;
	private HY_SplashListener listener;

	public HY_SplashBaseImage(View layout, ImageView imageView) {
		this.layout = layout;
		this.imageView = imageView;
	}

	abstract void loadImage(Activity paramActivity, ImageView paramImageView,
			LoadSplashCallback paramLoadSplashCallback);

	public void play(final Activity context, final HY_SplashListener listener,
			final int index, final int length) {
		loadImage(context, this.imageView, new LoadSplashCallback() {
			public void onLoadSuccess() {
				HY_SplashBaseImage.this.playSplash(context, listener, index,
						length);
			}

			public void onLoadFailed() {
				listener.onFinish();
			}
		});
	}

	public void playSplash(Activity context, final HY_SplashListener listener,
			int index, int length) {
		this.listener = listener;
		Logger.d("index:" + index);
		Logger.d("length:" + length);
		if (index < length - 1) {
			Animation animation = getAnimation();
			animation.setAnimationListener(new Animation.AnimationListener() {
				public void onAnimationStart(Animation paramAnimation) {
					Logger.d(TAG, "animation start");
				}

				public void onAnimationRepeat(Animation paramAnimation) {
					Logger.d(TAG, "animation repeat");
				}

				public void onAnimationEnd(Animation paramAnimation) {
					Logger.d(TAG, "animation end");
					HY_SplashBaseImage.this.layout
							.setVisibility(View.INVISIBLE);
					listener.onFinish();
				}
			});
			Logger.d(TAG, "start animat ion");
			this.layout.startAnimation(animation);
			this.layout.setVisibility(0);
		} else {
			Logger.d("最后一张闪屏");
			layout.setVisibility(0);
			MyCountDownTimer myCountDownTimer = new MyCountDownTimer(1500, 1000);
			myCountDownTimer.start();
		}
	}

	private Animation getAnimation() {
		Animation fadeIn = new AlphaAnimation(0.0F, 1.0F);
		fadeIn.setInterpolator(new DecelerateInterpolator());
		fadeIn.setDuration(500L);

		Animation fadeOut = new AlphaAnimation(1.0F, 0.0F);
		fadeOut.setInterpolator(new AccelerateInterpolator());
		fadeOut.setStartOffset(1500L);
		fadeOut.setDuration(500L);

		AnimationSet animation = new AnimationSet(false);
		animation.addAnimation(fadeIn);
		animation.addAnimation(fadeOut);

		return animation;
	}

	public static abstract interface LoadSplashCallback {
		public abstract void onLoadSuccess();

		public abstract void onLoadFailed();
	}

	// 复写倒计时
	private class MyCountDownTimer extends CountDownTimer {

		public MyCountDownTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		// 计时过程
		@Override
		public void onTick(long l) {
			// 防止计时过程中重复点击
			// mInitViewTv.setText("正在初始化..."+l / 1000 + " s");
		}

		// 计时完毕的方法
		@Override
		public void onFinish() {
			Logger.d("播放结束");
			HY_SplashBaseImage.this.layout.setVisibility(View.INVISIBLE);
			listener.onFinish();
		}
	}
}
