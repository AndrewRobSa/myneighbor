package com.softly.neighbor.service;

import java.util.ArrayList;
import java.util.List;

import com.softly.neighbor.persistence.entities.Authority;
import com.softly.neighbor.persistence.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
    UserRepository userRepository;
	
    @Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
    	com.softly.neighbor.persistence.entities.User appUser = 
    			userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("The user was not found."));
			
	    List<GrantedAuthority> grantList = new ArrayList<>();
	    for (Authority authority : appUser.getAuthority()) {
	        // ROLE_USER, ROLE_ADMIN,..
	        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority.getAuthority());
	        grantList.add(grantedAuthority);
	    }
			
	    UserDetails user = (UserDetails) new User(appUser.getEmail(), appUser.getPassword(), grantList);
	    return user;
     }
}
