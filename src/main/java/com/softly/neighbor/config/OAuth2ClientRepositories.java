package com.softly.neighbor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;

@Configuration
public class OAuth2ClientRepositories {
	
	@Bean
	ClientRegistrationRepository clientRegistrationRepository() {
		return new InMemoryClientRegistrationRepository(this.googleClientRegistration());
	}

	//GOOGLE
 	private ClientRegistration googleClientRegistration() {
 		return ClientRegistration.withRegistrationId("google")
 			.clientId("83743108461-6sklrtmgkrmpmsvn2agrddlm2a97tdaa.apps.googleusercontent.com")
 			.clientSecret("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")
 			.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
 			.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
 			.redirectUri("https://localhost:8080/login/oauth2/code/google")
 			.scope("openid", "profile", "email", "address", "phone")
 			.authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
 			.tokenUri("https://www.googleapis.com/oauth2/v4/token")
 			.userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
 			.userNameAttributeName(IdTokenClaimNames.SUB)
 			.jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
 			.clientName("Google")
 			.build();
	}
}
