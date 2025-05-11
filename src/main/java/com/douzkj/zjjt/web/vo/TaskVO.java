package com.douzkj.zjjt.web.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class TaskVO implements Serializable {

    private Long id;
    private String taskId;
    private Integer status;
    private Long openedAtMs;
    private Long closedAtMs;
    private Long frameImageCnt;
    private Long labelImageCnt;
    private Long labelJsonCnt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
