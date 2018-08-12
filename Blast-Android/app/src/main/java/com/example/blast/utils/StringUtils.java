package com.example.blast.utils;

import java.util.ArrayList;

public class StringUtils {
	/*
	 * Get description string from seperated string
	 */
	public static String getDescriptionFromStringArr(ArrayList<String> string_list) {
		if (string_list == null)
			return "";
		if (string_list.size() == 0)
			return "";
		
		String retStr = "";
		for (int i = 0; i < string_list.size()-1; i++) {
			if (string_list.get(i) != null) {
				retStr = retStr + "   " + string_list.get(i) + System.getProperty("line.seperator");
			}
		}
		if (string_list.get(string_list.size()-1) != null) {
			retStr = retStr + "   " + string_list.get(string_list.size()-1);
		}
		
		return retStr;
	}
}
