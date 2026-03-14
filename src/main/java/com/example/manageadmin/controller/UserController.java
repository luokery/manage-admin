package com.example.manageadmin.controller;

import com.example.manageadmin.model.dto.UserCreateDTO;
import com.example.manageadmin.model.dto.UserResponseDTO;
import com.example.manageadmin.model.dto.UserUpdateDTO;
import com.example.manageadmin.model.vo.ResponseVO;
import com.example.manageadmin.model.vo.Result;
import com.example.manageadmin.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "用户管理", description = "用户的增删改查接口")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {
    
    private final UserService userService;
    
    @Operation(summary = "获取用户列表", description = "获取所有用户的列表信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功获取用户列表")
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseVO<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return Result.success(users, "获取用户列表成功");
    }
    
    @Operation(summary = "获取用户详情", description = "根据用户ID获取用户详细信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功获取用户详情"),
            @ApiResponse(responseCode = "400", description = "用户不存在")
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseVO<UserResponseDTO> getUserById(
            @Parameter(description = "用户ID", required = true) @PathVariable Long id) {
        UserResponseDTO user = userService.getUserById(id);
        return Result.success(user, "获取用户详情成功");
    }
    
    @Operation(summary = "搜索用户", description = "根据关键词搜索用户（支持用户名和邮箱模糊匹配）")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "搜索成功")
    })
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseVO<List<UserResponseDTO>> searchUsers(
            @Parameter(description = "搜索关键词", required = true) @RequestParam String keyword) {
        List<UserResponseDTO> users = userService.searchUsers(keyword);
        return Result.success(users, "搜索用户成功");
    }
    
    @Operation(summary = "创建用户", description = "创建新用户")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "用户创建成功"),
            @ApiResponse(responseCode = "400", description = "参数验证失败或用户名/邮箱已存在")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseVO<UserResponseDTO> createUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "用户创建信息", required = true)
            @Valid @RequestBody UserCreateDTO dto) {
        UserResponseDTO user = userService.createUser(dto);
        return Result.success(user, "创建用户成功");
    }
    
    @Operation(summary = "更新用户", description = "根据用户ID更新用户信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "用户更新成功"),
            @ApiResponse(responseCode = "400", description = "用户不存在或参数验证失败")
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseVO<UserResponseDTO> updateUser(
            @Parameter(description = "用户ID", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "用户更新信息", required = true)
            @Valid @RequestBody UserUpdateDTO dto) {
        UserResponseDTO user = userService.updateUser(id, dto);
        return Result.success(user, "更新用户成功");
    }
    
    @Operation(summary = "删除用户", description = "根据用户ID删除用户")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "用户删除成功"),
            @ApiResponse(responseCode = "400", description = "用户不存在")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseVO<Void> deleteUser(
            @Parameter(description = "用户ID", required = true) @PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success(null, "删除用户成功");
    }
    
    @Operation(summary = "获取用户数量", description = "获取系统中用户的总数")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功获取用户数量")
    })
    @GetMapping("/count")
    @ResponseStatus(HttpStatus.OK)
    public ResponseVO<Map<String, Long>> getUserCount() {
        Map<String, Long> data = new HashMap<>();
        data.put("count", userService.getUserCount());
        return Result.success(data, "获取用户数量成功");
    }
}
