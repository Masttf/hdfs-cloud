package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Service.HdfsService;

import java.io.InputStream;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/hdfs")
public class HdfsController {

    @Autowired
    private HdfsService hdfsService;

    // 上传文件
    @PostMapping("/upload")
    public String uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(defaultValue = "/cloud_disk") String hdfsPath) {
        hdfsService.uploadFile(file, hdfsPath);
        return "Upload success!";
    }

    @PostMapping("/mkdir")
    public String mkdir(@RequestBody String filePath) {
        if(hdfsService.createDir(filePath)){
            return "创建目录成功";
        }else{
            return "创建目录失败";
        }
    }
    
    // 下载文件
    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadFile(@RequestParam String hdfsFilePath) {
        InputStream inputStream = hdfsService.downloadFile(hdfsFilePath);
        String fileName = hdfsFilePath.substring(hdfsFilePath.lastIndexOf("/") + 1);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(inputStream));
    }

    // 列出文件目录
    @GetMapping("/list")
    public List<String> listFiles(@RequestParam(defaultValue = "/cloud_disk") String hdfsDir) {
        System.out.println("getList: " + hdfsDir);
        return hdfsService.listFiles(hdfsDir);
    }

    // 删除文件
    @DeleteMapping("/delete")
    public String deleteFile(@RequestParam String hdfsFilePath) {
        hdfsService.deleteFile(hdfsFilePath);
        return "Delete success!";
    }
}
