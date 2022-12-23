package com.softly.neighbor.controller;

import java.util.Collections;
import java.util.HashMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	
	@PostMapping("home") 
	public ResponseEntity<?> home(){
		HashMap<String, Object> result = new HashMap<>();
		result.put("response", RESULT.OK);
		result.put("message", "Successful login!.. This is the home page.");
		return new ResponseEntity<HashMap<String, Object>>(result, HttpStatus.OK);	
	}
	
	@GetMapping("loggedUser")
    public ResponseEntity<?> user(@AuthenticationPrincipal OAuth2User principal) {
		HashMap<String, Object> result = new HashMap<>();
		result.put("response", RESULT.OK);
		result.put("message", Collections.singletonMap("name", principal.getAttribute("name")));
		return new ResponseEntity<HashMap<String, Object>>(result, HttpStatus.OK);
    }
}
