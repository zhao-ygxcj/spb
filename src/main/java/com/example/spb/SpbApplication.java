package com.example.spb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
@MapperScan("com.example.spb.mapper")
public class SpbApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpbApplication.class, args);
    }

}
