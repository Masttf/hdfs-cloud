package com.example.demo.Service;

import lombok.SneakyThrows;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class HdfsService {

    @Autowired
    private FileSystem fileSystem;

    // 上传文件到 HDFS
    @SneakyThrows
    public void uploadFile(MultipartFile file, String hdfsPath) {
        try (InputStream inputStream = file.getInputStream()) {
            Path path = new Path(hdfsPath + "/" + file.getOriginalFilename());
            // 使用 create 方法创建文件并写入数据
            try (FSDataOutputStream outputStream = fileSystem.create(path)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
        }
    }

    @SneakyThrows
    public boolean createDir(String hdfsPath) {
        Path path = new Path(hdfsPath);
        return fileSystem.mkdirs(path);
    }
    
    // 下载文件
    @SneakyThrows
    public InputStream downloadFile(String hdfsFilePath) {
        Path path = new Path(hdfsFilePath);
        return fileSystem.open(path);
    }

    // 列出目录文件
    @SneakyThrows
    public List<String> listFiles(String hdfsDir) {
        List<String> files = new ArrayList<>();
        FileStatus[] fileStatuses = fileSystem.listStatus(new Path(hdfsDir));
        if (fileStatuses != null) { // 防止空指针异常
            for (FileStatus status : fileStatuses) {
                files.add(status.getPath().getName());
            }
        }
        return files;
    }

    // 删除文件
    @SneakyThrows
    public void deleteFile(String hdfsFilePath) {
        fileSystem.delete(new Path(hdfsFilePath), true);
    }
}
