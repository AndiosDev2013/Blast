package com.example.blast.model;

import java.util.ArrayList;

public class ChannelModel {
	public static class Result {
		public ArrayList<Info> result;
	}
	
	
	public static class Info {
		public String tid;
		public String vid;
		public String name;
		public String format;
		public String weight;
		public String uri;
	}
	
	public static class DetailInfo {
		public String tid;
		public String vid;
		public String name;
		public String format;
		public String weight;
		public String uri;
	}
}
