package com.douzkj.zjjt.mq.recognizer;

import com.douzkj.zjjt.entity.CameraDevice;
import lombok.Data;

import java.io.Serializable;

@Data
public class RecognizerTask implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 设备唯一标识
     */
    private String cameraIndexCode;

    private CameraDevice camera;

    /**
     * 监控rtsp 地址
     */
    private String rtspUrl;


    /**
     * rtsp 地址创建时间
     */
    private Long rtspCreatedTime;


    /**
     * rtsp 地址失效时间. 默认为5min
     */
    private Long rtspExpiredTime;


    public RecognizerTask(String cameraIndexCode, String rtspUrl) {
        this(cameraIndexCode, rtspUrl, null);
    }

    public RecognizerTask(String cameraIndexCode, String rtspUrl, Long rtspCreatedTime) {
        this.cameraIndexCode = cameraIndexCode;
        this.rtspUrl = rtspUrl;
        this.rtspCreatedTime = rtspCreatedTime == null ? System.currentTimeMillis() : rtspCreatedTime;
        this.rtspExpiredTime = this.rtspCreatedTime + 5 * 60 * 1000;
    }
}
