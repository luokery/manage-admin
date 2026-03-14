package com.example.manageadmin.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    
    @Value("${springdoc.security-schemes.name:bearer-key}")
    private String securitySchemesName;
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("用户管理系统 API")
                        .version("1.0.0")
                        .description("基于 Spring Boot11 + MyBatis + MapStruct 的用户管理系统 RESTful API 文档\n\n" +
                                "## 认证说明\n" +
                                "- 大部分接口需要 JWT Token 认证\n" +
                                "- 请先调用 `/api/auth/login` 或 `/api/auth/register` 获取 Token\n" +
                                "- 在 Swagger UI 中点击右上角 'Authorize' 按钮，输入 `Bearer <your_token>` 进行认证"
                        		+ "")
                        .contact(new Contact()
                                .name("开发团队")
                                .email("dev@example.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .addSecurityItem(new SecurityRequirement()
                		.addList(securitySchemesName))
                .components(new Components()
                		.addSecuritySchemes(securitySchemesName, createAPIKeyScheme()))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("开发环境")
                ));
    }
    
    private SecurityScheme createAPIKeyScheme() {
    	return new SecurityScheme()
			.name("Authorization")
			.type(SecurityScheme.Type.HTTP)
			.scheme("Bearer")
			.in(SecurityScheme.In.HEADER)
			.bearerFormat("JWT");
    }
    
    @Bean
    public OpenApiCustomizer openApiCustomizer() {
        return openApi -> {
            // 添加全局安全方案描述
            openApi.getPaths().values().forEach(pathItem -> {
                pathItem.readOperations().forEach(operation -> {
                    // 为需要认证的接口添加说明
                    if (operation.getTags() != null && 
                        operation.getTags().stream().noneMatch(tag -> 
                            tag.equals("认证管理"))) {
                        operation.description(operation.getDescription() + "\n\n**需要认证**: 是");
                    }
                });
            });
        };
    }
}
