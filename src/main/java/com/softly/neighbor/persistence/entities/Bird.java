package com.softly.neighbor.persistence.entities;

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
public class Bird {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	@Column
    private String specie;
	
	@Column
    private String size;
}