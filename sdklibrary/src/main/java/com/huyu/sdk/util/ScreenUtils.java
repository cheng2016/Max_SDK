/**
 * Copyright (c) 2015-2018, smithJiang  (www.devstore.cn).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.huyu.sdk.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 屏幕坐标工具类
 * 
 * @author smithJiang
 * @version 1.0
 * @date 2015年6月29日11:18:01
 * @since java 6
 * @description
 */
public class ScreenUtils {
	private static final String TAG = ScreenUtils.class.getSimpleName();
	private static ScreenUtils mScreenTools = null;


	private ScreenUtils(Context context) {

	}
	public static ScreenUtils getInstance(Context context) {
		if (mScreenTools == null) {
			synchronized (ScreenUtils.class) {
				if (mScreenTools == null) {
					mScreenTools = new ScreenUtils(context);
				}
			}
		}
		return mScreenTools;
	}


	/** 视图转成图片 */
	public Bitmap viewToBitmap(View view) {

		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.buildDrawingCache();
		Bitmap newb = view.getDrawingCache();

		Bitmap b = Bitmap.createBitmap(newb, 0, 0, view.getMeasuredWidth(),
				view.getMeasuredHeight());
		newb.recycle();
		newb = null;
		return b;
	}

	public File bitmapToFile(Bitmap bitmap, String filePath, int quality) {
		if (bitmap == null) {
			return null;
		}
		File imageFile = new File(filePath);
		if (!imageFile.getParentFile().exists()) {
			imageFile.getParentFile().mkdirs();
		}
		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream(imageFile);
			boolean b = bitmap.compress(Bitmap.CompressFormat.JPEG, quality,
					fout);
			if (b) {
				return imageFile;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fout != null) {
				try {
					fout.close();
					fout = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public void bitmapToFile(Bitmap bitmap, int quality, File imageFile) {
		if (bitmap == null) {
			return;
		}
		if (!imageFile.getParentFile().exists()) {
			imageFile.getParentFile().mkdirs();
		}
		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream(imageFile);
			bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fout);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fout != null) {
				try {
					fout.close();
					fout = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 保存一键登录截图
	 * 
	 * @param context
	 * @param view
	 */
	/*public void saveScreenshot(Context context, View view) {
		try {
			view.setDrawingCacheEnabled(true);
			view.buildDrawingCache();
			// 上面2行必须加入，如果不加如view.getDrawingCache()返回null
			Bitmap bitmap = view.getDrawingCache();
			String fileName = PluginConstants.SCREEN_SHOT + "_"
					+ DateUtil.getFormatMilisDate() + ".jpg";
			File file = new File(PluginConstants.GAME_DIR, fileName);
			ScreenTools.getInstance(context).bitmapToFile(bitmap, 100, file);
			HY_ToastUtils.show(context, "您的账号和密码的截图已经保存到手机图库");
			noticeGallery(context, fileName, file);
		} catch (Exception e) {
		}
	}*/


    /** SD卡根目录 */
    public static final String SDCARD_DIR = android.os.Environment
            .getExternalStorageDirectory().getPath();
    /** 提供外部访问页游SDK目录路径 */
    public static final String GAME_DIR = SDCARD_DIR + File.separator + "DCIM"
            + File.separator + "camera";
    public static final String SCREEN_SHOT = "Screenshot";
	/**
	 * 保存一键登录截图
	 * 
	 * @param
	 * @param view
	 */
	public void saveOneKeyLoginScreenshot(Context context, View view) {
		try {
			Bitmap bmp = ScreenUtils.getInstance(context).viewToBitmap(view);
			String fileName = SCREEN_SHOT + "_"
					+ DateUtils.getFormatMilisDate() + ".jpg";
			File file = new File(GAME_DIR, fileName);
			ScreenUtils.getInstance(context).bitmapToFile(bmp, 100, file);
			ToastUtils.show(context, "Screenshot successful", Toast.LENGTH_LONG);
			noticeGallery(context, fileName, file);
		} catch (Exception e) {
		}
	}






	/**
	 * 最后通知图库更新
	 * 
	 * @param context
	 * @param fileName
	 * @param file
	 * @throws FileNotFoundException
	 */
	private void noticeGallery(Context context, String fileName, File file) {
		if (file != null) {
			File rootFile = file.getParentFile();
			if (!rootFile.exists()) {
				rootFile.mkdirs();
			}
		}
		try {
			MediaStore.Images.Media.insertImage(context.getContentResolver(),
					file.getAbsolutePath(), fileName, null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		Uri uri = Uri.fromFile(file);
		intent.setData(uri);
		context.sendBroadcast(intent);
	}




	/**
	 * 图片合成
	 * 
	 * *//*
	public Bitmap compoundsBitmap(Bitmap bitmap1Main, Bitmap bitmap2RedCircle,
			Bitmap bitmap3Text, Bitmap bitmap4Arrow, int[] locationCircle) {
		Bitmap bitmap0 = Bitmap.createBitmap(bitmap1Main.getWidth(),
				bitmap1Main.getHeight(), Config.RGB_565);
		Canvas canvas = new Canvas(bitmap0);
		// 主体页面
		canvas.drawBitmap(bitmap1Main, new Matrix(), null);
		// 加入批注红圈 ，location为bitmap2写入点的x、y坐标
		canvas.drawBitmap(bitmap2RedCircle, locationCircle[0]
				- bitmap2RedCircle.getWidth() / 2, locationCircle[1]
				- bitmap2RedCircle.getHeight() / 2, null);

		float topText = 0;
		float left = locationCircle[0] - bitmap4Arrow.getWidth() / 2;
		float padding = 5;// 距离屏幕左边、上边界至少5像素
		boolean isBottom = false;
		// 计算三角形位置：如果红圈在屏幕上半部分，则三角形、文字在红圈下面
		float top = locationCircle[1] + bitmap2RedCircle.getHeight() / 2;
		topText = top + bitmap4Arrow.getHeight();
		// 如果红圈在屏幕下半部分，重新计算位置，三角形、文字在红圈上面
		if (locationCircle[1] > getScreenHeight() / 2) {
			isBottom = true;
			top = locationCircle[1] - bitmap2RedCircle.getHeight() / 2
					- bitmap4Arrow.getHeight();
			topText = top - bitmap3Text.getHeight();

			if (topText < padding) {
				topText = padding;
			}
		}
		// 三角形要翻转方向
		if (isBottom) {
			Matrix matrix = new Matrix();
			matrix.setRotate(180, bitmap4Arrow.getWidth() / 2,
					bitmap4Arrow.getHeight() / 2);
			bitmap4Arrow = Bitmap.createBitmap(bitmap4Arrow, 0, 0,
					bitmap4Arrow.getWidth(), bitmap4Arrow.getHeight(), matrix,
					false);
		}

		// 加入三角形图片
		canvas.drawBitmap(bitmap4Arrow, left, top, null);
		// 计算文字位置
		left = locationCircle[0] - bitmap3Text.getWidth() / 2;
		if (left < 0) {
			left = 0;
		}
		// 如果超出右边边界，则重新计算左边界
		if (left + bitmap3Text.getWidth() > getScreenWidth()) {
			left = getScreenWidth() - bitmap3Text.getWidth();
		}

		// 加入文字图片
		canvas.drawBitmap(bitmap3Text, left, topText, null);

		// 回收资源
		bitmap1Main.recycle();
		bitmap1Main = null;

		bitmap2RedCircle.recycle();
		bitmap2RedCircle = null;

		bitmap3Text.recycle();
		bitmap3Text = null;

		bitmap4Arrow.recycle();
		bitmap4Arrow = null;

		return bitmap0;
	}*/

	/**
	 * 以指定位置为中心截取图片
	 */
	public static Bitmap cropImage(Bitmap bitmap, int[] location, int[] size) {
		int nw = size[0];// 裁切后所取的正方形区域边长
		int nh = size[1];// 裁切后所取的正方形区域边长

		int x = location[0] - nw / 2;
		int y = location[1] - nh / 2;
		// 偏左纠正
		if (x < 0) {
			x = 0;
		}
		// 偏上纠正
		if (y < 0) {
			y = 0;
		}
		int bitmapW = bitmap.getWidth();
		int bitmapH = bitmap.getHeight();

		// 偏右纠正
		if (x + nw > bitmapW) {
			x = bitmapW - nw;
		}
		// 偏下纠正
		if (y + nh > bitmapH) {
			y = bitmapH - nh;
		}

		// 下面这句是关键
		return Bitmap.createBitmap(bitmap, x, y, nw, nh, null, false);
	}



}