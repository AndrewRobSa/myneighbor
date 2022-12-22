package com.softly.neighbor.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
	
	@RequestMapping("login")
    public ResponseEntity<?> login() {
		HashMap<String, Object> result = new HashMap<>();
		result.put("response", RESULT.OK);
		result.put("message", "This is login page, please login.");
		return new ResponseEntity<HashMap<String, Object>>(result, HttpStatus.OK);	
    }

	@PostMapping("loginFailed")
    public ResponseEntity<?> loginFailed() {
		HashMap<String, Object> result = new HashMap<>();
		result.put("response", RESULT.ERROR);
		result.put("message", "Login went wrong, please try again...");
		return new ResponseEntity<HashMap<String, Object>>(result, HttpStatus.OK);	
    }
	
	@PostMapping("home") 
	public ResponseEntity<?> home(){
		HashMap<String, Object> result = new HashMap<>();
		result.put("response", RESULT.OK);
		result.put("message", "Successful login!.. This is the home page.");
		return new ResponseEntity<HashMap<String, Object>>(result, HttpStatus.OK);	
	}
	
	@GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        return Collections.singletonMap("name", principal.getAttribute("name"));
    }
}
