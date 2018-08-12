package com.example.blast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.example.blast.utils.Validation;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class BlastApp extends Application {
	private Intent mIntent = null;
	private static BlastApp instance;

	public final Intent getIntent() {
		if (mIntent == null)
			mIntent = new Intent();
		return mIntent;
	}

	public final void setIntent(Intent paramIntent) {
		mIntent = paramIntent;
	}

	public BlastApp() {
		instance = this;
	}

	public static BlastApp getInstance() {
		return instance;
	}

	public static Context getContext() {
		return getInstance();
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


	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;

		// checkSignatures();
		
		// initialize Configuration Manager
		ConfigInfo.initialize(instance.getApplicationContext());

		// initialize Validation
		Validation.initialize(instance.getApplicationContext());

		// initialize Image Loader
		initImageLoader(instance.getApplicationContext());
		new myImageLoader();
		myImageLoader.init();
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

	@Override
	public void onTerminate() {
		
		super.onTerminate();
	}
}