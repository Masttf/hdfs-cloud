package com.example.demo.Service;

import lombok.SneakyThrows;

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
            fileSystem.copyFromLocalFile(new Path(inputStream.toString()), path);
        }
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
