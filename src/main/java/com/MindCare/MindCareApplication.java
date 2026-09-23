package com.MindCare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MindCareApplication {

	public static void main(String[] args) {
		SpringApplication.run(MindCareApplication.class, args);
	}

}
