package com.softly.neighbor.persistence.repository;

import java.util.Optional;

import com.softly.neighbor.persistence.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	public Optional<User> findByEmail(String username);
}
