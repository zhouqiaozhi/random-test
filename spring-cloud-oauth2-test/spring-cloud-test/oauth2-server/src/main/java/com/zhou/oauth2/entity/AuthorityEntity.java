package com.zhou.oauth2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity(name = "authority")
public class AuthorityEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "AUTH_ID")
	@SequenceGenerator(name = "AUTH_ID")
	private Long id;
	@Column
	private String username;
	@Column
	private String authority;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
}
