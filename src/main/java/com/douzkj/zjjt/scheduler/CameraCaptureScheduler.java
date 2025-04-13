//package com.douzkj.zjjt.scheduler;
//
//import com.douzkj.zjjt.service.CameraCaptureService;
//import com.douzkj.zjjt.service.SignalService;
//import com.douzkj.zjjt.service.SysConfigService;
//import lombok.Data;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
//@Component
//@Data
//public class CameraCaptureScheduler {
//
//    private final CameraCaptureService cameraCaptureService;
//
//    private final SignalService signalService;
//
//    private final SysConfigService configService;
//
//    // 动态周期配置（单位：s），可通过配置中心动态修改
//    private volatile long captureIntervalSeconds;
//
//    // 定时任务执行器
//    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
//
//    // 初始化后立即启动调度
//    @PostConstruct
//    public void init() {
//        captureIntervalSeconds = configService.getCaptureIntervalSeconds();
////        scheduleTask();
//    }
//
//
////
////    private void scheduleTask() {
////        scheduler.scheduleAtFixedRate(
////                this::executeCapture,
////                0,
////                captureIntervalSeconds,
////                TimeUnit.SECONDS
////        );
////    }
//
////    /**
////     * 实际执行采集的方法
////     */
////    private void executeCapture() {
////        cameraCaptureService.capture();
////    }
//}