package com.example.manageadmin.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.manageadmin.model.po.Page;


public interface BaseRepository<T> {
	
	/**
	 * 查询分页列表
	 */
	 List<T> queryPageList(@Param("page") Page pagePO, @Param("po") T entityPO);
	 
	/**
	 * 查询数据总数
	 */
	 long queryCount(@Param("po") T entityPO);
}
