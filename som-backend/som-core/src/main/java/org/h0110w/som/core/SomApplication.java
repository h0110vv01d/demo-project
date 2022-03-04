package org.h0110w.som.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SomApplication {
	public static void main(String[] args) {
		SpringApplication.run(SomApplication.class, args);
	}
}
