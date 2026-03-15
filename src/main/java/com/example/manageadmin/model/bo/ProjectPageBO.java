package com.example.manageadmin.model.bo;

import java.time.LocalDateTime;

import com.example.manageadmin.model.po.Project;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class ProjectPageBO extends Project {
	
	// 开始日期（起始范围）
    private LocalDateTime startDateFrom;

    // 开始日期（结束范围）
    private LocalDateTime startDateTo;

    // 结束日期（起始范围）
    private LocalDateTime endDateFrom;

    // 结束日期（结束范围）
    private LocalDateTime endDateTo;

    // 创建时间（起始范围）
    private LocalDateTime createdAtFrom;

    // 创建时间（结束范围）
    private LocalDateTime createdAtTo;

    // 关键词（同时匹配编号和名称）
    private String keyword;
    
    // 排序字段
    private String sortBy = "createdAt";

    // 排序方向
    private String sortDirection = "DESC";
    
    /**
     * 获取排序字段（转换为数据库字段名）
     */
    public String getSortBy() {
        if (sortBy == null || sortBy.isEmpty()) {
            return "created_at";
        }
        // 转换 camelCase 到 snake_case
        return sortBy.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
}
