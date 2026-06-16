package com.leoyoung.backend.controller;

import com.leoyoung.backend.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * 文件上传 Controller —— 处理商品图片上传
 * 保存到 static/images/product/，返回可访问的 URL
 */
@RestController
@RequestMapping("/api/admin")
public class UploadController {

    /** 上传到 classpath 输出目录（target/classes/static/），Spring Boot 从这里提供静态资源 */
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/target/classes/static/images/product/";

    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return Result.error(null, "文件为空");
        }
        String originalName = file.getOriginalFilename();
        String ext = originalName != null && originalName.contains(".")
                ? originalName.substring(originalName.lastIndexOf(".")) : ".jpg";
        String newName = UUID.randomUUID().toString().substring(0, 8) + ext;

        // 先保存到 target（Spring Boot 实际读取位置）
        File targetDir = new File(UPLOAD_DIR);
        if (!targetDir.exists()) targetDir.mkdirs();
        File targetFile = new File(targetDir, newName);
        file.transferTo(targetFile);

        // 再复制到 src（防止下次编译丢失），用 Files.copy 避免 transferTo 不能重复调的问题
        File srcDir = new File(System.getProperty("user.dir") + "/src/main/resources/static/images/product/");
        if (!srcDir.exists()) srcDir.mkdirs();
        Files.copy(targetFile.toPath(), new File(srcDir, newName).toPath(), StandardCopyOption.REPLACE_EXISTING);

        String url = "http://localhost:8080/images/product/" + newName;
        return Result.success(url, "上传成功");
    }
}
