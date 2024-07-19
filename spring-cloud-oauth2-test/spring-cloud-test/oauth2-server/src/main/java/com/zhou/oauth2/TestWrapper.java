package com.zhou.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

public class TestWrapper extends HttpServletRequestWrapper {

	public TestWrapper(HttpServletRequest request) {
		super(request);
	}
	
	public String getHeader(String name) {
		return "host".equals(name) ? "oauth2-server:9000" : super.getHeader(name);
	}
}
