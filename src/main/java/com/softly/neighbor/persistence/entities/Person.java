package com.softly.neighbor.persistence.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class Person {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Column 
	private String firstName;
	
	@Column 
	private String lastName;
	
	@Column
	private String fullName;
	
	@Column 
	private String phoneNumber;
	
	@Column 
	private Date birthDate;
	
	@Column
	private String gender;

	/**
	 * This method is invoked in the registration (creation) of a new Person
	 * @param firstName
	 * @param lastName
	 * @param phoneNumber
	 * @param birthDate
	 * @param gender
	 */
	public Person(String firstName, String lastName, String phoneNumber, Date birthDate, String gender) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.birthDate = birthDate;
		this.gender = gender;
	}
	
}
