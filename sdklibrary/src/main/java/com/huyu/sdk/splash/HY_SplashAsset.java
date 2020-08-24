package com.huyu.sdk.splash;

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import com.huyu.sdk.util.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

public class HY_SplashAsset extends HY_SplashBaseImage {
    private String assetPath;

    public HY_SplashAsset(View layout, ImageView imageView, String assetPath) {
        super(layout, imageView);
        this.assetPath = assetPath;
    }

    @Override
    public void loadImage(final Activity context, final ImageView imageView,
                          final HY_SplashBaseImage.LoadSplashCallback callback) {
        Logger.d(TAG, "start loadImage..  assetPath : " + assetPath);
        new LoadImageTask(context, imageView, callback).execute(assetPath);
    }

    private static final class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        WeakReference<Application> applicationWeakReference;
        WeakReference<ImageView> imageViewWeakReference;
        LoadSplashCallback callback;

        public LoadImageTask(Activity context, ImageView imageView, LoadSplashCallback callback) {
            this.applicationWeakReference = new WeakReference<>(context.getApplication());
            this.imageViewWeakReference = new WeakReference<>(imageView);
            this.callback = callback;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            if (params == null || params.length == 0) return null;
            Bitmap bitmap = null;
            try {
                InputStream stream = applicationWeakReference.get().getAssets().open(params[0]);
                bitmap = BitmapFactory.decodeStream(stream);
            } catch (IOException e) {
                Logger.e(TAG, "load asset splash failed : "
                        + params[0] + e.toString());
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result == null) {
                callback.onLoadFailed();
            } else {
                imageViewWeakReference.get().setImageBitmap(result);
                callback.onLoadSuccess();
            }
        }
    }
}
