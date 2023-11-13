package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class ProductAccountApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductAccountApplication.class, args);
	}
	
	@Bean
	public WebClient.Builder webBuilder(){
		return WebClient.builder();
	}
}
