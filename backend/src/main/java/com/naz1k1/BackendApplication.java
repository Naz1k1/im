package com.naz1k1;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.naz1k1.mapper")
@Slf4j
public class BackendApplication {
    public static void main(String[] args) {
        try {
            log.info("Starting Backend Application...");
            SpringApplication.run(BackendApplication.class, args);
            log.info("Backend Application started successfully!");
        } catch (Exception e) {
            log.error("Application failed to start", e);
            e.printStackTrace();
        }
    }
}
