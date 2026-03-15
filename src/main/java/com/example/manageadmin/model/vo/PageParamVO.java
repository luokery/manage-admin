package com.example.manageadmin.model.vo;


import com.example.manageadmin.validation.QueryGroup;
import com.example.manageadmin.validation.QueryPageGroup;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PageParamVO<T> {
	/**
	 * 页面号
	 * 查询的页号
	 */
	@Schema(description = "当前页码(范围1-n)", example = "1", defaultValue = "1")
	@NotNull(groups = {QueryGroup.class, QueryPageGroup.class}, message = "页面号不能为空")
	@Min(value = 1, groups = {QueryGroup.class, QueryPageGroup.class}, message = "页面号不能:最小不能小于1")
	private int pageNumber = 0;
	
	/**
	 * 页大小
	 */
	@Schema(description = "每页大小(范围1-1000)", example = "10", defaultValue = "10")
	@NotNull(groups = {QueryGroup.class, QueryPageGroup.class}, message = "页大小不能为空")
	@Min(value=1, groups = {QueryGroup.class, QueryPageGroup.class}, message = "页大小不能:最小不能小于1")
	@Max(value=1000, groups = {QueryGroup.class, QueryPageGroup.class}, message = "页大小不能:最大不能大于1000")
	private int pageSize = 0;

    @Schema(description = "数据")
    private T data;
}
