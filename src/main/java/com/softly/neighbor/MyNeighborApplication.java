package com.softly.neighbor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"com.softly.neighbor.persistence.entities"})
@EnableJpaRepositories(basePackages = {"com.softly.neighbor.persistence.repository"})
@ComponentScan({"com.softly.neighbor.config","com.softly.neighbor.controller",
	"com.softly.neighbor.service"})
public class MyNeighborApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyNeighborApplication.class, args);
	}

}
