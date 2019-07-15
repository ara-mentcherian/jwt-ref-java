package com.telus.apip.jwtref;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.telus.apip.jwtref.keystore.KeyGenerator;

@SpringBootApplication
public class JwtReferenceApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtReferenceApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner init(KeyGenerator kg) {
		return (args) -> {
			kg.generateKeySet("application-01");
			kg.generateKeySet("application-02");
			kg.generateKeySet("application-03");
			kg.generateKeySet("application-04");
		};
	}

}
