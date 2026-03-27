package com.example.manageadmin.model.dto.auth;


import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 登录请求 DTO
 */
@Data
@Schema(description = "登录请求")
public class LoginDTO {
    
    @NotBlank(message = "用户名不能为空")
    @Schema(description = "用户名", example = "admin", requiredMode = RequiredMode.REQUIRED)
    private String username;
    
    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码", example = "123456", requiredMode = RequiredMode.REQUIRED)
    private String password;
}
