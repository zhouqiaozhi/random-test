package com.zhou.user.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.zhou.user.model.User;

@RestController
public class UserController {
	
	private final static Map<Long, User> map;
	static {
		map = new HashMap<>();
		map.put(1L, new User(1L, "userA", "UserA@mail.com"));
		map.put(2L, new User(2L, "adminA", "adminA@mail.com"));
		
	}
	
	private final Map<String, User> map2;
	{
		map2 = new HashMap<>();
		map2.put("user", map.get(1L));
		map2.put("admin", map.get(2L));
	}
	
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/info")
	public User getUser() {
		var cur = SecurityContextHolder.getContext().getAuthentication();
		return map2.get(cur.getName());
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/{id}")
	public User getUserById(@PathVariable Long id) {
		return map.get(id);
	}
	
}
