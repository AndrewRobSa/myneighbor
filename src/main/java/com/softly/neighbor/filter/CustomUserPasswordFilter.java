package com.softly.neighbor.filter;

import com.softly.neighbor.config.JwtUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;

public class CustomUserPasswordFilter extends UsernamePasswordAuthenticationFilter{
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Setter
	private boolean postOnly = true;
	
	@Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) 
        throws AuthenticationException {
		
		if (this.postOnly && !request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}
		
		String username = obtainUsername(request);
		username = (username != null) ? username.trim() : "";
		String password = obtainPassword(request);
		password = (password != null) ? password : "";
		
		UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
		super.setDetails(request, authRequest);
		
		Authentication authentication = this.getAuthenticationManager().authenticate(authRequest);
		this.setToken(request, authentication);
		return authentication;
    }
	
	private void setToken(HttpServletRequest request, Authentication authentication) {
		String token = jwtUtils.generateJwtToken(authentication);
		request.setAttribute("Authorization", token);
	}

}
