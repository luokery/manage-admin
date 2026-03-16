package com.example.manageadmin.model.po;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class BasePO {
	/**
	 * 创建日期 
	 * 	crtTime 2024-12-31 23:59:59
	 */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    /**
     * 更新时间: 
     * 	updTime 2024-12-31 23:59:59
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    /**
     * 版本号: 
     * 	乐观锁
     */
    private Integer version;
    
    /**
     * 逻辑删除:
     * 	未删除=0, 删除=主键id , 默认为0
     */
    private Long deleteId;
}
