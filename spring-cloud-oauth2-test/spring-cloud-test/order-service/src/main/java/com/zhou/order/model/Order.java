package com.zhou.order.model;

public class Order {
	private String id;
	private String name;
	private Long userId;
	
	public Order() {
	}
	public Order(String id, String name, Long userId) {
		this.id = id;
		this.name = name;
		this.userId = userId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
