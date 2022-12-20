package com.softly.neighbor.controller;

import java.util.HashMap;

import com.softly.neighbor.persistence.entities.Bird;
import com.softly.neighbor.service.BirdService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BirdController {
	
	@Autowired
	private BirdService birdService;

    @RequestMapping("birds")
    public ResponseEntity<?> getBird() {
    	HashMap<String, Object> result = new HashMap<>();
    	result.put("response", RESULT.OK);
		result.put("message", "");
    	result.put("birds", birdService.get());
        return new ResponseEntity<HashMap<String, Object>>(result, HttpStatus.OK);
    }

    @PostMapping("saveBird")
    public ResponseEntity<?> saveBird(@RequestBody Bird bird) {
    	HashMap<String, Object> result = new HashMap<>();
    	birdService.save(bird);
    	result.put("response", RESULT.OK);
		result.put("message", "The bird was saved.");
		result.put("birds", birdService.get());
    	return new ResponseEntity<HashMap<String, Object>>(result, HttpStatus.OK);
    }
}
