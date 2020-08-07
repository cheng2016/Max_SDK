package com.huyu.sdk.util.encry;

import java.util.Formatter;

public class TypeConvert {
  public static int MAX_HEX_LENGTH = 200;
  
  private byte btmp;
  
  public static byte[] hexStr2ByteArr(String paramString) {
    byte[] arrayOfByte1 = paramString.getBytes();
    int i = arrayOfByte1.length;
    byte[] arrayOfByte2 = new byte[i / 2];
    for (byte b = 0; b < i; b += 2) {
      String str = new String(arrayOfByte1, b, 2);
      arrayOfByte2[b / 2] = (byte)Integer.parseInt(str, 16);
    } 
    return arrayOfByte2;
  }
  
  public static String toHexString(byte paramByte) { return new String(new char[] { Character.forDigit(paramByte >>> 4 & 0xF, 16), Character.forDigit(paramByte & 0xF, 16) }); }
  
  public static String toHexString(byte[] paramArrayOfByte) {
    StringBuffer stringBuffer = new StringBuffer();
    for (byte b = 0; b < paramArrayOfByte.length; b++)
      stringBuffer.append(toHexString(paramArrayOfByte[b])); 
    return stringBuffer.toString();
  }
  
  public int byte2int(byte[] paramArrayOfByte) { return paramArrayOfByte[3] & 0xFF | (paramArrayOfByte[2] & 0xFF) << 8 | (paramArrayOfByte[1] & 0xFF) << 16 | (paramArrayOfByte[0] & 0xFF) << 24; }
  
  public int byte2int(byte[] paramArrayOfByte, int paramInt) { return paramArrayOfByte[paramInt + 3] & 0xFF | (paramArrayOfByte[paramInt + 2] & 0xFF) << 8 | (paramArrayOfByte[paramInt + 1] & 0xFF) << 16 | (paramArrayOfByte[paramInt] & 0xFF) << 24; }
  
  public long byte2long(byte[] paramArrayOfByte) { return paramArrayOfByte[7] & 0xFFL | (paramArrayOfByte[6] & 0xFFL) << 8 | (paramArrayOfByte[5] & 0xFFL) << 16 | (paramArrayOfByte[4] & 0xFFL) << 24 | (paramArrayOfByte[3] & 0xFFL) << 32 | (paramArrayOfByte[2] & 0xFFL) << 40 | (paramArrayOfByte[1] & 0xFFL) << 48 | paramArrayOfByte[0] << 56; }
  
  public long byte2long(byte[] paramArrayOfByte, int paramInt) { return paramArrayOfByte[paramInt + 7] & 0xFFL | (paramArrayOfByte[paramInt + 6] & 0xFFL) << 8 | (paramArrayOfByte[paramInt + 5] & 0xFFL) << 16 | (paramArrayOfByte[paramInt + 4] & 0xFFL) << 24 | (paramArrayOfByte[paramInt + 3] & 0xFFL) << 32 | (paramArrayOfByte[paramInt + 2] & 0xFFL) << 40 | (paramArrayOfByte[paramInt + 1] & 0xFFL) << 48 | paramArrayOfByte[paramInt] << 56; }
  
  public short byte2short(byte[] paramArrayOfByte) { return (short)(paramArrayOfByte[1] & 0xFF | (paramArrayOfByte[0] & 0xFF) << 8); }
  
  public short byte2short(byte[] paramArrayOfByte, int paramInt) { return (short)(paramArrayOfByte[paramInt + 1] & 0xFF | (paramArrayOfByte[paramInt + 0] & 0xFF) << 8); }
  
  public int byte2short2int(byte[] paramArrayOfByte, int paramInt) { return paramArrayOfByte[paramInt + 1] & 0xFF | (paramArrayOfByte[paramInt + 0] & 0xFF) << 8; }
  
  public void int2byte(int paramInt1, byte[] paramArrayOfByte, int paramInt2) {
    paramArrayOfByte[paramInt2] = (byte)(paramInt1 >> 24);
    paramArrayOfByte[paramInt2 + 1] = (byte)(paramInt1 >> 16);
    paramArrayOfByte[paramInt2 + 2] = (byte)(paramInt1 >> 8);
    paramArrayOfByte[paramInt2 + 3] = (byte)paramInt1;
  }
  
  public byte[] int2byte(int paramInt) { return new byte[] { (byte)(paramInt >> 24), (byte)(paramInt >> 16), (byte)(paramInt >> 8), (byte)paramInt }; }
  
  public void long2byte(long paramLong, byte[] paramArrayOfByte, int paramInt) {
    paramArrayOfByte[paramInt] = (byte)(int)(paramLong >> 56);
    paramArrayOfByte[paramInt + 1] = (byte)(int)(paramLong >> 48);
    paramArrayOfByte[paramInt + 2] = (byte)(int)(paramLong >> 40);
    paramArrayOfByte[paramInt + 3] = (byte)(int)(paramLong >> 32);
    paramArrayOfByte[paramInt + 4] = (byte)(int)(paramLong >> 24);
    paramArrayOfByte[paramInt + 5] = (byte)(int)(paramLong >> 16);
    paramArrayOfByte[paramInt + 6] = (byte)(int)(paramLong >> 8);
    paramArrayOfByte[paramInt + 7] = (byte)(int)paramLong;
  }
  
  public byte[] long2byte(long paramLong) { return new byte[] { (byte)(int)(paramLong >> 56), (byte)(int)(paramLong >> 48), (byte)(int)(paramLong >> 40), (byte)(int)(paramLong >> 32), (byte)(int)(paramLong >> 24), (byte)(int)(paramLong >> 16), (byte)(int)(paramLong >> 8), (byte)(int)paramLong }; }
  
  public void reverse2Byte(byte[] paramArrayOfByte, int paramInt) {
    this.btmp = paramArrayOfByte[paramInt + 0];
    paramArrayOfByte[paramInt + 0] = paramArrayOfByte[paramInt + 1];
    paramArrayOfByte[paramInt + 1] = this.btmp;
  }
  
  public void reverse4Byte(byte[] paramArrayOfByte, int paramInt) {
    this.btmp = paramArrayOfByte[paramInt + 0];
    paramArrayOfByte[paramInt + 0] = paramArrayOfByte[paramInt + 3];
    paramArrayOfByte[paramInt + 3] = this.btmp;
    this.btmp = paramArrayOfByte[paramInt + 1];
    paramArrayOfByte[paramInt + 1] = paramArrayOfByte[paramInt + 2];
    paramArrayOfByte[paramInt + 2] = this.btmp;
  }
  
  public void reverse8Byte(byte[] paramArrayOfByte, int paramInt) {
    this.btmp = paramArrayOfByte[paramInt + 0];
    paramArrayOfByte[paramInt + 0] = paramArrayOfByte[paramInt + 7];
    paramArrayOfByte[paramInt + 7] = this.btmp;
    this.btmp = paramArrayOfByte[paramInt + 1];
    paramArrayOfByte[paramInt + 1] = paramArrayOfByte[paramInt + 6];
    paramArrayOfByte[paramInt + 6] = this.btmp;
    this.btmp = paramArrayOfByte[paramInt + 2];
    paramArrayOfByte[paramInt + 2] = paramArrayOfByte[paramInt + 5];
    paramArrayOfByte[paramInt + 5] = this.btmp;
    this.btmp = paramArrayOfByte[paramInt + 3];
    paramArrayOfByte[paramInt + 3] = paramArrayOfByte[paramInt + 4];
    paramArrayOfByte[paramInt + 4] = this.btmp;
  }
  
  public void short2byte(int paramInt1, byte[] paramArrayOfByte, int paramInt2) {
    paramArrayOfByte[paramInt2] = (byte)(paramInt1 >> 8);
    paramArrayOfByte[paramInt2 + 1] = (byte)paramInt1;
  }
  
  public byte[] short2byte(int paramInt) { return new byte[] { (byte)(paramInt >> 8), (byte)paramInt }; }
  
  public String toHexString(byte[] b, int offset, int length) {
    StringBuffer buffer = new StringBuffer();
    length += offset;
    int max = 0;
    for (int i = offset; i < length; ++i) {
      max++;
      if (max >= MAX_HEX_LENGTH) {
        break;
      }
      buffer.append(toHexString(b[i]));
    }
    return buffer.toString() + (max >= MAX_HEX_LENGTH ? "..." : "");
  }

  public String toHexString2(byte[] b, int offset, int length) {
    StringBuffer buffer = new StringBuffer();
    Formatter formatter = new Formatter(buffer);
    length += offset;
    int max = 0;
    for (int i = offset; i < length; ++i) {
      max++;
      if (max >= MAX_HEX_LENGTH) {
        break;
      }
      formatter.format("%02x", b[i]);
    }
    return buffer.toString() + (max >= MAX_HEX_LENGTH ? "..." : "");
  }
}
