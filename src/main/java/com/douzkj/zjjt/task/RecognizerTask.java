package com.douzkj.zjjt.task;

import com.douzkj.zjjt.repository.entity.SignalConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class RecognizerTask implements Serializable {
    private static final long serialVersionUID = 1L;

    private String taskId;

    /**
     * 监控设备信息
     */
    private CameraDTO camera;

    /**
     * 监控rtsp 信息
     */
    private CameraRtspDTO rtsp;


    private SignalDTO signal;


    /**
     * 任务创建毫秒时间戳
     */
    private Long createdTimeMs;


    public RecognizerTask(String taskId, CameraDTO camera, CameraRtspDTO rtsp, SignalDTO signal) {
        this(taskId, camera, rtsp, signal, null);
    }

    public RecognizerTask(String taskId, CameraDTO camera, CameraRtspDTO rtsp, SignalDTO signal, Long taskCreatedTimeMs) {
        this.taskId = taskId;
        this.camera = camera;
        this.rtsp = rtsp;
        this.signal = signal;
        this.createdTimeMs =  taskCreatedTimeMs == null ? System.currentTimeMillis() : taskCreatedTimeMs;
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CameraDTO implements Serializable {
        /**
         * 监控点唯一标识
         */
        private String indexCode;
        /**
         * 监控点名称
         */
        private String name;

        private String hkMeta;
    }

    @Data
    @NoArgsConstructor
    public static class CameraRtspDTO implements Serializable {

        private String url;

        /**
         * rtsp 地址创建时间
         */
        private Long rtspCreatedTimeMs;


        /**
         * rtsp 地址失效时间. 默认为5min
         */
        private Long rtspExpiredTimeMs;

        public CameraRtspDTO(String url) {
            this(url, null);
        }

        public CameraRtspDTO(String url, Long rtspCreatedTimeMs) {
            this.url = url;
            this.rtspCreatedTimeMs = rtspCreatedTimeMs == null ? System.currentTimeMillis() : rtspCreatedTimeMs;
            this.rtspExpiredTimeMs = this.rtspCreatedTimeMs + 5 * 60 * 1000;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignalDTO implements Serializable {

    	private String signalId;

        private String signalName;

        private SignalConfig config;
    }
}
