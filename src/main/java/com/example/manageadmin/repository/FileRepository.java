package com.example.manageadmin.repository;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import com.example.manageadmin.model.po.FileInfo;

import java.util.List;
import java.util.Optional;

/**
 * 文件信息数据访问层
 */
@Mapper
@Repository
public interface FileRepository {

    /**
     * 根据ID查询文件信息（未删除）
     */
    @Select("SELECT * FROM file_info WHERE id = #{id} AND delete_id = 0")
    Optional<FileInfo> findById(Long id);

    /**
     * 根据存储文件名查询（未删除）
     */
    @Select("SELECT * FROM file_info WHERE stored_filename = #{storedFilename} AND delete_id = 0")
    Optional<FileInfo> findByStoredFilename(String storedFilename);

    /**
     * 根据存储文件名和访问类型查询（未删除）
     */
    @Select("SELECT * FROM file_info WHERE stored_filename = #{storedFilename} AND access_type = #{accessType} AND delete_id = 0")
    Optional<FileInfo> findByStoredFilenameAndAccessType(@Param("storedFilename") String storedFilename, 
                                                          @Param("accessType") String accessType);

    /**
     * 根据业务类型和业务ID查询文件列表
     */
    @Select("SELECT * FROM file_info WHERE business_type = #{businessType} AND business_id = #{businessId} AND delete_id = 0 ORDER BY created_at DESC")
    List<FileInfo> findByBusiness(@Param("businessType") String businessType, @Param("businessId") Long businessId);

    /**
     * 根据上传者ID查询文件列表
     */
    @Select("SELECT * FROM file_info WHERE uploader_id = #{uploaderId} AND delete_id = 0 ORDER BY created_at DESC")
    List<FileInfo> findByUploaderId(Long uploaderId);

    /**
     * 根据访问类型查询文件列表
     */
    @Select("SELECT * FROM file_info WHERE access_type = #{accessType} AND delete_id = 0 ORDER BY created_at DESC")
    List<FileInfo> findByAccessType(String accessType);

    /**
     * 查询所有未删除的文件
     */
    @Select("SELECT * FROM file_info WHERE delete_id = 0 ORDER BY created_at DESC")
    List<FileInfo> findAll();

    /**
     * 插入文件信息
     */
    @Insert("INSERT INTO file_info (original_filename, stored_filename, file_path, access_url, file_size, " +
            "content_type, extension, access_type, business_type, business_id, uploader_id, created_at, updated_at, delete_id) " +
            "VALUES (#{originalFilename}, #{storedFilename}, #{filePath}, #{accessUrl}, #{fileSize}, " +
            "#{contentType}, #{extension}, #{accessType, typeHandler=org.apache.ibatis.type.EnumTypeHandler}, " +
            "#{businessType}, #{businessId}, #{uploaderId}, #{createdAt}, #{updatedAt}, 0)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(FileInfo fileInfo);

    /**
     * 更新文件信息
     */
    @Update("UPDATE file_info SET original_filename = #{originalFilename}, access_url = #{accessUrl}, " +
            "business_type = #{businessType}, business_id = #{businessId}, updated_at = #{updatedAt} " +
            "WHERE id = #{id} AND delete_id = 0")
    int update(FileInfo fileInfo);

    /**
     * 逻辑删除文件
     */
    @Update("UPDATE file_info SET delete_id = id, updated_at = CURRENT_TIMESTAMP WHERE id = #{id} AND delete_id = 0")
    int deleteById(Long id);

    /**
     * 统计文件数量
     */
    @Select("SELECT COUNT(*) FROM file_info WHERE delete_id = 0")
    long count();

    /**
     * 根据访问类型统计文件数量
     */
    @Select("SELECT COUNT(*) FROM file_info WHERE access_type = #{accessType} AND delete_id = 0")
    long countByAccessType(String accessType);
}
