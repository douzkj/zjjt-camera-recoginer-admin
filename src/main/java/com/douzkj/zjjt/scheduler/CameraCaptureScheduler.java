package com.douzkj.zjjt.scheduler;

import com.douzkj.zjjt.service.CameraCaptureService;
import com.douzkj.zjjt.service.SignalService;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@Data
public class CameraCaptureScheduler {

    private final CameraCaptureService cameraCaptureService;

    private final SignalService signalService;

    // 动态周期配置（单位：s），可通过配置中心动态修改
    private volatile long captureIntervalSeconds = 5 * 60;

    // 定时任务执行器
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    // 初始化后立即启动调度
    @PostConstruct
    public void init() {
        scheduleTask();
    }

    /**
     * 动态调整周期的方法
     * @param newIntervalSeconds 新间隔时间（分钟）
     */
    public void updateInterval(int newIntervalSeconds) {
        this.captureIntervalSeconds = newIntervalSeconds;
        // 取消现有任务，重新调度
        scheduler.shutdownNow();
        scheduleTask();
    }

    private void scheduleTask() {
        scheduler.scheduleAtFixedRate(
                this::executeCapture,
                0,
                captureIntervalSeconds,
                TimeUnit.SECONDS
        );
    }

    /**
     * 实际执行采集的方法
     */
    private void executeCapture() {
        cameraCaptureService.capture();
    }
}