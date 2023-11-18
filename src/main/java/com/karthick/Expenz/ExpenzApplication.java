package com.karthick.Expenz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ExpenzApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpenzApplication.class, args);
	}

}
