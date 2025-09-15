package com.angrysurfer.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.angrysurfer.spring")
public class NucleusSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(NucleusSpringBootApplication.class, args);
	}

}
