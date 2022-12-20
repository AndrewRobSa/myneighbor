package com.softly.neighbor.service;

import com.softly.neighbor.persistence.entities.Person;
import com.softly.neighbor.persistence.repository.PersonRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService {
	
	@Autowired
	private PersonRepository personRepository;
	
	public String savePerson(Person person) {
		if(person == null) {
			return "The Person could not be saved.";
		}
		
		this.personRepository.save(person);
		return "ok";
	}

}
