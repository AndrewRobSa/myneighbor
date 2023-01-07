package com.softly.neighbor.config;

import com.softly.neighbor.persistence.repository.PersonRepository;
import com.softly.neighbor.persistence.repository.UserRepository;
import com.softly.neighbor.service.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
prePostEnabled = true)
public class SecurityConfiguration {
	
	@Autowired private UserRepository userRepository;
	@Autowired private PersonRepository personRepository;
	
	@Bean
    UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }
 
    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }
    
    @Bean
    AuthenticationProvider authProvider() {
        DaoAuthenticationProvider provider =  new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        
        return provider;
    }
    
    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/images/**", "/js/**", "/webjars/**");
    }

	@Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        	.requiresChannel(channel -> 
        		channel.anyRequest().requiresSecure())
        	.csrf().disable()
    		.cors().disable()
    		.authorizeHttpRequests().requestMatchers("/", "/error", "/registration").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/processLogin")
                .failureUrl("/login?error")
                .defaultSuccessUrl("/home", true)
                .usernameParameter("email")
                .passwordParameter("password")
                .permitAll()
            .and()
            .logout()
                .permitAll()
                .logoutSuccessUrl("/login?logout")
            .and()
            .headers().frameOptions().sameOrigin()
            .and()
            .exceptionHandling(e -> e
                    .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
            .oauth2Login(oauth2 -> oauth2
            		.defaultSuccessUrl("/home", true)
            				.userInfoEndpoint(userInfo -> userInfo
            						.oidcUserService(this.oidcUserService())
            						.userService(this.oAuth2UserService())
            						)
            				);
            
        return http.build();
    }
	
	@Bean("oauthRest")
    WebClient rest(ClientRegistrationRepository clients, OAuth2AuthorizedClientRepository authz) {
        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 =
                new ServletOAuth2AuthorizedClientExchangeFilterFunction(clients, authz);
        return WebClient.builder().filter(oauth2).build();
    }
	
	private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
		return new OIDCUserServiceImpl(this.userRepository, this.personRepository);
	}
	
	private OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
		return new OAuth2UserServiceImpl(this.userRepository, this.personRepository);
	}
}
