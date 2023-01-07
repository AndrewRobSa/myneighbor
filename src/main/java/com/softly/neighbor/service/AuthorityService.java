package com.softly.neighbor.service;

import com.softly.neighbor.persistence.entities.Authority;
import com.softly.neighbor.persistence.repository.AuthorityRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorityService {
	
	@Autowired
	private AuthorityRepository authorityRepository;
	
	public Authority save(Authority authority) {
		return this.authorityRepository.save(authority);
	}
	
	/*
	@PostConstruct
	void init_() {
		Authority authority = new Authority();
		authority.setAuthority("ROLE_USER");
		this.authorityRepository.save(authority);
		
		authority = new Authority();
		authority.setAuthority("ROLE_ADMIN");
		this.authorityRepository.save(authority);
	}*/
}
