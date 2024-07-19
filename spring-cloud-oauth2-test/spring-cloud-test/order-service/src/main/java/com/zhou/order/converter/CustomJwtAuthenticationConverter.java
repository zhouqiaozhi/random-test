package com.zhou.order.converter;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

@Component
public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private Collection<? extends GrantedAuthority> extractResourceRoles(final Jwt jwt) {
    	var list = new ArrayList<String>();
    	var scope = jwt.getClaim("scope");
    	var role = jwt.getClaim("role");
    	list.addAll((Collection<String>) scope);
    	list.addAll((Collection<String>) role);
        return list.stream().map(SimpleGrantedAuthority::new).toList();
    }

    private final JwtGrantedAuthoritiesConverter defaultGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    public CustomJwtAuthenticationConverter() {
    }

    @Override
    public AbstractAuthenticationToken convert(final Jwt source) {
        return new JwtAuthenticationToken(source, extractResourceRoles(source));
    }
}