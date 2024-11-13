package com.capstone.LEMS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LemsApplication {

	public static void main(String[] args) {
		SpringApplication.run(LemsApplication.class, args);
		System.out.println("Backend is Running");
	}
}
