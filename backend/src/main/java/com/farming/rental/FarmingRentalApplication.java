package com.farming.rental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main Spring Boot Application Class
 * Entry point for the Farming Equipment Rental System
 */
@SpringBootApplication
@EnableMongoAuditing
@EnableScheduling
@EnableCaching
public class FarmingRentalApplication {

    public static void main(String[] args) {
        SpringApplication.run(FarmingRentalApplication.class, args);
    }
}
