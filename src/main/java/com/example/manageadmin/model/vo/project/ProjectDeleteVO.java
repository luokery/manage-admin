package com.example.manageadmin.model.vo.project;


import com.example.manageadmin.validation.DeleteGroup;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "项目删除请求")
public class ProjectDeleteVO {
	
    @NotBlank(message = "项目编号不能为空", groups = {DeleteGroup.class})
    private Long id;

    @NotBlank(message = "项目版本不能为空", groups = {DeleteGroup.class})
    @Schema(description = "项目版本", example = "1", defaultValue = "1")
    private Integer version;
}
