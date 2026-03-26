package com.example.manageadmin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.example.manageadmin.repository")
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableTransactionManagement
public class ManageAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManageAdminApplication.class, args);
    }
}
