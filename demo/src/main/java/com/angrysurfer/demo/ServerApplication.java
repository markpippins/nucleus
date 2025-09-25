package com.angrysurfer.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// @SpringBootApplication
@SpringBootApplication(scanBasePackages = {"com.angrysurfer"})
@EnableJpaRepositories(basePackages = "com.angrysurfer")
@EntityScan(basePackages = "com.angrysurfer")
public class ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

}
