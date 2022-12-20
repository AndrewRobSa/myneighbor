package com.softly.neighbor.persistence.repository;

import com.softly.neighbor.persistence.entities.Bird;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BirdRepository extends JpaRepository<Bird, Long>{

}
