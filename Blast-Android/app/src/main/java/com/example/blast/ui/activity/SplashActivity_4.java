package com.example.blast.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.example.blast.AppConstants;
import com.example.blast.R;

public class SplashActivity_4 extends Activity{

	private static String TAG = SplashActivity_0.class.getName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);    // Removes title bar
		setContentView(R.layout.layout_splash_4);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		// Start timer and launch main activity
		IntentLauncher launcher = new IntentLauncher();
		launcher.start();
	}

	private class IntentLauncher extends Thread {

		/**
		 * Sleep for some time and than start new activity.
		 */ 
		@Override
		public void run() {
			try {
				// Sleeping
				Thread.sleep(AppConstants.SPLASH4_SLEEP_TIME*1000);
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
			}

			// Start main activity
			Intent intent = new Intent(SplashActivity_4.this, MainActivity.class);
			SplashActivity_4.this.startActivity(intent);
			SplashActivity_4.this.finish();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}
}
