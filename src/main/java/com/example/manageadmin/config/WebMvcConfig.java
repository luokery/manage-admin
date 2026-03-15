package com.example.manageadmin.config;

import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Value;
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
	
	// # 基础上传目录
    @Value("${file.upload-dir:/tmp/uploads}")
    private String uploadDir;
    @Value("${file.upload-url:/uploads}")
    private String uploadUrl;
    
    // # 公开文件目录
    @Value("${file.public-dir:/public}")
    private String publicDir;
    @Value("${file.public-url:/public}")
    private String publicUrl;
    
    // # 私密文件目录（需要认证）
    @Value("${file.private-dir:/private}")
    private String privateDir;
    @Value("${file.private-url:/private}")
    private String privateUrl;
    
    /**
     * # 项目图片目录 
     * 服务器地址(物理): uploadDir + publicDir|privateDir + projectsDir
     * 			/tmp/uploads/private/projects 
     * 服务器地址(访问): uploadUrl + publicUrl|privateUrl + projectsUrl
     * 			/uploads/private/projects
     */
    @Value("${file.project-dir:/projects}")
    private String projectDir;
    @Value("${file.project-url:/projects}")
    private String projectUrl;
	
	
	public WebMvcConfig(SerialNumberInterceptor serialNumberInterceptor) {
		this.serialNumberInterceptor = serialNumberInterceptor;
	}
	
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	
        // 项目图片: 配置静态资源映射，用于访问上传的文件
    	String projectHandler = MessageFormat.format("{0}/**", projectUrl);
    	String projectLocations = MessageFormat.format("file:{0}/", projectDir);
    	log.info("增加静态文件映射: {}, {}", projectHandler, projectLocations);
        registry.addResourceHandler( projectHandler).addResourceLocations( projectLocations);
        
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
