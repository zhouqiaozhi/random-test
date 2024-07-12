package com.zhou.batch.utils;

public class StringUtils {
	public static boolean isNumber(String s) {
		for(int i = 0; i < s.length(); i++) {
			if(s.charAt(i) > '9' || s.charAt(i) < '0') {
				return false;
			}
		}
		return true;
	}
}
