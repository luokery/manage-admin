package com.example.manageadmin.model.vo.project;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDateTime;

import com.example.manageadmin.validation.UpdateGroup;
import com.example.manageadmin.validation.project.ProjectStatus;

@Data
@Schema(description = "项目更新请求")
public class ProjectUpdateVO {
    
    @Schema(description = "项目编号", example = "PRJ-2024-001")
    @Size(max = 50, message = "项目编号最大50个字符")
    private String projectCode;
    
    @Schema(description = "项目名称", example = "智慧城市项目")
    @Size(min = 2, max = 100, message = "项目名称长度必须在2-100个字符之间")
    private String projectName;
    
    @Schema(description = "项目描述", example = "这是一个智慧城市建设项目的描述")
    private String description;
    
    @Schema(description = "项目图片URL")
    private String imageUrl;
    
    @ProjectStatus(message = "项目状态值无效，必须是 0-新建，1-待审批，2-进行中, 3-已完成", groups = {UpdateGroup.class})
    @Schema(description = "项目状态：0-新建，1-待审批，2-进行中, 3-已完成", example = "1", allowableValues = {"0", "1", "2", "2"}, defaultValue = "1")
    private Integer status;
    
    @Schema(description = "开始日期", example = "2024-01-01T00:00:00")
    private LocalDateTime startDate;
    
    @Schema(description = "结束日期", example = "2024-12-31T23:59:59")
    private LocalDateTime endDate;
    
    @NotBlank(message = "项目编号不能为空", groups = {UpdateGroup.class})
    private Long id;

    @NotBlank(message = "项目版本不能为空", groups = {UpdateGroup.class})
    @Schema(description = "项目版本", example = "1", defaultValue = "1")
    private Integer version;

}
