package com.example.manageadmin.model.vo.project;


import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.manageadmin.model.vo.SortByVO;
import com.example.manageadmin.validation.QueryPageGroup;
import com.example.manageadmin.validation.project.ProjectSortBy;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Schema(description = "项目分页查询参数")
@ProjectSortBy(message = "项目排序字段无效, 或不在排序字段范围.", groups = {QueryPageGroup.class})
public class ProjectPageQueryVO extends SortByVO {
	
    @Schema(description = "项目编号（模糊匹配）", example = "PRJ-2024")
    private String projectCode;

    @Schema(description = "项目名称（模糊匹配）", example = "智慧")
    private String projectName;

    @Schema(description = "项目状态：0-已暂停，1-进行中，2-已完成", example = "1", allowableValues = {"0", "1", "2"})
    private Integer status;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "开始日期（起始范围）", example = "2024-01-01 00:00:00")
    private LocalDateTime startDateFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "开始日期（结束范围）", example = "2024-12-31 23:59:59")
    private LocalDateTime startDateTo;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "结束日期（起始范围）", example = "2024-01-01 00:00:00")
    private LocalDateTime endDateFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "结束日期（结束范围）", example = "2024-12-31 23:59:59")
    private LocalDateTime endDateTo;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间（起始范围）", example = "2024-01-01 00:00:00")
    private LocalDateTime createdAtFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间（结束范围）", example = "2024-12-31 23:59:59")
    private LocalDateTime createdAtTo;

    @Schema(description = "关键词（同时匹配编号和名称）", example = "智慧城市")
    private String keyword;
    
    /**
     * 验证日期范围是否有效
     */
    @JsonIgnore
    public boolean isValidDateRange() {
        // 验证开始日期范围
        if (startDateFrom != null && startDateTo != null && startDateFrom.isAfter(startDateTo)) {
            return false;
        }
        // 验证结束日期范围
        if (endDateFrom != null && endDateTo != null && endDateFrom.isAfter(endDateTo)) {
            return false;
        }
        // 验证创建时间范围
        if (createdAtFrom != null && createdAtTo != null && createdAtFrom.isAfter(createdAtTo)) {
            return false;
        }
        return true;
    }
}
