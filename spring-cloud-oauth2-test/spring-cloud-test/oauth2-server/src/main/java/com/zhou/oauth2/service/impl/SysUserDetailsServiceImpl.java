package com.zhou.oauth2.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.zhou.oauth2.service.AuthorityService;
import com.zhou.oauth2.service.SysUserService;

@Component
public class SysUserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private AuthorityService authorityService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		var user = sysUserService.findById(username).orElse(null);
		if(user == null) {
			throw new UsernameNotFoundException(username);
		}
		var authorities = authorityService.findAllByUsername(username).stream().map(it -> new SimpleGrantedAuthority(it.getAuthority())).toList();
		return new User(username, user.getPassword(), authorities);
	}

}
