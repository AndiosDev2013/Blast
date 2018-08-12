package com.example.blast;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.example.blast.utils.Validation;
import com.example.blast.utils.myImageLoader;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class BlastApp extends Application {
	public static Context mContext;
	public static String mPackageName;

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = this.getApplicationContext();

		// checkSignatures();
		
		// initialize Configuration Manager
		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		AppGlobals.SCREEN_WIDTH = display.getWidth();
		AppGlobals.SCREEN_HEIGHT = display.getHeight();

		// initialize Validation
		Validation.initialize(mContext);

		// initialize Image Loader
		initImageLoader(mContext);
		new myImageLoader();
		myImageLoader.init();
	}

	public void checkSignatures() {
		// Add code to print out the key hash
		try {
			PackageInfo info = getPackageManager().getPackageInfo(
					"com.example.blast", PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());

				String hashkey = Base64.encodeToString(md.digest(), Base64.DEFAULT);
				Log.d("KeyHash:", hashkey);
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	private void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
		.threadPriority(Thread.NORM_PRIORITY - 2)
		.denyCacheImageMultipleSizesInMemory()
		.discCacheFileNameGenerator(new Md5FileNameGenerator())
		.tasksProcessingOrder(QueueProcessingType.LIFO)
		.writeDebugLogs() // Remove for release app
		.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
}