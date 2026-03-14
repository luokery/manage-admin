package com.example.manageadmin.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(title = "请求VO", description = "请求VO")
@Data
public class RequestVO<T> {
	/**
	 * 流水号: 请求发送的流水号, 与返回的流水号对应
	 */
	@Schema(title = "流水号", description = "请求发送的流水号, 与返回的流水号对应")
	private String serialNumber;
	
	/**
	 * 请求的数据: 对应的DTO
	 */
	@Valid
	@NotNull(message = "请求数据不能为空")
	private T data;
	
}
