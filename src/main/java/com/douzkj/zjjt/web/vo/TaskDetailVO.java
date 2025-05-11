package com.douzkj.zjjt.web.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class TaskDetailVO implements Serializable {
    private Long id;
    private String cameraIndexCode;
    private String cameraName;
    private String cameraAddr;
    private String regionPathName;
    private String regionIndexCode;
    private String taskId;
    private Long signalId;
    private String frameImagePath;
    private Long frameTimeMs;
    private String labelImagePath;
    private String labelJsonPath;
    private String labelTypes;
    private Long labelTimeMs;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
