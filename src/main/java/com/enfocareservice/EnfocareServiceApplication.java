package com.enfocareservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EnfocareServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnfocareServiceApplication.class, args);
	}

}
