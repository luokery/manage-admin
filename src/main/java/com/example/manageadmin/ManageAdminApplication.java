package com.example.manageadmin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.manageadmin.repository")
public class ManageAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManageAdminApplication.class, args);
    }
}
