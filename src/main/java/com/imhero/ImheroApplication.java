package com.imhero;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ImheroApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImheroApplication.class, args);
	}

}
