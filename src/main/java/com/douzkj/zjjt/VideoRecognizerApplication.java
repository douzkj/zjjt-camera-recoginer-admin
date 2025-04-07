package com.douzkj.zjjt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.douzkj.zjjt.**.mapper")
public class VideoRecognizerApplication {
    public static void main(String[] args) {
        SpringApplication.run(VideoRecognizerApplication.class, args);
    }
}