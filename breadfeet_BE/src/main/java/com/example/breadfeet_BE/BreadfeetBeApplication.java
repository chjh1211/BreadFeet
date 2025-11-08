package com.example.breadfeet_BE;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BreadfeetBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(BreadfeetBeApplication.class, args);
	}

}
