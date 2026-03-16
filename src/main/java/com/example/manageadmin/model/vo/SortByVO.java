package com.example.manageadmin.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "排序参数字段")
public abstract class SortByVO {
	
    @Schema(description = "排序字段", example = "createdAt", allowableValues = {"createdAt", "updatedAt"})
    private String sortBy = "createdAt";

    @Schema(description = "排序方向", example = "DESC", allowableValues = {"ASC", "DESC"}, defaultValue = "DESC")
    private String sortDirection = "DESC";

    /**
     * 获取排序方向（默认 DESC）
     */
    public String getSortDirection() {
        if ("ASC".equalsIgnoreCase(sortDirection)) {
            return "ASC";
        }
        return "DESC";
    }

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
