package com.zhou.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.zhou.common.dto.UserDto;
import com.zhou.order.client.fallback.UserClientFallback;
import com.zhou.order.interceptor.OAuth2FeignInterceptor;

@FeignClient(name = "user-service", configuration = OAuth2FeignInterceptor.class, fallback = UserClientFallback.class)//, configuration = OAuth2FeignInterceptor.class)
public interface UserClient {
	@GetMapping("/user/info")
	UserDto getUser();
	@GetMapping("/user/{id}")
	UserDto getUserById(@PathVariable("id") Long id);
}