package com.naz1k1.utils;

import com.naz1k1.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Component
public class FileUtils {

    private static final String AVATAR_PATH = "static/image/avatar";
    
    @Value("${file.upload.maxSize:5242880}") //5MB
    private long maxFileSize;

    private static final String[] ALLOWED_IMAGE_TYPES = {
            "image/jpeg", "image/png", "image/gif", "image/webp"
    };

    /**
     * 获取项目根路径
     * @return 项目根路径
     */
    private String getProjectRootPath() {
        try {
            File path = new File(ResourceUtils.getURL("classpath:").getPath());
            return path.getParentFile().getParentFile().getAbsolutePath();
        } catch (FileNotFoundException e) {
            throw new BusinessException("获取项目路径失败");
        }
    }

    /**
     * 上传头像
     * @param file 文件
     * @return 文件访问路径
     */
    public String uploadAvatar(MultipartFile file) {
        // 验证文件
        validateImageFile(file);

        // 创建年月日目录
        String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String relativePath = "/" + AVATAR_PATH + "/" + datePath;
        Path uploadDir = Paths.get(getProjectRootPath() + relativePath);

        try {
            // 创建目录
            Files.createDirectories(uploadDir);

            // 生成文件名
            String originalFilename = file.getOriginalFilename();
            String extension = null;
            if (originalFilename != null) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String filename = UUID.randomUUID().toString() + extension;

            // 保存文件
            Path filePath = uploadDir.resolve(filename);
            file.transferTo(filePath.toFile());

            // 返回相对路径（用于访问的URL路径）
            return relativePath + "/" + filename;
        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new BusinessException("文件上传失败");
        }
    }

    /**
     * 验证图片文件
     * @param file 文件
     */
    private void validateImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("文件不能为空");
        }

        // 检查文件大小
        if (file.getSize() > maxFileSize) {
            throw new BusinessException("文件大小不能超过" + (maxFileSize / 1024 / 1024) + "MB");
        }

        // 检查文件类型
        String contentType = file.getContentType();
        boolean isValidType = false;
        for (String type : ALLOWED_IMAGE_TYPES) {
            if (type.equals(contentType)) {
                isValidType = true;
                break;
            }
        }
        if (!isValidType) {
            throw new BusinessException("不支持的文件类型");
        }
    }

    /**
     * 删除旧头像
     * @param avatarPath 头像路径
     */
    public void deleteOldAvatar(String avatarPath) {
        if (avatarPath != null && avatarPath.startsWith("/" + AVATAR_PATH + "/")) {
            try {
                Path filePath = Paths.get(getProjectRootPath() + avatarPath);
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                log.error("删除旧头像失败: {}", avatarPath, e);
            }
        }
    }

    /**
     * 确保头像目录存在
     */
    public void ensureAvatarDirectoryExists() {
        try {
            Path avatarDir = Paths.get(getProjectRootPath(), AVATAR_PATH);
            if (!Files.exists(avatarDir)) {
                Files.createDirectories(avatarDir);
                log.info("创建头像目录: {}", avatarDir);
            }
        } catch (IOException e) {
            log.error("创建头像目录失败", e);
            throw new BusinessException("创建头像目录失败");
        }
    }
} 