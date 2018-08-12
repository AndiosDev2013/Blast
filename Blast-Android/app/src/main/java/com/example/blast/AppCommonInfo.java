package com.example.blast;

import java.util.ArrayList;

import com.example.blast.model.VideoModel;

public class AppCommonInfo {
	
	/*
	 * App is initialized?
	 */
	public static boolean App_Is_Initialzed = false;
	
	/*
	 * Current Channel type
	 */
	public static int Current_Channel_type = Constants.CHANNEL_TYPE_LAND;
	
	/*
	 * Video's Information
	 */
	
	public static ArrayList<VideoModel.DetailInfo> VideoList = new ArrayList<VideoModel.DetailInfo>();
}
