package com.douzkj.zjjt.infra.hikvision.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("hikvision.artemis")
@Data
public class ArtemisConfigProps {
    /**
     * host地址
     */
    private String host;
    /**
     * app key
     */
    private String appKey;
    /**
     * app secret
     */
    private String appSecret;

    /**
     * OpenAPI接口的上下文
     */
    private String path = "/artemis";

    /**
     * 接口协议
     */
    private String schema = "https";
}
