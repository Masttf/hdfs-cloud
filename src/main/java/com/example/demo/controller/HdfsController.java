package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.net.URI;

import com.example.demo.Service.HdfsService;
import com.example.demo.entity.*;
import java.io.InputStream;
import java.util.List;



@RestController
@RequestMapping("/api/hdfs")
public class HdfsController {

    @Autowired
    private HdfsService hdfsService;

    // 上传文件
    @PostMapping("/files")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(defaultValue = "/cloud_disk") String path) {
        try {
            hdfsService.uploadFile(file, path);
            return ResponseEntity.created(URI.create("/api/hdfs/files?path=" + path + "/" + file.getOriginalFilename()))
                    .body("{\"message\":\"File uploaded successfully\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"File upload failed\"}");
        }
    }

    // 创建目录
    @PostMapping("/directories")
    public ResponseEntity<?> createDirectory(@RequestBody myPath path) {
        try {
            boolean created = hdfsService.createDir(path.getPath());
            if (created) {
                return ResponseEntity.created(URI.create("/api/hdfs/directories?path=" + path.getPath()))
                        .body("{\"message\":\"Directory created successfully\"}");
            }
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("{\"error\":\"Directory already exists\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Directory creation failed\"}");
        }
    }
    
    // 下载文件
    @GetMapping("/files")
    public ResponseEntity<InputStreamResource> downloadFile(@RequestParam String path) {
        try {
            InputStream inputStream = hdfsService.downloadFile(path);
            String fileName = path.substring(path.lastIndexOf("/") + 1);
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(inputStream));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 列出目录内容
    @GetMapping("/contents")
    public ResponseEntity<?> listDirectory(@RequestParam(defaultValue = "/cloud_disk") String path) {
        try {
            List<fileInfo> files = hdfsService.listFiles(path);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(files);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"Directory not found\"}");
        }
    }

    // 删除文件或目录
    @DeleteMapping
    public ResponseEntity<?> deleteResource(@RequestBody myPath path) {
        try {
            hdfsService.deletePath(path.getPath());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Deletion failed\"}");
        }
    }

    // 获取文件元数据
    @GetMapping("/metadata")
    public ResponseEntity<?> getFileMetadata(@RequestParam String hdfsFilePath) {
        try {
            fileInfo metadata = hdfsService.getFileMetadata(hdfsFilePath);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(metadata);
        } catch (Exception e) {
            return ResponseEntity.status(404)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"error\":\"File not found\"}");
        }
    }

    @GetMapping("/dir")
    public ResponseEntity<?> isDirectoryExit(@RequestParam String hdfsDirPath) {
        try {
            boolean isExit = hdfsService.isDirectoryExit(hdfsDirPath);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(isExit);
        } catch (Exception e) {
            return ResponseEntity.status(404)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"error\":\"Dir not found\"}");
        }
    }
}
