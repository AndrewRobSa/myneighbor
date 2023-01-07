package com.softly.neighbor.controller;

import java.util.Collections;
import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class LoginController {
	
	@RequestMapping("login")
    public ResponseEntity<?> login(@RequestParam (required = false) String error, @RequestParam (required = false) String logout) {
		HashMap<String, Object> result = new HashMap<>();
		if(error!=null) {
			result.put("response", RESULT.OK);
			result.put("message", "Wrong username or password!.. please try again.");
		} else if(logout!=null) {
			result.put("response", RESULT.OK);
			result.put("message", "You logged out... come back soon!");
		} else {
			result.put("response", RESULT.OK);
			result.put("message", "This is login page! please login.");
		}
		return new ResponseEntity<HashMap<String, Object>>(result, HttpStatus.OK);	
    }
	
	@RequestMapping("home") 
	public ResponseEntity<?> home(HttpServletRequest request){
		HashMap<String, Object> result = new HashMap<>();
		result.put("response", RESULT.OK);
		result.put("message", "Successful login!.. This is the home page.");
		
		String token = request.getHeader("Authorization");
		result.put("Token", token);
		return new ResponseEntity<HashMap<String, Object>>(result, HttpStatus.OK);	
	}
	
	@GetMapping("loggedUser")
    public ResponseEntity<?> user(@AuthenticationPrincipal OAuth2User principal) {
		HashMap<String, Object> result = new HashMap<>();
		result.put("response", RESULT.OK);
		result.put("message", Collections.singletonMap("name", principal.getAttribute("name")));
		//retrieve the authorities associated with the user
		StringBuilder builder = new StringBuilder();
		principal.getAuthorities().forEach(a -> builder.append(a.getAuthority() + " "));
		result.put("authorities", builder.toString().strip());
		return new ResponseEntity<HashMap<String, Object>>(result, HttpStatus.OK);
    }
	
	@GetMapping("login/oauth2/code/{client}")
	public ResponseEntity<?> redirectLogin(@PathVariable String client, @RequestParam String code, @RequestParam String state){
		HashMap<String, Object> result = new HashMap<>();
		
		
		return new ResponseEntity<HashMap<String, Object>>(result, HttpStatus.OK);
	}
	
}
