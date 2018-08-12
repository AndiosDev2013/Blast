package com.example.blast.http;

import com.example.blast.http.HttpApi.ResponseModel;
import com.example.blast.model.ChannelModel;
import com.example.blast.model.RecentVideo;
import com.example.blast.model.UserModel.Register;
import com.example.blast.model.VideoModel;
import com.example.blast.model.UserModel.Login;
import com.google.gson.Gson;

public class Server {

	/*
	 * Login with Email
	 */
	public static class LoginParamInfo {
		String mail;
		String pass;
	}

	public static Object LoginWithEmail(String user_email, String password) {
		Gson gson = new Gson();

		LoginParamInfo info = new LoginParamInfo();
		info.mail = user_email;
		info.pass = password;
		String data = gson.toJson(info);

		ResponseModel resModel = HttpApi.sendPostRequest(ServerConfig.URL_LOGIN_EMAIL, data); 
		if (resModel.status == 200) {
			try {
				Login.Result result = gson.fromJson(resModel.response, Login.Result.class);
				return result;

			} catch (Exception e) {
				e.printStackTrace();
				resModel.response = "Json Parsing Error";
			}
		}

		return resModel.response;	
	}

	/*
	 * Register with Email
	 */
	public static class RegisterParamInfo {
		String mail;
		String pass;
		String status;
	}

	public static Object RegisterWithEmail(String user_email, String password) {
		Gson gson = new Gson();

		RegisterParamInfo info = new RegisterParamInfo();
		info.mail = user_email;
		info.pass = password;
		info.status = "1";

		String data = gson.toJson(info);

		ResponseModel resModel = HttpApi.sendPostRequest(ServerConfig.URL_REGISTER_EMAIL, data);
		if (resModel.status == 200) {
			try {
				Register.Result result = gson.fromJson(resModel.response, Register.Result.class);
				return result;

			} catch (Exception e) {
				e.printStackTrace();
				resModel.response = "Json Parsing Error";
			}
		}

		return resModel.response;
	}
	
	/*
	 * Reset Password
	 */
	public static Object ResetPassword(String user_email) {
		String url = ServerConfig.URL_RESET_PASSWORD;
		url = url.replace("$email$", user_email);
		
		ResponseModel resModel = HttpApi.sendPostRequest(url, "");
		if (resModel.status == 200) {
			try {

			} catch (Exception e) {
				e.printStackTrace();
				resModel.response = "Json Parsing Error";
			}
		}
		
		return resModel.response;
	}

	/*
	 * Get Channel List
	 */
	public static Object GetChannelList() {

		ResponseModel resModel = HttpApi.sendGetRequest(ServerConfig.URL_CHANNEL_LIST);

		if (resModel.status == 200) {
			try {
				resModel.response = "{result:" + resModel.response + "}";
				
				Gson gson = new Gson();
				ChannelModel.Result result = gson.fromJson(resModel.response, ChannelModel.Result.class);
				return result;

			} catch (Exception e) {
				e.printStackTrace();
				resModel.response = "Json Parsing Error";
			}
		}

		return resModel.response;		
	}

	/*
	 * Get Detail Information of One Channel 
	 */
	public static Object GetChannelInfo() {
		
		ResponseModel resModel = HttpApi.sendGetRequest(ServerConfig.URL_CHANNEL_INFO);

//		if (resModel.status == 200) {
//			try {
//				Gson gson = new Gson();
//				ChannelModel.InfoList result = gson.fromJson(resModel.response, ChannelModel.InfoList.class);
//				return result;
//
//			} catch (Exception e) {
//				e.printStackTrace();
//				resModel.response = "Json Parsing Error";
//			}
//		}

		return resModel.response;		
	}

	/*
	 * Get Video List by Channel
	 */
	
	public static Object GetVideoListByChannel(String channel_id) {
		String url = ServerConfig.URL_VIDEO_LIST_BY_CHANNEL_GET;
		url = url.replace("$channel_id$", channel_id);
		
		ResponseModel resModel = HttpApi.sendGetRequest(url);
		if (resModel.status == 200) {
			try {
				resModel.response = "{result:" + resModel.response + "}";
				
				Gson gson = new Gson();
				VideoModel.Result result = gson.fromJson(resModel.response, VideoModel.Result.class);
				return result;

			} catch (Exception e) {
				e.printStackTrace();
				resModel.response = "Json Parsing Error";
			}
		}

		return resModel.response;		
	}
	
	/*
	 * Get Vote Status
	 */
	public static class GetVoteParaInfo {
		public String type = "results";
		public static class Criteria {
			public String entity_id;
			public String entity_type = "node";
			public String tag = "vote";
		}
		public Criteria criteria = new Criteria();
	}
	
	public static Object GetVoteStatusOfVideo(String video_id) {
		video_id = "5";
		Gson gson = new Gson();
		
		GetVoteParaInfo info = new GetVoteParaInfo();
		info.criteria.entity_id = video_id;

		String data = gson.toJson(info);
		
		ResponseModel resModel = HttpApi.sendPostRequest(ServerConfig.URL_GET_VOTE_STATUS, data);
		if (resModel.status == 200) {
			try {
				VideoModel.Result result = gson.fromJson(resModel.response, VideoModel.Result.class);
				return result;

			} catch (Exception e) {
				e.printStackTrace();
				resModel.response = "Json Parsing Error";
			}
		}
		
		return resModel.response;
	}
	
	/*
	 * Vote UP Video
	 */
	private static class VoteUpParamInfo {
		public String type = "results";
		public static class Criteria {
			public String entity_id;
			public String entity_type = "node";
			public String tag = "vote";
		}
		public Criteria criteria;
	}
	
	public static Object VoteUpVideo(String video_id) {
		Gson gson = new Gson();
		
		VoteUpParamInfo info = new VoteUpParamInfo();
		info.criteria.entity_id = video_id;

		String data = gson.toJson(info);
		
		ResponseModel resModel = HttpApi.sendPostRequest(ServerConfig.URL_VOTE_UP_VIDEO, data);
		if (resModel.status == 200) {
			try {
				VideoModel.Result result = gson.fromJson(resModel.response, VideoModel.Result.class);
				return result;

			} catch (Exception e) {
				e.printStackTrace();
				resModel.response = "Json Parsing Error";
			}
		}
		
		return resModel.response;
	}

	/*
	 * Vote DOWN Video
	 */
	public static Object VoteDownVideo(String video_id) {
		String data = "";
		ResponseModel resModel = HttpApi.sendPostRequest(ServerConfig.URL_VOTE_DOWN_VIDEO, data);
		return resModel.response;
	}

	/*
	 * Get Recent Videos
	 */
	public static Object GetRecentVideos() {

		ResponseModel resModel = HttpApi.sendGetRequest(ServerConfig.URL_RECENT_VIDEOS);
		if (resModel.status == 200) {
			try {
				Gson gson = new Gson();
				RecentVideo.InfoList result = gson.fromJson(resModel.response, RecentVideo.InfoList.class);
				return result;

			} catch (Exception e) {
				e.printStackTrace();
				resModel.response = "Json Parsing Error";
			}
		}

		return resModel.response;		
	}
}
