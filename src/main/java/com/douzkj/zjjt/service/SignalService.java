package com.douzkj.zjjt.service;

import com.douzkj.zjjt.entity.CameraDevice;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 设备通路服务
 */
@Component
public class SignalService {

    /**
     * 获取通路以及设备列表
     *
     * @return
     */
    public Map<String, List<CameraDevice>> getSignalDevices() {
        return null;
    }


    /**
     * 获取通道采集并行度
     */
    public int getCaptureParallelism() {
        //todo 从配置中心获取
        return 10;
    }

    /**
     * 获取采集周期
     * 单位：秒
     * @return
     */
    public long getCaptureIntervalSeconds() {
        //todo 从配置中心获取
        return 5*60;

    }
}
