package com.example.manageadmin.config;

import java.text.MessageFormat;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.manageadmin.interceptors.SerialNumberInterceptor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	private SerialNumberInterceptor serialNumberInterceptor;
	
	private FileConfig fileConfig;
	
	public WebMvcConfig(SerialNumberInterceptor serialNumberInterceptor, FileConfig fileConfig) {
		this.serialNumberInterceptor = serialNumberInterceptor;
		this.fileConfig = fileConfig;
	}
	
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	
        // 项目图片: 配置静态资源映射，用于访问上传的文件
    	String projectHandler = MessageFormat.format("{0}/**", fileConfig.getProjectUrl());
    	String projectLocations = MessageFormat.format("file:{0}/", fileConfig.getProjectDir());
    	log.info("增加静态文件映射: {}, {}", projectHandler, projectLocations);
//        registry.addResourceHandler( projectHandler).addResourceLocations( projectLocations);
        
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(serialNumberInterceptor)
                // 指定需要拦截的路径，例如拦截所有路径或特定路径
                .addPathPatterns("/api/**") // 拦截所有路径: /api开头的
                // .excludePathPatterns("/ignore/&zwnj;**"); // 排除某些路径，例如/ignore/**&zwnj;路径下的请求不会被拦截
                ;
    }
}
