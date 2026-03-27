package com.example.manageadmin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
public class FileConfig {
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
}
