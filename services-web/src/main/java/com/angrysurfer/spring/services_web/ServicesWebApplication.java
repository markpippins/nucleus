package com.angrysurfer.spring.services_web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.angrysurfer.spring")
public class ServicesWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicesWebApplication.class, args);
	}

}
