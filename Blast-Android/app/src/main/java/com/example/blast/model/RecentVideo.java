package com.example.blast.model;

import java.util.ArrayList;

public class RecentVideo {
	public static class InfoList {
		public int error;
		public ArrayList<Info> videos;
	}

	public static class Info {
		public String id;
		public String channel_id;
		public String source;
		public String thumbnail_url;
		public String url;
		public String views_num;
		public String timestamp_added;
		public String channel_name;
	}
}
