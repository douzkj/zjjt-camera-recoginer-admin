package com.douzkj.zjjt.infra.hikvision.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author ranger dong
 * @date 00:10 2025/3/30
 * @descrption
 * @copyright Spotter.ink
 */
@Component
@ConfigurationProperties("hikvision.sync")
@Data
public class HikvisionSyncConfig {

    private Long cameraIntervalSeconds = 60 * 60L;
}
