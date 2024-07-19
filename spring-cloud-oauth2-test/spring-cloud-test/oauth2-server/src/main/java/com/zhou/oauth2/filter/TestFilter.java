package com.zhou.oauth2.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class TestFilter extends OncePerRequestFilter {
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if(RequestMethod.POST.name().equals(request.getMethod()) && "/oauth2/login".equals(request.getRequestURI())) {
			if(request.getHeader("authorization") != null) {
				response.setStatus(HttpStatus.FORBIDDEN.value());
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write("{ \"result\": false, \"message\": \"User already logged in\"  }");
				log.info("User already logged in");
				return;
			}
			if(request.getSession(false) == null) {
				request.getSession(true);
			}
		}
		filterChain.doFilter(request, response);
		
	}

}
