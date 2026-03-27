package com.example.manageadmin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.example.manageadmin.mapper.AuthMapper;
import com.example.manageadmin.model.dto.auth.LoginDTO;
import com.example.manageadmin.model.dto.auth.RegisterDTO;
import com.example.manageadmin.model.dto.user.UserResponseDTO;
import com.example.manageadmin.model.vo.ResponseVO;
import com.example.manageadmin.model.vo.Result;
import com.example.manageadmin.model.vo.auth.LoginVO;
import com.example.manageadmin.model.vo.user.UserVO;
import com.example.manageadmin.service.AuthService;

/**
 * 认证控制器
 * 处理登录、注册、刷新 Token 等请求
 */
@Tag(name = "认证管理", description = "用户登录、注册、Token 刷新等接口")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * 用户登录
     */
    @Operation(summary = "用户登录", description = "使用用户名和密码登录，返回 JWT Token")
    @PostMapping("/login")
    public ResponseVO<LoginVO> login(@Valid @RequestBody LoginDTO loginDTO) {
    	
		String token = authService.login( loginDTO);
        UserResponseDTO userDTO = authService.getCurrentUser( loginDTO.getUsername());
        
        UserVO userVO = AuthMapper.INSTANCE.toUserVO(userDTO);
        
        LoginVO data = new LoginVO();
        data.setToken(token);
        data.setUser(userVO);
        
        return Result.success(data, "登录成功");
    }

    /**
     * 用户注册
     */
    @Operation(summary = "用户注册", description = "注册新用户账号")
    @PostMapping("/register")
    public ResponseVO<UserVO> register(@Valid @RequestBody RegisterDTO registerDTO) {
    	
        UserResponseDTO userDTO = authService.register(registerDTO);
        
        UserVO userVO = AuthMapper.INSTANCE.toUserVO(userDTO);
        
        return Result.success(userVO, "注册成功");
    }

    /**
     * 刷新 Token
     */
    @Operation(summary = "刷新Token", description = "使用有效的 Token 刷新获取新 Token")
    @PostMapping("/refresh")
    public ResponseVO<LoginVO> refreshToken(@RequestHeader("Authorization") String authHeader) {
        String oldToken = authHeader.replace("Bearer ", "");
        String newToken = authService.refreshToken(oldToken);
        
        LoginVO data = new LoginVO();
        data.setToken(newToken);
        
        return Result.success(data, "Token 刷新成功");
    }

    /**
     * 获取当前用户信息
     */
    @Operation(summary = "获取当前用户", description = "获取当前登录用户的信息")
    @GetMapping("/me")
    public ResponseVO<UserVO> getCurrentUser() {
        UserResponseDTO userDTO = authService.getCurrentUser();
        
        UserVO userVO = AuthMapper.INSTANCE.toUserVO(userDTO);
        
        return Result.success(userVO, "获取用户信息成功");
    }

    /**
     * 登出（客户端删除 Token 即可）
     */
    @Operation(summary = "登出", description = "用户登出（客户端删除 Token）")
    @PostMapping("/logout")
    public ResponseVO<Void> logout() {
        return Result.success(null, "登出成功");
    }
}
