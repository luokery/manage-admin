package com.example.manageadmin.controller;

import com.example.manageadmin.cosnst.ProjectEnum;
import com.example.manageadmin.mapper.ProjectMapper;
import com.example.manageadmin.model.dto.project.ProjectDeleteDTO;
import com.example.manageadmin.model.dto.project.ProjectResponseDTO;
import com.example.manageadmin.model.dto.project.ProjectUpdateDTO;
import com.example.manageadmin.model.vo.PageDTO;
import com.example.manageadmin.model.vo.PageParamVO;
import com.example.manageadmin.model.vo.PageVO;
import com.example.manageadmin.model.vo.ResponseVO;
import com.example.manageadmin.model.vo.Result;
import com.example.manageadmin.model.vo.project.ProjectCreateVO;
import com.example.manageadmin.model.vo.project.ProjectDeleteVO;
import com.example.manageadmin.model.vo.project.ProjectPageQueryVO;
import com.example.manageadmin.model.vo.project.ProjectUpdateVO;
import com.example.manageadmin.service.ProjectService;
import com.example.manageadmin.validation.AddGroup;
import com.example.manageadmin.validation.QueryPageGroup;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Tag(name = "项目管理", description = "项目的增删改查和图片上传接口")
@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProjectController {
    
    private final ProjectService projectService;
    
    @Value("${file.project-dir:/tmp/uploads/projects}")
    private String projectsDir;
    @Value("${file.project-url:/tmp/uploads/projects}")
    private String projectUrl;
    
    /**
     * ****************************************************************************************
     * 创建
     */
    @Operation(summary = "创建项目", description = "创建新项目")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "项目创建成功"),
            @ApiResponse(responseCode = "400", description = "参数验证失败或项目编号已存在")
    })
    @PostMapping
    public ResponseVO<ProjectResponseDTO> createProject(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "项目创建信息", required = true)
            @Validated(AddGroup.class) @RequestBody ProjectCreateVO createDTO) {
        ProjectResponseDTO project = projectService.createProject(createDTO);
        return Result.success(project, "创建项目成功");
    }
    
    /**
     * ****************************************************************************************
     * 删除
     */
    @Operation(summary = "删除项目", description = "根据项目ID删除项目")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "项目删除成功"),
            @ApiResponse(responseCode = "400", description = "项目不存在")
    })
    @DeleteMapping("/{id}")
    public ResponseVO<Void> deleteProject(
            @Parameter(description = "项目ID", required = true) @PathVariable Long id, @ParameterObject ProjectDeleteVO deleteVO) {
//    	
    	ProjectDeleteDTO deleteDTO = ProjectMapper.INSTANCE.toDeleteDTO(deleteVO);
        projectService.deleteProject(deleteDTO);
        return Result.success(null, "删除项目成功");
    }
    
    /**
     * ****************************************************************************************
     * 修改
     */
    @Operation(summary = "更新项目", description = "根据项目ID更新项目信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "项目更新成功"),
            @ApiResponse(responseCode = "400", description = "项目不存在或参数验证失败")
    })
    @PutMapping("/{id}")
    public ResponseVO<ProjectResponseDTO> updateProject(
            @Parameter(description = "项目ID", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "项目更新信息", required = true)
            @Valid @RequestBody ProjectUpdateVO paramVO) {
    	ProjectUpdateDTO dto = ProjectMapper.INSTANCE.toDTO(paramVO);
    	
        ProjectResponseDTO project = projectService.updateProject(dto);
        return Result.success(project, "更新项目成功");
    }
    
    @Operation(summary = "上传项目图片", description = "为项目上传图片")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "图片上传成功"),
            @ApiResponse(responseCode = "400", description = "项目不存在或文件上传失败")
    })
    @PostMapping("/{id}/image")
    public ResponseVO<Map<String, String>> uploadProjectImage(
            @Parameter(description = "项目ID", required = true) @PathVariable Long id,
            @Parameter(description = "项目图片文件", required = true) @RequestParam("file") MultipartFile file) {
        
        if (file.isEmpty()) {
            return Result.build(400, "请选择要上传的文件");
        }
        
        try {
            // 检查文件类型
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return Result.build(400, "只能上传图片文件");
            }
            
            // 创建上传目录
            Path uploadPath = Paths.get(projectsDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".") 
                    ? originalFilename.substring(originalFilename.lastIndexOf(".")) 
                    : ".jpg";
            String newFilename = UUID.randomUUID().toString() + extension;
            
            // 保存文件
            Path filePath = uploadPath.resolve(newFilename);
//            file.transferTo(filePath.toFile());
            file.transferTo( new File(filePath.toUri()) );
            // 生成访问URL
            String imageUrl = MessageFormat.format( "{0}/{1}", projectUrl, newFilename);
            
            // 更新项目图片URL
            projectService.updateProjectImage(id, imageUrl);
            
            Map<String, String> data = new HashMap<>();
            data.put("imageUrl", imageUrl);
            data.put("filename", newFilename);
            
            return Result.success(data, "图片上传成功");
            
        } catch (IOException e) {
            return Result.build(500, "文件上传失败: " + e.getMessage(), null);
        }
    }
    /**
     * ****************************************************************************************
     * 查询
     */
    @Operation(summary = "分页查询项目", description = "支持多条件查询、分页和排序")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功获取项目列表"),
            @ApiResponse(responseCode = "400", description = "参数验证失败")
    })
    @PostMapping("/page")
    public ResponseVO< PageVO<ProjectResponseDTO>> queryPageProjects( @Validated(QueryPageGroup.class) @RequestBody PageParamVO<ProjectPageQueryVO> paramPageVO) {
        
    	PageDTO<ProjectResponseDTO, ProjectPageQueryVO> pageDTO = ProjectMapper.INSTANCE.toPageDTO(paramPageVO);
    	pageDTO.setDataParam( paramPageVO.getData());
    	
        PageDTO<ProjectResponseDTO, ProjectPageQueryVO> resultDTO = projectService.queryProjects( pageDTO);
        
        PageVO<ProjectResponseDTO> result = ProjectMapper.INSTANCE.toPageVO(resultDTO);
        
        return Result.success(result, "查询成功");
    }
    
    @Operation(summary = "获取项目详情", description = "根据项目ID获取项目详细信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功获取项目详情"),
            @ApiResponse(responseCode = "400", description = "项目不存在")
    })
    @GetMapping("/{id}")
    public ResponseVO<ProjectResponseDTO> getProjectById(
            @Parameter(description = "项目ID", required = true) @PathVariable Long id) {
        ProjectResponseDTO project = projectService.getProjectById(id);
        return Result.success( project, ProjectEnum.Details_SUCCESS);
    }
    
    /**
     * ****************************************************************************************
     * @TODO 无用功能
     */
    @Operation(summary = "获取项目数量", description = "获取系统中项目的总数")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功获取项目数量")
    })
    @GetMapping("/count")
    public ResponseVO<Map<String, Object>> getProjectCount() {
        Map<String, Object> data = new HashMap<>();
        data.put("total", projectService.getProjectCount());
        data.put("inProgress", projectService.getProjectCountByStatus(1));
        data.put("completed", projectService.getProjectCountByStatus(2));
        data.put("paused", projectService.getProjectCountByStatus(0));
        return Result.success(data, "获取项目数量成功");
    }
    
    @Operation(summary = "获取项目列表", description = "获取所有项目的列表信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功获取项目列表")
    })
    @GetMapping
    public ResponseVO<List<ProjectResponseDTO>> getAllProjects() {
        List<ProjectResponseDTO> projects = projectService.getAllProjects();
        return Result.success( projects, ProjectEnum.LIST_SUCCESS);
    }
    
    @Operation(summary = "搜索项目", description = "根据关键词搜索项目（支持项目名称和编号模糊匹配）")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "搜索成功")
    })
    @GetMapping("/search")
    public ResponseVO<List<ProjectResponseDTO>> searchProjects(
            @Parameter(description = "搜索关键词", required = true) @RequestParam String keyword) {
        List<ProjectResponseDTO> projects = projectService.searchProjects(keyword);
        return Result.success( projects, "搜索项目成功");
    }
    
    @Operation(summary = "按状态获取项目", description = "根据状态获取项目列表")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功获取项目列表")
    })
    @GetMapping("/status/{status}")
    public ResponseVO<List<ProjectResponseDTO>> getProjectsByStatus(
            @Parameter(description = "状态：1-进行中，2-已完成，0-已暂停", required = true) @PathVariable Integer status) {
        List<ProjectResponseDTO> projects = projectService.getProjectsByStatus(status);
        return Result.success(projects, "获取项目列表成功");
    }
}

