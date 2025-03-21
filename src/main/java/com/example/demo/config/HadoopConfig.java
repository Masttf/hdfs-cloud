package com.example.demo.config;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.net.URI;

@org.springframework.context.annotation.Configuration
public class HadoopConfig {

    @Bean
    public FileSystem hdfsFileSystem() throws IOException, InterruptedException {
        Configuration conf = new Configuration();
        conf.set("dfs.replication", "1"); // 副本数设为1（测试环境）
        return FileSystem.get(URI.create("hdfs://localhost:9000"), conf, "your_username");
    }
}
