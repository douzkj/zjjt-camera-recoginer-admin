package com.douzkj.zjjt.web.vo;

import com.douzkj.zjjt.entity.enums.SignalType;
import com.douzkj.zjjt.repository.entity.SignalConfig;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 通路VO
 *
 * @author ranger dong
 * @date 21:23 2025/3/23
 * @descrption
 * @copyright Spotter.ink
 */
@Data
public class SignalVO implements Serializable {

    /**
     * 通路ID
     */
    private Long id;

    /**
     * 通路名称
     */
    private String name;

    private String description;

    /**
     * 设备数量
     */
    private Integer cameraCnt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Integer status;

    private SignalType type;

    private SignalConfig config;

    private Long openedAtMs;
    private Long closedAtMs;
    private Long latestClosedAtMs;
}
