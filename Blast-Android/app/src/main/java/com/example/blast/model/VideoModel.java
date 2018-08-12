package com.example.blast.model;

import java.util.ArrayList;

public class VideoModel {
	public static class Result {
		public ArrayList<Info> result;
	}
	
	public static class Info {
		public String node_title;
		public String nid;
		public String Channel;
		public String Description;
		public String Video;
	}

//	public static class Info {
//		public String vid;
//		public String uid;
//		public String title;
//		public String log;
//		public String status;
//		public String comment;
//		public String promote;
//		public String sticky;
//		public String ds_switch;
//		public String nid;
//		public String type;
//		public String language;
//		public String created;
//		public String changed;
//		public String tnid;
//		public String translate;
//		public String revision_timestamp;
//		public String revision_uid;
//		public ArrayList<String> field_video_description;
//		public UND field_channel;
//		public UND1 field_video;
//		public ArrayList<String> field_video_thumbnail;
//		public RDF_MAPPING rdf_mapping;
//		public String name;
//		public String picture;
//		public String data;
//		public String uri;
//	}
//	
//	public static class UND {
//		public ArrayList<UND_item> und;
//	}
//	
//	public static class UND_item {
//		public String tid;
//	}
//	
//	public static class UND1 {
//		public ArrayList<UND_item1> und;
//	}
//	
//	public static class UND_item1 {
//		public String fid;
//		public String uid;
//		public String filename;
//		public String uri;
//		public String filemime;
//		public String filesize;
//		public String status;
//		public String timestamp;
//		public String type;
//		public UND field_folder;
//		public ArrayList<String> rdf_mapping;
//		public ArrayList<String> metadata;
//		public String alt;
//		public String title;
//		public String display;
//		public String description;
//	}
//	
//	public static class RDF_MAPPING {
//		public ArrayList<String> rdftype;
//		public PREDICATES title;
//		public PREDICATES2 created;
//		public PREDICATES2 changed;
//		public PREDICATES body;
//		public PREDICATES1 uid;
//		public PREDICATES name;
//		public PREDICATES3 comment_count;
//		public PREDICATES2 last_activity;
//	}
//	
//	public static class PREDICATES {
//		public ArrayList<String> predicates;
//	}
//	
//	public static class PREDICATES1 {
//		public ArrayList<String> predicates;
//		public String type;
//	}
//	
//	public static class PREDICATES2 {
//		public ArrayList<String> predicates;
//		public String datatype;
//		public String callback;
//	}
//	
//	public static class PREDICATES3 {
//		public ArrayList<String> predicates;
//		public String datatype;
//	}
	
	public static class DetailInfo {
		public String id;
		public String category_name;
		public String title;
		public String description;
		public String thumbnail_img;
		public String vote_up_count;
		public String vote_down_count;
		public String uri ;
	}
}
