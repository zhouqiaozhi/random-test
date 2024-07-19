package com.zhou.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.zhou.user.converter.CustomJwtAuthenticationConverter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
public class SecurityConfig {
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http, CustomJwtAuthenticationConverter converter) throws Exception {
		http.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests.requestMatchers("/health").permitAll().anyRequest().authenticated());
		http.exceptionHandling(exceptionHandling ->exceptionHandling
				.accessDeniedHandler((request, response, accessDeniedException) -> {
					if(accessDeniedException instanceof AuthorizationDeniedException) {
						response.setStatus(HttpStatus.UNAUTHORIZED.value());
						response.getWriter().write(HttpStatus.UNAUTHORIZED.name());
					}
				})
		);
		http.oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer.jwt(jwt -> jwt.jwtAuthenticationConverter(converter)));
		return http.build();
	}
}
