package com.example.manageadmin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.manageadmin.config.FileConfig;
import com.example.manageadmin.model.dto.file.FileUploadResponseDTO;
import com.example.manageadmin.model.po.FileAccessType;
import com.example.manageadmin.model.po.FileInfo;
import com.example.manageadmin.repository.FileRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 文件上传服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    private final FileConfig fileConfig;

    /**
     * 上传文件
     *
     * @param file         文件
     * @param accessType   访问类型
     * @param businessType 业务类型
     * @param businessId   业务ID
     * @param uploaderId   上传者ID
     * @return 文件上传响应
     */
    @Transactional
    public FileUploadResponseDTO uploadFile(MultipartFile file, FileAccessType accessType,
                                            String businessType, Long businessId, Long uploaderId) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }

        // 获取文件信息
        String originalFilename = file.getOriginalFilename();
        String contentType = file.getContentType();
        long fileSize = file.getSize();
        String extension = getFileExtension(originalFilename);

        // 生成存储文件名
        String storedFilename = UUID.randomUUID().toString() + extension;

        // 确定存储路径
        String subDir = accessType.getCode();
        Path uploadPath = Paths.get(fileConfig.getUploadDir(), subDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 保存文件
        Path filePath = uploadPath.resolve(storedFilename);
        file.transferTo(filePath.toFile());

        // 生成访问URL
        String accessUrl = accessType.getUrlPrefix() + storedFilename;

        // 保存文件信息
        LocalDateTime now = LocalDateTime.now();
        FileInfo fileInfo = FileInfo.builder()
                .originalFilename(originalFilename)
                .storedFilename(storedFilename)
                .filePath(filePath.toString())
                .accessUrl(accessUrl)
                .fileSize(fileSize)
                .contentType(contentType)
                .extension(extension)
                .accessType(accessType)
                .businessType(businessType)
                .businessId(businessId)
                .uploaderId(uploaderId)
                .createdAt(now)
                .updatedAt(now)
                .deleteId(0l)
                .build();

        fileRepository.insert(fileInfo);

        log.info("文件上传成功: id={}, accessType={}, originalFilename={}, accessUrl={}",
                fileInfo.getId(), accessType, originalFilename, accessUrl);

        return FileUploadResponseDTO.builder()
                .id(fileInfo.getId())
                .originalFilename(originalFilename)
                .storedFilename(storedFilename)
                .accessUrl(accessUrl)
                .fileSize(fileSize)
                .contentType(contentType)
                .accessType(accessType.getCode())
                .businessType(businessType)
                .build();
    }

    /**
     * 获取文件信息
     */
    public FileInfo getFileInfo(Long fileId) {
        return fileRepository.findById(fileId).orElse(null);
    }

    /**
     * 根据存储文件名获取文件信息
     */
    public FileInfo getFileInfoByStoredName(String storedFilename, FileAccessType accessType) {
    	
//    	Map<Long, FileInfo> fileStore = new ConcurrentHashMap<>();
//        fileStore.values().stream()
//                .filter(f -> f.getStoredFilename().equals(storedFilename) 
//                        && f.getAccessType() == accessType
//                        && !(0 == f.getDeleteId()) )
//                .findFirst()
//                .orElse(null);
        return fileRepository.findByStoredFilenameAndAccessType(storedFilename, accessType.getCode()).orElse(null);
    }

	/**
	 * 根据存储文件名获取文件信息（不限访问类型）
	 */
	public FileInfo getFileInfoByStoredName(String storedFilename) {
	    return fileRepository.findByStoredFilename(storedFilename).orElse(null);
	}
    
    /**
     * 获取文件内容
     */
    public byte[] getFileContent(FileInfo fileInfo) throws IOException {
        Path filePath = Paths.get(fileInfo.getFilePath());
        if (!Files.exists(filePath)) {
            throw new IOException("文件不存在: " + fileInfo.getStoredFilename());
        }
        return Files.readAllBytes(filePath);
    }

    /**
     * 删除文件
     */
    @Transactional
    public boolean deleteFile(Long fileId) {
    	
        FileInfo fileInfo = fileRepository.findById(fileId).orElse(null);
        if (fileInfo == null) {
            return false;
        }
        
        // 逻辑删除数据库记录
        int rows = fileRepository.deleteById(fileId);
        if (rows == 0) {
            return false;
        }
        
        log.info("文件已删除(逻辑, 需要管理员删除): id={}, filename={}", fileId, fileInfo.getStoredFilename());
        
        // 删除物理文件
//        try {
//            Path filePath = Paths.get(fileInfo.getFilePath());
//            Files.deleteIfExists(filePath);
//            log.info("文件已删除: id={}, filename={}", fileId, fileInfo.getStoredFilename());
//        } catch (IOException e) {
//            log.warn("删除物理文件失败: {}", e.getMessage());
//        }
        return true;
    }

    

	/**
	 * 获取业务关联的文件列表
	 */
	public List<FileInfo> getFilesByBusiness(String businessType, Long businessId) {
	    return fileRepository.findByBusiness(businessType, businessId);
	}
	
	/**
	 * 获取用户上传的文件列表
	 */
	public List<FileInfo> getFilesByUploader(Long uploaderId) {
	    return fileRepository.findByUploaderId(uploaderId);
	}

    /**
     * 获取所有文件列表
     */
    public List<FileInfo> getAllFiles() {
        return fileRepository.findAll();
    }

    /**
     * 统计文件数量
     */
    public long count() {
        return fileRepository.count();
    }
    
    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }
}
