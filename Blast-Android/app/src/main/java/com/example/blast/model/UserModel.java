package com.example.blast.model;

import java.util.ArrayList;

public class UserModel {
	
	/*
	 * Login
	 */
	public static class Login {
		public static class Result {
			public String sessid;
			public String session_name;
			public User	user;
		}
		
		public static class User {
			public String uid;
			public String mail;
			public String theme;
			public String signature;
			public String signature_format;
			public String created;
			public String access;
			public String login;
			public String status;
			public String timezone;
			public String language;
			public String picture;
			public boolean data;
//			public Role roles;
//			public Rdf rdf_mapping;
		}
		
		public static class Role {
			public String name;
		}
		
		public static class Rdf {
			public static class Name {
				public ArrayList<String> predicates;
			}
			
			public static class Homepage {
				public ArrayList<String> predicates;
				public String type;
			}
			
			public ArrayList<String> rdftype;
			public Name name;
			public Homepage homepage;
			public String type;
		}
	}
	
	/*
	 * Register
	 */
	public static class Register {
		public static class Result {
			public String uid;
			public String uri;
		}
	}
}
