package com.example.blast.utils;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.blast.http.HttpApi;
import com.example.blast.http.HttpApi.ResponseModel;

import android.text.TextUtils;

public class YoutubeExtractor {
	/*
	 * Attempt Type
	 */
	private static int mAttemptType = 0;
	@SuppressWarnings("rawtypes")
	public static Map video_url_map = new HashMap();

	public static class AttemptType {
		public static final int Embedded = 0;
		public static final int DetailPage = 1;
		public static final int Vevo = 2;
		public static final int Blank = 3;
		public static final int Error = 4;
	}

	/*
	 * Video Quality
	 */
	public static class VideoQuality {
		public static final String Small240 = "36";
		public static final String Medium360 = "18";
		public static final String HD720 = "22";
		public static final String HD1080 = "37";
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void extractVideobyId(String video_id) throws MalformedURLException {
		if (mAttemptType == AttemptType.Error) {
			mAttemptType = AttemptType.Embedded;
			return;
		}

		Map parameters = new HashMap();
		switch (mAttemptType) {
		case AttemptType.Embedded:
			parameters.put("el", "embedded");
			break;

		case AttemptType.DetailPage:
			parameters.put("el", "detailpage");
			break;

		case AttemptType.Vevo:
			parameters.put("el", "vevo");
			break;

		case AttemptType.Blank:
			parameters.put("el", "");
			break;

		default:
			break;
		}

		parameters.put("video_id", video_id);
		parameters.put("ps", "default");
		parameters.put("eurl", "");
		parameters.put("gl", "US");
		parameters.put("hl", "en");

		String urlString = addQueryStringToUrlString("https://www.youtube.com/get_video_info", parameters);
		ResponseModel resModel = HttpApi.sendGetRequest(urlString);
		if (resModel.status != 200) {
			return;
		}

		Map video_map = getMapFromString(resModel.response, "UTF-8");
		
		String stream_query = (String)video_map.get("url_encoded_fmt_stream_map");
		ArrayList<String> stream_query_list = new ArrayList<String>(Arrays.asList(stream_query.split(",")));
		String adaptive_fmts = (String)video_map.get("adaptive_fmts");
		ArrayList<String> adaptive_fmts_list = new ArrayList<String>(Arrays.asList(adaptive_fmts.split(",")));
		stream_query_list.addAll(adaptive_fmts_list);
		
		Map stream_URLs_map = new HashMap();
		for (int i = 0; i < stream_query_list.size(); i++) {
			Map stream_map = getMapFromString(stream_query_list.get(i), "UTF-8");
			
			String type = (String)stream_map.get("type");
			String url_str = (String)stream_map.get("url");
			
			if (!TextUtils.isEmpty(url_str) && type.contains("video/mp4")) {
				String stream_str = url_str;
				String signature = (String)stream_map.get("sig");
				if (!TextUtils.isEmpty(signature)) {
					stream_str = stream_str + "&signature=" + signature;
				}
				
				URL stream_url = new URL(stream_str);
				Map sig_map = getMapFromString(stream_url.getQuery(), "UTF-8");
				if (sig_map.containsKey("signature")) {
					String itag = (String)stream_map.get("itag");
					stream_URLs_map.put(itag, stream_url);
				}
			}
		}
			
		boolean contentIsAvailable = false;
		ArrayList<String> video_quality_list = new ArrayList<String>();
		video_quality_list.add(VideoQuality.Small240);
		video_quality_list.add(VideoQuality.Medium360);
		video_quality_list.add(VideoQuality.HD720);
		video_quality_list.add(VideoQuality.HD1080);
		
		for (int i = 0; i < video_quality_list.size(); i++) {
			URL stream_url = (URL)stream_URLs_map.get(video_quality_list.get(i));
			if (stream_url != null) {
				video_url_map.put(video_quality_list.get(i), stream_url);
				contentIsAvailable = true;
			}
		}
		
		mAttemptType++;
		
		if (contentIsAvailable) {
			extractVideobyId(video_id);
		}
	}

	@SuppressWarnings({ "rawtypes" })
	private static String addQueryStringToUrlString(String url, Map parameters) {
		String retString = url;
		if (parameters != null) {
			Iterator iter = parameters.entrySet().iterator();

			while (iter.hasNext()) {
				Map.Entry mEntry = (Map.Entry) iter.next();

				if (!retString.contains("?")) {
					retString = retString + String.format("?%s=%s", mEntry.getKey(), mEntry.getValue());
				} else {
					retString = retString + String.format("&%s=%s", mEntry.getKey(), mEntry.getValue());
				}
			}
		}

		return retString;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Map getMapFromString(String str, String encoding) {
		Map retMap = new HashMap();

		String [] temp_array = str.split("&");
		ArrayList<String> str_list = new ArrayList<String>(Arrays.asList(temp_array));
		
		for (int i = 0; i < str_list.size(); i++) {
			String str_item = str_list.get(i);
			
			String[] str_array = str_item.split("=");
			if (str_array.length < 2)
				continue;
			
			String key = str_array[0];
			String value = "";
			try {
				value = URLDecoder.decode(str_array[1], encoding);
				value = value.replace("+", " ");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			retMap.put(key, value);
		}

		return retMap;
	}
	
	public static String parseYoutubeVideoId(String youtubeUrl) {
		String video_id = null;
		if (youtubeUrl != null && youtubeUrl.trim().length() > 0 &&
				youtubeUrl.startsWith("http")) {
			// ^.*((youtu.be\/)|(v\/)|(\/u\/\w\/)|(embed\/)|(watch\?))\??v?=?([^#\&\?]*).*/
			String expression = "^.*((youtu.be" + "\\/)" + "|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#\\&\\?]*).*";
			CharSequence input = youtubeUrl;
			Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(input);
			if (matcher.matches()) {
				// Regular expression some how doesn't work with id with "v" at
				// prefix
				String groupIndex1 = matcher.group(7);
				if (groupIndex1 != null && groupIndex1.length() == 11) {
					video_id = groupIndex1;

				} else if (groupIndex1 != null && groupIndex1.length() == 10) {
					video_id = "v" + groupIndex1;
				}
			}
		}
		return video_id;
	}
	
	public static boolean isYoutubeURL(String url) {
		String youtube_key = "http://www.youtube.com/";
		return url.contains(youtube_key);
	}
}
