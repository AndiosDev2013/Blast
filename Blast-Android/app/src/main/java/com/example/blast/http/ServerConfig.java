package com.example.blast.http;

public class ServerConfig {
	
	public final static String HOST = "http://ec2-54-242-129-215.compute-1.amazonaws.com/drupal/";
	
	public static final int CONNECTION_TIMEOUT = 60;
	public static final String BUSY = "-1";
	public static final String NO_INTERNET = "-2";
	
	// response status
	public static final int STATUS_SUCCESS = 1;
	public static final int STATUS_FAIL = 0;
	
	/*
	 * server URLs for getting data
	 */
	public final static String URL_RECENT_VIDEOS = HOST + "blastapi/recent-videos";
	
	// user register / login / logout / reset password
	public final static String URL_REGISTER_EMAIL = HOST + "videoapi/newuser/register";
	public final static String URL_LOGIN_EMAIL = HOST + "videoapi/user/login";
	public final static String URL_LOGOUT = HOST + "videoapi/user/logout";
	public final static String URL_RESET_PASSWORD = HOST + "videoapi/user/$email$/password_reset";

	
	// channel list / info
	public final static String URL_CHANNEL_LIST = HOST + "videoapi/channel";
	public final static String URL_CHANNEL_INFO = HOST + "videoapi/taxonomy_term/channel_id";
	
	// video list / info
	public final static String URL_VIDEO_LIST_BY_CHANNEL_POST = HOST + "videoapi/channel/selectNodes/";
	public final static String URL_VIDEO_LIST_BY_CHANNEL_GET = HOST + "videoapi/videosbychannel?field_channel_tid=$channel_id$";
	public final static String URL_VIDEO_INFO = HOST + "videoapi/node/video_id";
	
	// Vote up / Vote video
	public final static String URL_GET_VOTE_STATUS = HOST + "videoapi/votingapi/select_votes";
	public final static String URL_VOTE_UP_VIDEO = HOST + "videoapi/votingapi/select_votes";
	public final static String URL_VOTE_DOWN_VIDEO = HOST + "videoapi/video/vote_down?video_id=$video_id$&user_id=$user_id$";
}
