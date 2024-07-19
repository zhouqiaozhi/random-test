package com.zhou.gateway.config;

import java.net.URI;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationEntryPoint;

import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {
	@Bean
	SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
		var redirect = new RedirectServerAuthenticationEntryPoint("/oauth2/authorization/oidc-client");
		http
			.authorizeExchange(authorizeExchange -> authorizeExchange
						.pathMatchers(HttpMethod.POST, "/oauth2/login")
						.permitAll()
						.anyExchange()
						.authenticated()
					)
			.oauth2Login(login -> login
				.authenticationSuccessHandler((webFilterExchange, authentication) -> {
					return webFilterExchange.getExchange().getResponse().writeWith(Mono.just(new DefaultDataBufferFactory().wrap("OK".getBytes())));
				}))
				.logout(logout -> logout.logoutSuccessHandler((exchange, authentication) -> {
					return exchange.getExchange().getResponse().writeWith(Mono.just(new DefaultDataBufferFactory().wrap("Logout success".getBytes()))); 
				})
			)
			.exceptionHandling((exceptionHandling) -> exceptionHandling
				.authenticationEntryPoint((exchange, ex) -> {
//					return Mono.just(exchange.getRequest().getURI().getRawPath())
//					.map(it -> "/reload".equals(it))
//					.doOnSuccess(res -> {
//						if(res) {
//							redirect.commence(exchange, ex);
//						} else {
//							exchange.getResponse().writeWith(Mono.just(new DefaultDataBufferFactory().wrap("You must do login before access resource".getBytes())));
//						}
//					});
					
//					if("/reload".equals(exchange.getRequest().getURI().getRawPath())) {
//						
//					}
					
					return Mono.just(exchange.getRequest().getURI().getRawPath())
					.map(it -> "/reload".equals(it))
					.map(res -> {
						if(res) {
							return redirect.commence(exchange, ex);
						} else {
							return exchange.getResponse().writeWith(Mono.just(new DefaultDataBufferFactory().wrap("You must do login before access resource".getBytes())));
						}
					}).flatMap(it -> it);
				})
			)
			.oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer.jwt(Customizer.withDefaults())
		);
		http.csrf(csrf -> csrf.disable());
		http.cors(cors -> cors.disable());
		return http.build();
	}	
}
