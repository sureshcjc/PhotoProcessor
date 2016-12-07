package com.waldo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Suresh
 *
 *	Spring boot started class
 */
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan({"com.waldo","com.waldo.services","com.waldo.repo"})
public class Application {
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
