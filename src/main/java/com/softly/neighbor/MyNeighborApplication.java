package com.softly.neighbor;

import com.softly.neighbor.service.DocumentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
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
public class MyNeighborApplication implements CommandLineRunner{
	
	@Autowired
	DocumentService documentService;

	public static void main(String[] args) {
		SpringApplication.run(MyNeighborApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		//documentService.deleteAll();
		documentService.init();
	}
}
