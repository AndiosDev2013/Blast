package com.example.blast.model;

import com.google.gson.Gson;

public class ErrorModel {
	public int status;
	public String result;
	
	public static String GetErrorString(String error_repsonse) {
		Gson gson = new Gson();
		ErrorModel error = new ErrorModel();
		try {
			error = gson.fromJson(error_repsonse, ErrorModel.class);
			return error.result;
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return "Unknown Error";
	}
}
