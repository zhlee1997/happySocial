package com.jeremylee.idea_link_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class IdeaLinkServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IdeaLinkServiceApplication.class, args);
	}

}
