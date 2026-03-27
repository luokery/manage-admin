package com.example.manageadmin.controller;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.example.manageadmin.annotation.IgnoreResponseWrap;
import com.example.manageadmin.cosnst.ResultEnum;
import com.example.manageadmin.model.vo.ResponseVO;
import com.example.manageadmin.model.vo.Result;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 统一响应处理 @FIXME: 是否需要
 * 在控制器和统一异常处理, 只用抛出VO类型, 节省ResponseVO响应包装层
 *  如: ResponseVO<UserVO> 改为 UserVO直接返回, 由统一响应处理拦截.
 */
@RestControllerAdvice(basePackages = {"com.example.manageadmin.controller"})
public class ControllerResponseAdvice implements ResponseBodyAdvice<Object> {
	
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		
		// 不处理: ResponseEntity 包装器的时候
		if(returnType.getParameterType().isAssignableFrom(ResponseEntity.class)) {
			return false;
		}
		
		// 不处理: 带有忽略注解 或者 结果response是ResponseVO类型
		// 忽略IgnoreResponseWrap注解标注的
		final IgnoreResponseWrap[] declaredAnnotationsByType = returnType.getExecutable().getDeclaredAnnotationsByType(IgnoreResponseWrap.class);
		return !(declaredAnnotationsByType.length > 0 || returnType.getParameterType().equals(ResponseVO.class));
	}
	
	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
        // String类型不能直接包装
        if (returnType.getGenericParameterType().equals(String.class)) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                // 将数据包装在ResponseVO里后转换为json串进行返回
                return objectMapper.writeValueAsString(Result.build(ResultEnum.SUCCESS, body));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        // 否则直接包装成ResponseVO返回
        return Result.build(ResultEnum.SUCCESS, body);
	}
}