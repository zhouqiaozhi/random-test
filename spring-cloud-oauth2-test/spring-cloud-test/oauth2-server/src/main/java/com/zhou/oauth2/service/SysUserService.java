package com.zhou.oauth2.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zhou.oauth2.entity.SysUserEntity;

public interface SysUserService extends JpaRepository<SysUserEntity, String> {
}
