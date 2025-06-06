package com.naz1k1;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.naz1k1.mapper")
public class BackendApplication {
    public static void main(String[] args) {
       try {
           SpringApplication.run(BackendApplication.class,args);
       } catch (Exception e) {
           e.printStackTrace();
       }
    }
}
