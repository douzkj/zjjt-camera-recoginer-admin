package com.douzkj.zjjt.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class CameraDevice implements Serializable {

    /**
     * 监控点唯一标识
     */
    private String cameraIndexCode;
    /**
     * 监控点名称
     */
    private String cameraName;
}
