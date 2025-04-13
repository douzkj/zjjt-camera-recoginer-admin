package com.douzkj.zjjt.repository.entity;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author ranger dong
 * @date 23:28 2025/4/6
 * @descrption
 * @copyright Spotter.ink
 */
@Data
public class SignalFrameConfig implements Serializable {

    @NotNull
    @Valid
    private FrameStorageConfig storage;

    @NotNull
    @Valid
    private FrameReadConfig read = FrameReadConfig.DEFAULT;


    @Data
    public static class FrameReadConfig implements Serializable {


        public static final FrameReadConfig DEFAULT;
        static {
            DEFAULT = new FrameReadConfig();
            DEFAULT.setFrameIntervalSeconds(5);
            DEFAULT.setFrameRetryTimes(3);
            DEFAULT.setFrameRetryInterval(1);
            DEFAULT.setFrameWindow(-1);
        }


        /**
         * 帧读取间隔
         */
        @NotNull
        private Integer frameIntervalSeconds;

        /**
         * 读取重试次数
         */
        @NotNull
        private Integer frameRetryTimes;

        /**
         * 读取重试间隔
         */
        @NotNull
        private Integer frameRetryInterval;

        /**
         * 读取窗口大小. 为负数代表不限制
         */
        private Integer frameWindow = -1;
    }


    @Data
    public  static  class FrameStorageConfig implements Serializable {

        @NotNull
        private String frameStoragePath;

        @NotNull
        private String frameImageSuffix = "jpg";
    }

}
