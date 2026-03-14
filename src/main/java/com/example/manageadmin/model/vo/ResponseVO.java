package com.example.manageadmin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 统一API响应格式
 */
@Schema(title = "响应VO", description = "响应VO")
@Data
public class ResponseVO<T> {
    
	/**
	 * 流水号: 请求发送的流水号, 与返回的流水号对应
	 */
	@Schema(title = "流水号", description = "请求发送的流水号, 与返回的流水号对应")
	private String serialNumber;

	/**
	 * 响应代码: 0是成功, 非0失败, 为具体错误代码
	 */
	@Schema(title = "响应代码", example = "200", description = "响应代码: 0是成功, 非0失败, 为具体错误代码")
	private Integer code;
	
	/**
	 * 响应消息: 消息
	 */
	@Schema(title = "响应消息", example = "操作成功", description = "响应消息: 消息")
	private String message;

	/**
	 * 请求的数据: 对应的DTO
	 */
	@Schema(title = "响应数据", description = "响应数据")
	private T data;
}
