package com.example.manageadmin.model.po;

import lombok.Data;

@Data
public class Page {
	
	/**
	 * 页面号
	 * 查询的页号
	 */
	private int pageNumber = 0;
	
	/**
	 * 每页大小
	 */
	private int pageSize = 0;
	
	/**
	 * 总记录数
	 */
	private long totalElements = 0;
	
	/**
	 * 总页数
	 */
	private int totalPages = 0;
	
	/** 分页点 */
	private int offset = 0;

	public int getOffset() {
		offset = (pageNumber - 1) * pageSize;
		return offset;
	}
}
