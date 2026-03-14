package com.example.manageadmin.model.vo;

import org.slf4j.MDC;

import com.example.manageadmin.cosnst.ConstSysBase;

/**
 * 结果自定义构建工具
 * @author 
 *
 */
public class Result {
	
	
	/**
	 * 响应构建(code Integer)
	 * @param <T>
	 * @param code  	返回的代码
	 * @param message	返回的消息
	 * @param data  	返回的数据
	 * @return
	 */
	public static <T> ResponseVO<T> build(Integer code, String message, T data){
		ResponseVO<T> responseVO = new ResponseVO<T>();
		responseVO.setCode( code);
		responseVO.setMessage(message);
		responseVO.setData( data);
		responseVO.setSerialNumber( MDC.get( ConstSysBase.SERIAL_NUMBER_KEY));
		return responseVO;
	}
	
	public static <T> ResponseVO<T> build(Integer code, String message){
		return Result.build(code, message, null);
	}
	/**
	 * 响应构建(ResultFace T)
	 * @param <T>
	 * @param faceEnum
	 * @param data
	 * @return
	 */
	public static <T> ResponseVO<T> build(ResultFace faceEnum, T data){
		return Result.build(faceEnum.code(), faceEnum.message(), data);
	}
	/**
	 * 响应构建(ResultFace)
	 * @param <T>
	 * @param faceEnum
	 * @param data
	 * @return
	 */
	public static <T> ResponseVO<T> build(ResultFace faceEnum){
		return Result.build(faceEnum.code(), faceEnum.message());
	}
	
	public static <T> ResponseVO<T> build(ResultFace faceEnum, String message){
		return Result.build(faceEnum.code(), message);
	}
	
	/**
	 * 系统默认成功返回(无数据)
	 * @param <T>
	 * @return
	 */
	public static <T> ResponseVO<T> success(){
		return Result.build(ResultEnum.SUCCESS, null);
	}
	
	/**
	 * 系统默认成功返回(有数据)
	 * @param <T>
	 * @return
	 */
	public static <T> ResponseVO<T> success(T data){
		return Result.build(ResultEnum.SUCCESS, data);
	}
	
	public static <T> ResponseVO<T> success(String message) {
		return Result.build(ResultEnum.SUCCESS, message);
	}
	
	public static <T> ResponseVO<T> success( T data, String message) {
		return Result.build(ResultEnum.SUCCESS.code(), message, data);
	}
	
	public static <T> ResponseVO<T> success( T data, ResultFace faceEnum){
		return Result.build(ResultEnum.SUCCESS, data);
	}
}
