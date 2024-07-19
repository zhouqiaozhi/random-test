package com.zhou.order.client.fallback;

import org.springframework.stereotype.Component;

import com.zhou.common.dto.UserDto;
import com.zhou.order.client.UserClient;

@Component
public class UserClientFallback implements UserClient {
	private final UserDto user = new UserDto();

	@Override
	public UserDto getUserById(Long id) {
		return user;
	}

	@Override
	public UserDto getUser() {
		return user;
	}

}
