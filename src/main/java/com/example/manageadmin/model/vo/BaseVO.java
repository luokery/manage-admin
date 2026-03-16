package com.example.manageadmin.model.vo;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.manageadmin.validation.DeleteGroup;
import com.example.manageadmin.validation.UpdateGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * CamelCase策略,Java对象属性:personId,序列化后属性:persionId
 * PascalCase策略，Java对象属性：personId，序列化后属性：PersonId
 * SnakeCase策略，Java对象属性：personId，序列化后属性：person_id
 * KebabCase策略，Java对象属性：personId，序列化后属性：person-id
 * @author kunkka
 */
@Data
//@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "基础公共字段")
public abstract class BaseVO {
	
	/**
	 * 创建日期 crtTime
	 */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建日期", example = "2024-01-01 10:20:30")
    private LocalDateTime createdAt;

    /**
     * 更新时间: updTime
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "修改日期", example = "2024-12-31 23:59:59")
    private LocalDateTime updatedAt;
    
    @NotBlank(message = "版本号不能为空", groups = {UpdateGroup.class, DeleteGroup.class})
    @Schema(description = "版本号: 乐观锁", example = "123")
    private Integer version;
    
    @Schema(description = "逻辑删除: 0是未删除, 删除为对应数据记录的ID", example = "0")
    private Long deleteId;
}
