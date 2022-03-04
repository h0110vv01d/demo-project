package org.h0110w.som.test_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TestServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestServiceApplication.class, args);
    }
}
