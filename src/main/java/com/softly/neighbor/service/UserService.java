package com.softly.neighbor.service;

import java.util.Date;
import com.softly.neighbor.persistence.entities.User;
import com.softly.neighbor.persistence.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	/**
	 * A new User is saved in DB.
	 * @param username
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param password
	 * @param phoneNumber
	 * @param birthDate
	 * @return Result message from the transaction 
	 */
	public String registrate(User user) {
		if(user == null) {
			return "The User could not be saved.";
		}
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		this.userRepository.save(user);
		return "ok";
	}
	
	public String registrateUserWithGoogle(OidcUser googleUser) {
		User user = new User();
		user.setEmail(googleUser.getEmail());
		user.setCreationDate(new Date());
		user.setLastLogin(user.getCreationDate());
		this.userRepository.save(user);
		return "ok";
	}
	
	/**
	 * Verify if an email is already linked to an user
	 * @param email
	 * @return
	 */
	public boolean exists(String email) {
		return this.userRepository.existsByEmail(email);
	}
}
