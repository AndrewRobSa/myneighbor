package com.softly.neighbor.config;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.softly.neighbor.persistence.repository.UserRepository;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;

import lombok.Getter;
import lombok.Setter;

public class CustomAuthoritiesMappper implements GrantedAuthoritiesMapper {
	
	@Getter@Setter
	private UserRepository userRepository;

	@Override
	public Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
		Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
		authorities.forEach(authority -> {
			if (OidcUserAuthority.class.isInstance(authority)) {
				OidcUserAuthority oidcUserAuthority = (OidcUserAuthority)authority;

				// Map the claims found in idToken and/or userInfo
				// to one or more GrantedAuthority's and add it to mappedAuthorities

			} else if (OAuth2UserAuthority.class.isInstance(authority)) {
				OAuth2UserAuthority oauth2UserAuthority = (OAuth2UserAuthority)authority;

				Map<String, Object> userAttributes = oauth2UserAuthority.getAttributes();

				// Map the attributes found in userAttributes
				// to one or more GrantedAuthority's and add it to mappedAuthorities
			}
		});
		
		return mappedAuthorities;
	}
	
	

}
