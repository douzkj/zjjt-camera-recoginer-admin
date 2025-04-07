package com.douzkj.zjjt.web.vo;

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

    /**
     * 设备数量
     */
    private Integer cameraCnt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
