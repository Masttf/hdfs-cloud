package com.example.demo.config;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@org.springframework.context.annotation.Configuration
public class HadoopConfig {

    @Bean
    public FileSystem hdfsFileSystem() throws IOException {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://localhost:9000");
        conf.set("hadoop.home.dir", "/usr/local/hadoop");
        return FileSystem.get(conf);
    }
}
