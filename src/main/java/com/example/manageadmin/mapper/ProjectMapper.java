package com.example.manageadmin.mapper;

import com.example.manageadmin.model.bo.ProjectPageBO;
import com.example.manageadmin.model.dto.project.ProjectCreateDTO;
import com.example.manageadmin.model.po.Page;
import com.example.manageadmin.model.po.Project;
import com.example.manageadmin.model.vo.PageDTO;
import com.example.manageadmin.model.vo.PageParamVO;
import com.example.manageadmin.model.vo.PageVO;
import com.example.manageadmin.model.vo.project.ProjectQueryDTO;
import com.example.manageadmin.model.dto.project.ProjectResponseDTO;
import com.example.manageadmin.model.dto.project.ProjectUpdateDTO;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    
	ProjectMapper INSTANCE = Mappers.getMapper( ProjectMapper.class );
	
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    @Mapping(target = "status", constant = "1")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    Project toEntity(ProjectCreateDTO dto);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    void updateEntityFromDTO(ProjectUpdateDTO dto, @MappingTarget Project entity);
    
    @Mapping(target = "statusName", expression = "java(getStatusName(entity.getStatus()))")
    ProjectResponseDTO toResponseDTO(Project entity);
    
    List<ProjectResponseDTO> toResponseDTOList(List<Project> entities);
    
    default String getStatusName(Integer status) {
        if (status == null) return "未知";
        return switch (status) {
            case 1 -> "进行中";
            case 2 -> "已完成";
            case 0 -> "已暂停";
            default -> "未知";
        };
    }
//====================================================
	PageDTO<ProjectResponseDTO, ProjectQueryDTO> toPageDTO(PageParamVO<ProjectQueryDTO> pageParamVO);

	PageVO<ProjectResponseDTO> toPageVO(PageDTO<ProjectResponseDTO, ProjectQueryDTO> resultDTO);

	Page toPagePO(PageDTO<ProjectResponseDTO, ProjectQueryDTO> query);

	ProjectPageBO toPageBO(ProjectQueryDTO queryParamDTO);
}
