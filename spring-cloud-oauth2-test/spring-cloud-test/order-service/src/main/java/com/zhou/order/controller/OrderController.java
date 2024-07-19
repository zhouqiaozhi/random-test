package com.zhou.order.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhou.order.client.UserClient;
import com.zhou.order.model.Order;
import com.zhou.order.model.OrderDetail;

@RestController
public class OrderController {
	private static final Map<String, Order> map;
	static {
		map = new HashMap<>();
		map.put("03624eb7-3a89-4676-b79a-31bc87144912", new Order("03624eb7-3a89-4676-b79a-31bc87144912", "orderA", 1L));
		map.put("74c7d58b-84bb-4235-8898-508bca04480b", new Order("74c7d58b-84bb-4235-8898-508bca04480b", "orderB", 2L));
		map.put("4cbb48de-15d0-49ce-87ea-bfe50208caea", new Order("4cbb48de-15d0-49ce-87ea-bfe50208caea", "orderC", 1L));
		map.put("0a96c9cf-c8b3-44b0-a8f0-8eb3bd40b669", new Order("0a96c9cf-c8b3-44b0-a8f0-8eb3bd40b669", "orderD", 1L));
	}
	
	private final Map<String, List<Order>> map2;
	{
		map2 = new HashMap<>();
		map2.put("user", List.of( map.get("03624eb7-3a89-4676-b79a-31bc87144912"), map.get("4cbb48de-15d0-49ce-87ea-bfe50208caea"), map.get("0a96c9cf-c8b3-44b0-a8f0-8eb3bd40b669") ));
		map2.put("admin", List.of(map.get("74c7d58b-84bb-4235-8898-508bca04480b")));
	}
	
	
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private UserClient userClient;
	
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/last")
	public Order last() {
		var cur = SecurityContextHolder.getContext().getAuthentication();
		var list = map2.get(cur.getName());
		return list.get(list.size() - 1);
	}
	
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/last/info")
	public Order lastInfo() {
		var cur = SecurityContextHolder.getContext().getAuthentication();
		var list = map2.get(cur.getName());
		var orderDetail = objectMapper.convertValue(list.get(list.size() - 1), OrderDetail.class);
		var userDto = userClient.getUser();
		orderDetail.setUser(userDto.getName());
		return orderDetail;
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/all")
	public List<Order> getAllOrder() {
		return new ArrayList<>(map.values());
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/detail/{uuid}")
	public OrderDetail detail(@PathVariable("uuid") String uuid) {
		var orderDetail = objectMapper.convertValue(map.get(uuid), OrderDetail.class);
		var userDto = userClient.getUserById(orderDetail.getUserId());
		orderDetail.setUser(userDto.getName());
		return orderDetail;
	}
}
