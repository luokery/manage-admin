package com.example.manageadmin.service;

import com.example.manageadmin.model.bo.ProjectPageBO;
import com.example.manageadmin.model.dto.project.ProjectCreateDTO;
import com.example.manageadmin.mapper.ProjectMapper;
import com.example.manageadmin.model.po.Page;
import com.example.manageadmin.model.po.Project;
import com.example.manageadmin.model.vo.PageDTO;
import com.example.manageadmin.model.vo.project.ProjectQueryDTO;
import com.example.manageadmin.model.dto.project.ProjectResponseDTO;
import com.example.manageadmin.model.dto.project.ProjectUpdateDTO;
import com.example.manageadmin.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 项目服务
 * 使用 Redis 缓存项目数据
 */
@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "projects")
public class ProjectService {
    
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    
    /**
     * 获取所有项目列表
     * 缓存项目列表数据
     */
    @Cacheable(key = "'all'", unless = "#result == null || #result.isEmpty()")
    public List<ProjectResponseDTO> getAllProjects() {
        log.debug("从数据库加载所有项目");
        return projectMapper.toResponseDTOList(projectRepository.findAll());
    }

    /**
     * 分页查询项目列表
     * 支持多条件查询和排序
     */
    public PageDTO<ProjectResponseDTO, ProjectQueryDTO> queryProjects(PageDTO<ProjectResponseDTO, ProjectQueryDTO> query) {
        log.debug("分页查询项目: {}", query);

        ProjectQueryDTO queryParamDTO = query.getDataParam();
        // 验证日期范围
        if (!queryParamDTO.isValidDateRange()) {
            throw new IllegalArgumentException("日期范围无效：起始日期不能晚于结束日期");
        }

        // 验证分页参数
        if (!query.isValidPagination()) {
            throw new IllegalArgumentException("分页参数无效：页码不能为负数，每页大小必须在1-100之间");
        }
        
        Page pagePO = ProjectMapper.INSTANCE.toPagePO(query);
        ProjectPageBO paramPageBO = ProjectMapper.INSTANCE.toPageBO(queryParamDTO);
        
        // 查询数据
        List<Project> projects = projectRepository.queryPageList(pagePO, paramPageBO);
        long total = projectRepository.queryCount(paramPageBO);

        // 转换为 DTO
        List<ProjectResponseDTO> dataList = projectMapper.toResponseDTOList(projects);
//        query.result(dataList, queryParamDTO, query.getPageNumber(), query.getPageSize(), total);
        return PageDTO.of(dataList, queryParamDTO, query.getPageNumber(), query.getPageSize(), total);
    }
    
    /**
     * 根据ID获取项目
     * 缓存单个项目数据
     */
    @Cacheable(key = "#id", unless = "#result == null")
    public ProjectResponseDTO getProjectById(Long id) {
        log.debug("从数据库加载项目: {}", id);
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("项目不存在，ID: " + id));
        return projectMapper.toResponseDTO(project);
    }
    
    /**
     * 搜索项目
     * 不缓存搜索结果
     */
    public List<ProjectResponseDTO> searchProjects(String keyword) {
        log.debug("搜索项目: {}", keyword);
        return projectMapper.toResponseDTOList(projectRepository.search(keyword));
    }
    
    /**
     * 根据状态获取项目
     * 缓存按状态分组的项目列表
     */
    @Cacheable(key = "'status:' + #status", unless = "#result == null || #result.isEmpty()")
    public List<ProjectResponseDTO> getProjectsByStatus(Integer status) {
        log.debug("从数据库加载状态为 {} 的项目", status);
        return projectMapper.toResponseDTOList(projectRepository.findByStatus(status));
    }
    
    /**
     * 创建项目
     * 清除项目列表缓存
     */
    @Transactional
    @Caching(evict = {
            @CacheEvict(key = "'all'"),
            @CacheEvict(key = "'status:' + #dto.status", condition = "#dto.status != null")
    })
    public ProjectResponseDTO createProject(ProjectCreateDTO dto) {
        log.debug("创建项目: {}", dto.getProjectCode());
        // 检查项目编号是否已存在
        if (projectRepository.findByProjectCode(dto.getProjectCode()).isPresent()) {
            throw new RuntimeException("项目编号已存在: " + dto.getProjectCode());
        }
        
        Project project = projectMapper.toEntity(dto);
        projectRepository.insert(project);
        return projectMapper.toResponseDTO(project);
    }
    
    /**
     * 更新项目
     * 清除该项目的缓存和列表缓存
     */
    @Transactional
    @CacheEvict(allEntries = true)
    public ProjectResponseDTO updateProject(Long id, ProjectUpdateDTO dto) {
        log.debug("更新项目: {}", id);
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("项目不存在，ID: " + id));
        
        // 检查项目编号是否被其他项目占用
        if (dto.getProjectCode() != null && !dto.getProjectCode().equals(project.getProjectCode())) {
            projectRepository.findByProjectCode(dto.getProjectCode())
                    .ifPresent(p -> {
                        throw new RuntimeException("项目编号已存在: " + dto.getProjectCode());
                    });
        }
        
        projectMapper.updateEntityFromDTO(dto, project);
        projectRepository.update(project);
        return projectMapper.toResponseDTO(project);
    }
    
    /**
     * 更新项目图片
     * 清除该项目的缓存
     */
    @Transactional
    @CacheEvict(key = "#id")
    public ProjectResponseDTO updateProjectImage(Long id, String imageUrl) {
        log.debug("更新项目图片: {}", id);
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("项目不存在，ID: " + id));
        
        project.setImageUrl(imageUrl);
        project.setUpdatedAt(java.time.LocalDateTime.now());
        projectRepository.update(project);
        return projectMapper.toResponseDTO(project);
    }
    
    /**
     * 删除项目
     * 清除该项目的缓存和列表缓存
     */
    @Transactional
    @CacheEvict(allEntries = true)
    public void deleteProject(Long id) {
        log.debug("删除项目: {}", id);
        if (projectRepository.findById(id).isEmpty()) {
            throw new RuntimeException("项目不存在，ID: " + id);
        }
        projectRepository.deleteById(id);
    }
    
    /**
     * 获取项目总数
     * 缓存统计数据
     */
    @Cacheable(cacheNames = "statistics", key = "'projectTotal'")
    public long getProjectCount() {
        log.debug("统计项目数量");
        return projectRepository.count();
    }
    
    /**
     * 获取指定状态的项目数量
     * 缓存统计数据
     */
    @Cacheable(cacheNames = "statistics", key = "'projectStatus:' + #status")
    public long getProjectCountByStatus(Integer status) {
        log.debug("统计状态 {} 的项目数量", status);
        return projectRepository.countByStatus(status);
    }
}
