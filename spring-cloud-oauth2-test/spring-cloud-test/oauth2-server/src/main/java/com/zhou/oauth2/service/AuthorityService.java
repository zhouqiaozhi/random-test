package com.zhou.oauth2.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zhou.oauth2.entity.AuthorityEntity;


public interface AuthorityService extends JpaRepository<AuthorityEntity, Long> {
	List<AuthorityEntity> findAllByUsername(String username);
}
