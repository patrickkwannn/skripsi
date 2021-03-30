package com.skripsi.skripsiservice;

import org.springframework.boot.SpringApplication;
		import org.springframework.boot.autoconfigure.SpringBootApplication;
		import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class SkripsiServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkripsiServiceApplication.class, args);
	}
}