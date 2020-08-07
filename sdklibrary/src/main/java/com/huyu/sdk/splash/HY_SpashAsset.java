package com.huyu.sdk.splash;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import com.huyu.sdk.util.Logger;

import java.io.IOException;
import java.io.InputStream;

public class HY_SpashAsset extends HY_SplashBaseImage
{
    private String assetPath;

    public HY_SpashAsset(View layout, ImageView imageView, String assetPath)
    {
        super(layout, imageView);
        this.assetPath = assetPath;
    }

    @Override
    public void loadImage(final Activity context, final ImageView imageView,
            final HY_SplashBaseImage.LoadSplashCallback callback)
    {
        Logger.d(TAG, "start loadImage.. ");
        new AsyncTask()
        {

            @Override
            protected Object doInBackground(Object... params)
            {
                // TODO Auto-generated method stub
                Bitmap bitmap = null;
                try
                {
                    InputStream stream = context.getAssets().open(
                            HY_SpashAsset.this.assetPath);
                    bitmap = BitmapFactory.decodeStream(stream);
                }
                catch (IOException e)
                {
                    Logger.e(TAG, "load asset splash failed : "
                            + HY_SpashAsset.this.assetPath + e.toString());
                }

                return bitmap;
            }

            @Override
            protected void onPostExecute(Object result)
            {

                if (result == null)
                {
                    callback.onLoadFailed();
                }
                else
                {
                    imageView.setImageBitmap((Bitmap) result);
                    callback.onLoadSuccess();
                }
            }
        }.execute(new String[] { this.assetPath });
    }
}
