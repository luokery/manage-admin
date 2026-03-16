package com.example.manageadmin.repository;

import org.apache.ibatis.annotations.*;

import com.example.manageadmin.model.po.Project;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ProjectRepository extends BaseRepository<Project>{
    
    /**
     * ****************************************************************************************
     * 创建
     */
	// 初始化version=1, delete_id=0
    @Insert("INSERT INTO projects (project_code, project_name, description, image_url, status, start_date, end_date, created_at, updated_at, version, delete_id) " +
            "VALUES (#{projectCode}, #{projectName}, #{description}, #{imageUrl}, #{status}, #{startDate}, #{endDate}" 
            + ", #{version}, #{deleteId}" 
            + ", #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Project project);
	
    /**
     * ****************************************************************************************
     * 删除
     */
//    @Delete("DELETE FROM projects WHERE id = #{id}")
    @Update("UPDATE projects SET status = #{status}, " +
            "updated_at = #{updatedAt}, delete_id = #{id}, version = version + 1 WHERE id = #{id} and version= #{version}")
    int deleteById(Project project);
    /**
     * ****************************************************************************************
     * 修改
     */
    @Update("UPDATE projects SET project_code = #{projectCode}, project_name = #{projectName}, " +
            "description = #{description}, image_url = #{imageUrl}, status = #{status}, " +
            "start_date = #{startDate}, end_date = #{endDate}"
            + ", version = version + 1 , updated_at = #{updatedAt} "
            + "WHERE id = #{id} and version= #{version}")
    int update(Project project);
    
    /**
     * ****************************************************************************************
     * 查询
     */
    
    /**
     * ****************************************************************************************
     * 未整理(无用)
     */
	
    @Select("SELECT * FROM projects ORDER BY created_at DESC")
    List<Project> findAll();
    
    @Select("SELECT * FROM projects WHERE id = #{id}")
    Optional<Project> findById(Long id);
    
    @Select("SELECT * FROM projects WHERE project_code = #{projectCode}")
    Optional<Project> findByProjectCode(String projectCode);
    
    @Select("SELECT * FROM projects WHERE project_name LIKE CONCAT('%', #{keyword}, '%') OR project_code LIKE CONCAT('%', #{keyword}, '%')")
    List<Project> search(String keyword);
    
    @Select("SELECT * FROM projects WHERE status = #{status} ORDER BY created_at DESC")
    List<Project> findByStatus(Integer status);
    
    @Select("SELECT COUNT(*) FROM projects")
    long count();
    
    @Select("SELECT COUNT(*) FROM projects WHERE status = #{status}")
    long countByStatus(Integer status);

}
