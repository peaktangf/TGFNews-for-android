package com.project.tangaofeng.actionbar_demo;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 处理一些公用的全局操作
 *
 */
public class BaseApplication extends Application {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		initImageLoader(this);
	}

	 public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
		 DisplayImageOptions options = new DisplayImageOptions.Builder()
                     .showImageOnLoading(R.mipmap.test)
                     .showImageOnFail(R.mipmap.test)
                     .cacheInMemory(true)
                     .cacheOnDisk(true)
                     .bitmapConfig(Bitmap.Config.RGB_565)
                     .build();

		 ImageLoaderConfiguration config = ImageLoaderConfiguration
                     .createDefault(context);
		 // Initialize ImageLoader with configuration.
		 ImageLoader.getInstance().init(config);
	 }

	 public static void setImageViewUrl(final ImageView imageView, String url) {
        ImageLoader.getInstance().handleSlowNetwork(true);
        ImageLoader.getInstance().displayImage(url + "", imageView);
	 }

}
