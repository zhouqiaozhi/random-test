package com.zhou.k8s.utils;

import java.io.PrintWriter;

public class FileUtils {
	public static boolean write(String filename, String s) {
		boolean res = true;
		try(
				PrintWriter out = new PrintWriter(filename)
		) {
			out.write(s);
		} catch(Exception e) {
			System.out.println(e.getLocalizedMessage());
			res = false;
		}
		return res;
	}
}
