package com.huyu.sdk.util;

import android.os.CountDownTimer;
import android.widget.Button;

import com.huyu.sdk.R;


public class TimeThread extends CountDownTimer {
  private Button timeButton;
  
  public TimeThread(Button paramButton, long paramLong1, long paramLong2) {
    super(paramLong1, paramLong2);
    this.timeButton = paramButton;
  }
  
  public void onFinish() {
    this.timeButton.setText("重新获取");
    this.timeButton.setClickable(true);
    this.timeButton.setBackgroundResource(R.drawable.hy_bg_003);
    this.timeButton.setPadding(0, 0, 0, 0);
  }
  
  public void onTick(long paramLong) {
    this.timeButton.setClickable(false);
    this.timeButton.setBackgroundResource(R.drawable.hy_bg_004);
    this.timeButton.setPadding(0, 0, 0, 0);
    this.timeButton.setText((paramLong / 1000L) + "秒后可重新获取");
  }
}


/* Location:              C:\Users\mitni\Desktop\gitwork\AndroidTool\classes-dex2jar.jar!\com\icloud\sd\\utils\TimeThread.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.6
 */