package com.zhou.k8s.utils;

public class StringUtils {
	public static String getString(String value) {
		return String.format("\"%s\"", value);
	}
}
