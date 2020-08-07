package com.huyu.sdk.util;

import java.security.MessageDigest;

public class MD5Utils {
  private static final String[] hexDigits = {
          "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
          "a", "b", "c", "d", "e", "f" };

	public static String MD5(String origin) {
		return MD5Encode(origin,"");
	}

	public static String MD5Encode(String origin, String charsetname) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (charsetname == null || "".equals(charsetname))
				resultString = byteArrayToHexString(md.digest(resultString
						.getBytes()));
			else
				resultString = byteArrayToHexString(md.digest(resultString
						.getBytes(charsetname)));
		} catch (Exception exception) {
		}
		return resultString;
	}

  private static String byteArrayToHexString(byte[] paramArrayOfByte) {
    StringBuffer stringBuffer = new StringBuffer();
    for (byte b = 0; b < paramArrayOfByte.length; b++)
      stringBuffer.append(byteToHexString(paramArrayOfByte[b]));
    return stringBuffer.toString();
  }

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n += 256;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}
}
