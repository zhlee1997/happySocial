package com.jeremylee.insta_auth_service;

import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Encoders;

import java.security.Key;

@SpringBootApplication
public class InstaAuthServiceApplication {

	public static void main(String[] args) {
//		Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
//		String base64UrlEncodedSecret = Encoders.BASE64URL.encode(key.getEncoded());
//		System.out.println("Base64URL Encoded Secret: " + base64UrlEncodedSecret);

		SpringApplication.run(InstaAuthServiceApplication.class, args);


	}

}
