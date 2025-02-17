package com.jeremylee.insta_media_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class InstaMediaServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InstaMediaServiceApplication.class, args);
	}

}
