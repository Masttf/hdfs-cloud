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
import com.example.demo.entity.fileInfo;

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
    public List<fileInfo> listFiles(String hdfsDir) {
        List<fileInfo> files = new ArrayList<>();
        FileStatus[] fileStatuses = fileSystem.listStatus(new Path(hdfsDir));
        if (fileStatuses != null) { // 防止空指针异常
            for (FileStatus status : fileStatuses) {
                files.add(new fileInfo(
                    status.getPath().getName(),
                    status.isDirectory(),
                    status.getLen()
                ));
            }
        }
        return files;
    }

    // 删除文件
    @SneakyThrows
    public void deletePath(String hdfsFilePath) {
        fileSystem.delete(new Path(hdfsFilePath), true);
    }

    // 获取文件元数据
    @SneakyThrows
    public fileInfo getFileMetadata(String hdfsFilePath) {
        Path path = new Path(hdfsFilePath);
        FileStatus status = fileSystem.getFileStatus(path);
        return new fileInfo(
            status.getPath().getName(),
            status.isDirectory(),
            status.getLen()
        );
    }
}
