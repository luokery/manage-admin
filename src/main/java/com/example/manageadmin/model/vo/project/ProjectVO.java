package com.example.manageadmin.model.vo.project;

import java.time.LocalDateTime;

import com.example.manageadmin.validation.AddGroup;
import com.example.manageadmin.validation.DeleteGroup;
import com.example.manageadmin.validation.UpdateGroup;
import com.example.manageadmin.validation.UploadGroup;
import com.example.manageadmin.validation.ValidDateRange;
import com.example.manageadmin.validation.project.ProjectCode;
import com.example.manageadmin.validation.project.ProjectStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 项目创建请求 DTO
 */
@Schema(description = "项目创建请求")
@ValidDateRange(startField = "startDate", endField = "endDate", message = "开始日期必须早于结束日期", groups = {AddGroup.class, UpdateGroup.class})
@Data
public class ProjectVO {
    
	@NotBlank(message = "项目编号不能为空", groups = {DeleteGroup.class, UpdateGroup.class})
	@Schema(description = "项目id")
    private Long id;
    
	@Schema(description = "项目图片", example = "xxxx.jpg")
	@NotBlank(message = "项目图片不能为空", groups = {UploadGroup.class})
    private String imageUrl;
    
    @NotBlank(message = "项目编号不能为空", groups = {AddGroup.class})
    @ProjectCode(message = "项目编号格式不正确，应为 PRJ-YYYY-NNN 格式（如 PRJ-2024-001）", groups = {AddGroup.class, UpdateGroup.class})
    @Schema(description = "项目编号（格式：PRJ-YYYY-NNN，如 PRJ-2024-001）", example = "PRJ-2024-001", required = true)
    private String projectCode;

    @NotBlank(message = "项目名称不能为空", groups = {AddGroup.class})
    @Size(min = 2, max = 100, message = "项目名称长度必须在2-100个字符之间", groups = {AddGroup.class, UpdateGroup.class})
    @Schema(description = "项目名称", example = "智慧城市项目", required = true)
    private String projectName;

    @Size(max = 2000, message = "项目描述最大2000个字符", groups = {AddGroup.class, UpdateGroup.class})
    @Schema(description = "项目描述", example = "这是一个智慧城市建设项目的描述")
    private String description;

    @ProjectStatus(message = "项目状态值无效，必须是 0（已暂停）、1（进行中）或 2（已完成）", groups = {AddGroup.class, UpdateGroup.class})
    @Schema(description = "项目状态：0-已暂停，1-进行中，2-已完成", example = "1", allowableValues = {"0", "1", "2"}, defaultValue = "1")
    private Integer status;

    @Schema(description = "开始日期", example = "2024-01-01T00:00:00")
    private LocalDateTime startDate;

    @Schema(description = "结束日期", example = "2024-12-31T23:59:59")
    private LocalDateTime endDate;
}
