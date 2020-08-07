package com.huyu.sdk.listener;


import com.huyu.sdk.data.ResultCode;

public interface CallbackListener {
  void onResult(ResultCode resultCode, String msg, String data);
}