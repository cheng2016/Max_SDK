package com.huyu.sdk.view;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.widget.ImageView;

import com.huyu.sdk.util.ResourceHelper;

public class LoadingBar extends Dialog {
  ObjectAnimator animator;

  public LoadingBar(Context context) {
    super(context, ResourceHelper.getStyleId(context,"base_pop"));
    setContentView(ResourceHelper.getLayoutId(context,"loading"));

    this.animator = ObjectAnimator.ofFloat((ImageView)findViewById(ResourceHelper.getId(context,"progressBar")), "rotation", new float[] { 0.0F, 360.0F });
    this.animator.setDuration(500L);
    this.animator.setRepeatCount(-1);
    this.animator.start();
  }
  
  public void cancel() {
    super.cancel();
    this.animator = null;
  }

  @Override
  public void dismiss() {
    super.dismiss();
    this.animator = null;
  }

  public void show() {
    super.show();
    setCanceledOnTouchOutside(false);
  }
}