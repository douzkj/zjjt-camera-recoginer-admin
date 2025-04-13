package com.douzkj.zjjt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.douzkj.zjjt.**.mapper")
@EnableScheduling
@EnableAsync
public class VideoRecognizerApplication {
    public static void main(String[] args) {
        SpringApplication.run(VideoRecognizerApplication.class, args);
    }
}