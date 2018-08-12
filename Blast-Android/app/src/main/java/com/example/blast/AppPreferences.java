package com.example.blast;

import android.content.SharedPreferences;

public class AppPreferences {
	private static SharedPreferences instance = null;
	
	public static class KEY {
		// sign in
		public static final String LOGIN_MODE = "LOGIN_MODE";
		public static final String USER_ID = "USER_ID";
		public static final String USER_AVATAR_URL = "USER_AVATAR_URL";
        //
	}
	
	public static void initialize(SharedPreferences pref) {
		instance = pref;
	}
	
	// check contain
	public static boolean contains(String key) {
		return instance.contains(key);
	}
	
	// boolean
	public static boolean getBool(String key, boolean def) {
		return instance.getBoolean(key, def);
	}
	public static void setBool(String key, boolean value) {
		SharedPreferences.Editor editor = instance.edit();
		editor.putBoolean(key, value);
		editor.apply();
	}
	
	// int
	public static int getInt(String key, int def) {
		return instance.getInt(key, def);
	}
	public static void setInt(String key, int value) {
		SharedPreferences.Editor editor = instance.edit();
		editor.putInt(key, value);
		editor.apply();
	}
	
	// long
	public static long getLong(String key, long def) {
		return instance.getLong(key, def);
	}
	public static void setLong(String key, long value) {
		SharedPreferences.Editor editor = instance.edit();
		editor.putLong(key, value);
		editor.apply();
	}
	
	// string
	public static String getStr(String key, String def) {
		return instance.getString(key, def);
	}
	public static void setStr(String key, String value) {
		SharedPreferences.Editor editor = instance.edit();
		editor.putString(key, value);
		editor.apply();
	}

	// remove
	public static void removeKey(String key) {
		SharedPreferences.Editor editor = instance.edit();
		editor.remove(key);
		editor.apply();
	}
}
