package com.example.jeogieottae;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class JeogieottaeApplication {

    public static void main(String[] args) {
        SpringApplication.run(JeogieottaeApplication.class, args);
    }

}
