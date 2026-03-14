package com.example.manageadmin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@MapperScan("com.example.manageadmin.repository")
@EnableAspectJAutoProxy
public class ManageAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManageAdminApplication.class, args);
    }
}
