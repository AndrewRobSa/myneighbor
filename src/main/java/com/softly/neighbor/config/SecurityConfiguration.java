package com.softly.neighbor.config;

import com.softly.neighbor.service.UserDetailsServiceImpl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

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
        		.authorizeHttpRequests().requestMatchers("/login", "/registration").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
	                .loginPage("/login")
	                .permitAll()
	                .defaultSuccessUrl("/home")
	                .failureUrl("/loginFailed")
	                .usernameParameter("email")
	                .passwordParameter("password")
	                .and()
                .logout()
	                .permitAll()
	                .logoutSuccessUrl("/login");
 
        http.headers().frameOptions().sameOrigin();
 
        return http.build();
    }
 
    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/images/**", "/js/**", "/webjars/**");
    }
}
