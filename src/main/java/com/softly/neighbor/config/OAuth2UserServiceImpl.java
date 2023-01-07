package com.softly.neighbor.config;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.softly.neighbor.persistence.entities.Authority;
import com.softly.neighbor.persistence.entities.Person;
import com.softly.neighbor.persistence.entities.User;
import com.softly.neighbor.persistence.repository.PersonRepository;
import com.softly.neighbor.persistence.repository.UserRepository;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.Getter;
import lombok.Setter;

public class OAuth2UserServiceImpl implements OAuth2UserService<OAuth2UserRequest, OAuth2User>{
	
	private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
	
	public OAuth2UserServiceImpl(UserRepository userRepository, PersonRepository personRepository) {
		super();
		this.userRepository = userRepository;
		this.personRepository = personRepository;
	}

	@Getter@Setter
	private UserRepository userRepository;
	@Getter@Setter
	private PersonRepository personRepository;
	
	@Getter@Setter
	private class OAuth2UserInfo {
		String email;
		String fullName;
		String phoneNumber;
		String loginClient;
		
		OAuth2UserInfo(Map<String, Object> attributes, String client){
			this.email = (String)attributes.get("email");
			this.fullName = (String)attributes.get("name");
			this.phoneNumber = (String)attributes.get("phoneNumber");
			this.loginClient = client;
		}
	}
	
	/**
	 * Update the user in database if exists, otherwise create new
	 * @param user
	 * @return user model from DB
	 */
	private User updateUser(OAuth2UserInfo user) {
		//search user in DB. create new if doesn't exist
		User dbUser = this.userRepository.findByEmail(user.getEmail())
				.orElse(new User(user.getEmail(), user.getLoginClient()));
		//get person from user, if null create new and assign it to the user
		Person dbPerson = dbUser.getPerson();
		if(dbPerson == null) {
			dbPerson = new Person();
			dbUser.setPerson(dbPerson);
			
			//save personal information to person only the first time login in
			if(user.getFullName()!=null) dbPerson.setFullName(user.getFullName());
			if(user.getPhoneNumber()!=null) dbPerson.setPhoneNumber(user.getPhoneNumber());
			personRepository.save(dbPerson);
		}
		
		//save login details to user
		Date now = new Date();
		dbUser.setCreationDate(dbUser.getCreationDate()!=null? dbUser.getCreationDate() : now);
		dbUser.setLastLogin(now);
		return userRepository.save(dbUser);
	}

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		//delegate to default service loading of user
		OAuth2User user = delegate.loadUser(userRequest);
		
		//logic to manage user information if login client is GitHub
		if (Arrays.asList("github").contains(userRequest.getClientRegistration().getRegistrationId())) {
			//persist the user info into DB
			OAuth2UserInfo userInfo = new OAuth2UserInfo(
					user.getAttributes(), 
					userRequest.getClientRegistration().getRegistrationId());
			
			User appUser = this.updateUser(userInfo);
			
			//search the authorities from user in DB
			Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
			for (Authority authority : appUser.getAuthority()) {
		        // ROLE_USER, ROLE_ADMIN,..
		        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority.getAuthority());
		        mappedAuthorities.add(grantedAuthority);
		    }
			
			//create a copy of oauthUser but use the mappedAuthorities instead
			user = new DefaultOAuth2User(mappedAuthorities, user.getAttribute(null), "name");
			
		}else {
			throw new OAuth2AuthenticationException("Unknown registration client");
		}
		
		return user;
	}

}
