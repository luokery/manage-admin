package com.example.manageadmin.model.pojo;

import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@ConfigurationProperties(prefix = "jwt-properties")
public class JwtProperties {
	
	// 登陆url (API 返回 JSON，不跳转页面)
	private String loginUrl;
	
	// 未授权跳转（API 返回 JSON，不跳转页面）
	private String unauthorizedUrl;
	
	// 不需要认证的路径
	private List<String> excludePaths;
	
	// 公开接口 - 不需要认证
	private List<Map<String, String>> filterChainPublic;
	
//	// 静态资源 - 不需要认证
//	private List<Map<String, String>> filterChainStaticResources;
//
//	// 组件-公共 - 不需要认证
//	private List<Map<String, String>> filterChainComponentPublic;

	// 需要认证
	private List<Map<String, String>> filterChainRequired;

	
}
