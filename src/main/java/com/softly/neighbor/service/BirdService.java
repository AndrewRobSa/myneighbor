package com.softly.neighbor.service;

import java.util.List;

import com.softly.neighbor.persistence.entities.Bird;
import com.softly.neighbor.persistence.repository.BirdRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BirdService {
	
	@Autowired
	private BirdRepository birdRepository;
	
	/**
	@PostConstruct
    void postConstruct(){
        Bird sampleBird = new Bird();
        sampleBird.setSpecie("Hummingbird");
        sampleBird.setSize("small");
        save(sampleBird);
    }*/

    public void save(Bird bird) {
        birdRepository.save(bird);
    }

    public List<Bird> get() {
        return birdRepository.findAll();
    }
}
