package com.zhou.order.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class OAuth2FeignInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate template) {
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
		String token = request.getHeader("Authorization");
		if(!token.isEmpty()) {
			template.header("Authorization", token);
		}
	}
	
}
