package com.hyewon.grocey_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GroceyApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(GroceyApiApplication.class, args);
	}

}
