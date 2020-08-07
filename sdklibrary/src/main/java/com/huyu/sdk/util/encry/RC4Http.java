package com.huyu.sdk.util.encry;

public class RC4Http implements EncryptionTool {
  public static byte[] key;
  
  private static String shareKey = "cf79b00806591e4f8bfd411ef334a948";
  
  public static byte[] RC4Base(byte[] input, int start, int end) {
	int x = 0;
	int y = 0;
	byte key[] = initKey(shareKey);
	int xorIndex;
	for (int i = start; i < end; i++) {
		x = (x + 1) & 0xff;
		y = ((key[x] & 0xff) + y) & 0xff;
		byte tmp = key[x];
		key[x] = key[y];
		key[y] = tmp;
		xorIndex = ((key[x] & 0xff) + (key[y] & 0xff)) & 0xff;
		input[i] = (byte) (input[i] ^ key[xorIndex]);
	}
	return input;
  }
  
  public static byte[] copyOf(byte[] paramArrayOfByte, int paramInt) {
    byte[] arrayOfByte = new byte[paramInt];
    System.arraycopy(paramArrayOfByte, 0, arrayOfByte, 0, Math.min(paramArrayOfByte.length, paramInt));
    return arrayOfByte;
  }
  
  private static byte[] initKey(String aKey) {
	if (key == null) {
		byte[] b_key = aKey.getBytes();
		key = new byte[256];

		for (int i = 0; i < 256; i++) {
			key[i] = (byte) i;
		}

		int index1 = 0;
		int index2 = 0;
		if (b_key == null || b_key.length == 0) {
			return null;
		}
		for (int i = 0; i < 256; i++) {
			index2 = ((b_key[index1] & 0xff) + (key[i] & 0xff) + index2) & 0xff;
			byte tmp = key[i];
			key[i] = key[index2];
			key[index2] = tmp;
			index1 = (index1 + 1) % b_key.length;
		}
	}

	return copyOf(key, key.length);
  }
  
  public byte[] decry(byte[] paramArrayOfByte) { return RC4Base(paramArrayOfByte, 0, paramArrayOfByte.length); }
  
  public byte[] encry(byte[] paramArrayOfByte) { return RC4Base(paramArrayOfByte, 0, paramArrayOfByte.length); }
}


/* Location:              C:\Users\mitni\Desktop\gitwork\AndroidTool\classes-dex2jar.jar!\com\icloud\sd\\utils\encry\RC4Http.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.6
 */
