package com.softly.neighbor.config;

import com.softly.neighbor.service.UserDetailsServiceImpl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
public class SecurityConfiguration {
	
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
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().disable()
        		.authorizeHttpRequests().requestMatchers("/", "/login", "/login?logout", "/login?error", 
        				"/error", "/registration").permitAll()
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
	                .logoutSuccessUrl("/login?logout");
 
        http.headers().frameOptions().sameOrigin();
        
        http.exceptionHandling(e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
            .oauth2Login();
 
        return http.build();
    }
 
    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/images/**", "/js/**", "/webjars/**");
    }
}
