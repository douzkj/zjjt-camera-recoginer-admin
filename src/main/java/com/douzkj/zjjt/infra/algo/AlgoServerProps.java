package com.douzkj.zjjt.infra.algo;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "algo.server")
@Data
public class AlgoServerProps {

    private String endpoint;
}
