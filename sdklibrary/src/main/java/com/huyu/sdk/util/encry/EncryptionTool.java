package com.huyu.sdk.util.encry;

public interface EncryptionTool {
  byte[] decry(byte[] paramArrayOfByte);
  
  byte[] encry(byte[] paramArrayOfByte);
}