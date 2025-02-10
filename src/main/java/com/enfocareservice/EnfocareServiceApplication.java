package com.enfocareservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement  // <-- Add this annotation here
public class EnfocareServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnfocareServiceApplication.class, args);
    }

}
