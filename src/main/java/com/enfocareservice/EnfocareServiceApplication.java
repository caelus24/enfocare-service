package com.enfocareservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
public class EnfocareServiceApplication {

	private static final Logger logger = LoggerFactory.getLogger(EnfocareServiceApplication.class);
	
    public static void main(String[] args) {
    	 logger.info("🚀 Starting Enfocare Service Application... TOGE");
         SpringApplication.run(EnfocareServiceApplication.class, args);
         logger.info("✅ Enfocare Service Application Started Successfully! TOGE");
    }

}
