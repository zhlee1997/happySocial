package com.jeremylee.insta_gateway_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
public class InstaGatewayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InstaGatewayServiceApplication.class, args);
	}

}
