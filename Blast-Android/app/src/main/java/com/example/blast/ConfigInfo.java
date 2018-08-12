package com.example.blast;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ConfigInfo {

	private final static String CONFIG_FILE_NAME = "mTracker_config";

	/** the preferences. */
	private static SharedPreferences mPreference;
	private static Editor mEditor;

	public static void initialize(Context context) {
		mPreference = context.getSharedPreferences(CONFIG_FILE_NAME, Context.MODE_PRIVATE);
	}
	
	public ConfigInfo(Context context) {
		mPreference = context.getSharedPreferences(CONFIG_FILE_NAME, Context.MODE_PRIVATE);
		mEditor = mPreference.edit();
	}

	public static void clear()  {
		mEditor = mPreference.edit();
		mEditor.clear();
		mEditor.commit();
	}

	public static void setFirstLaunchFlag(boolean bFlag) {
		mEditor = mPreference.edit();
		mEditor.putBoolean("first_launch_flag", bFlag);
		mEditor.commit();
	}
	
	public static boolean getFirstLaunchFlag() {
		return mPreference.getBoolean("first_launch_flag", true);
	}
	
	public static void setUserLoginMode(int type) {
		mEditor = mPreference.edit();
		mEditor.putInt("login_mode", type);
		mEditor.commit();
	}
	
	public static int getUserLoginMode() {
		return mPreference.getInt("login_mode", Constants.LOGIN_TYPE_UNKNOWN);
	}
	
	public static void setUserId(String user_id) {
		mEditor = mPreference.edit();
		mEditor.putString("user_id", user_id);
		mEditor.commit();
	}
	
	public static String getUserId() {
		return mPreference.getString("user_id", null);
	}
	
	public static void setUserName(String user_name) {
		mEditor = mPreference.edit();
		mEditor.putString("user_name", user_name);
		mEditor.commit();
	}
	
	public static String getUserName() {
		return mPreference.getString("user_name", null);
	}
	
	public static void setUserAvatarUrl(String user_name) {
		mEditor = mPreference.edit();
		mEditor.putString("user_avatar_url", user_name);
		mEditor.commit();
	}
	
	public static String getUserAvatarUrl() {
		return mPreference.getString("user_avatar_url", null);
	}
}