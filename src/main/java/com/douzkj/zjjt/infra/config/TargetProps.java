package com.douzkj.zjjt.infra.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("target")
@Data
public class TargetProps {

    private String classification;
    private String statistic;
}
