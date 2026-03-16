package com.example.manageadmin.model.dto.project;

import lombok.Data;


@Data
public class ProjectDeleteDTO {
    
    /**
     * 项目编号
     */
    private Long id;
    
    /**
     * 项目版本
     */
    private Integer version;
}
