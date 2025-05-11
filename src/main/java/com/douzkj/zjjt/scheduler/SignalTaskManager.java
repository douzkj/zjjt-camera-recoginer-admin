package com.douzkj.zjjt.scheduler;

import com.douzkj.zjjt.repository.SignalRepository;
import com.douzkj.zjjt.repository.dao.Signal;
import com.douzkj.zjjt.service.CameraCaptureService;
import com.douzkj.zjjt.service.CameraService;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@Data
public class SignalTaskManager {

    private final Map<Long, ScheduledExecutorService> signalTasks = Maps.newConcurrentMap();

    private final static long intervalSeconds = 5;

    private final CameraCaptureService cameraCaptureService;

    private final SignalRepository signalRepository;

    private final CameraService cameraService;


    // 共享的 ScheduledExecutorService，线程数量可以根据实际情况调整
//    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(Math.max(Runtime.getRuntime().availableProcessors() * 2, 16));


    /**
     * 在 Bean 初始化完成后，将所有通路添加到 signalTasks 中
     */
    @PostConstruct
    @Async("asyncExecutor")
    public void init() {
        try {
            // 假设 signalRepository 有一个方法可以获取所有信号 ID
            List<Signal> allSignals = signalRepository.getOpenedSignals();
            log.info("可执行的通路：{}", allSignals.stream().map(Signal::getId).collect(Collectors.toList()));
            for (Signal signal : allSignals) {
                addTask(signal);
            }
            log.info("所有通路任务已添加到 signalTasks 中");
        } catch (Exception e) {
            log.error("初始化通路任务时出错", e);
        }
    }


    public void addTask(Long signalId) {
        if (!signalTasks.containsKey(signalId)) {
            Signal signal = signalRepository.getById(signalId);
            addTask(signal);
        }
    }

    public void addTask(Signal signal) {
        if (signal != null) {
            if (signalTasks.putIfAbsent(signal.getId(), createTask(signal)) == null) {
                log.info("成功为通路 {} 添加任务", signal.getId());
            } else {
                log.warn("通路 {} 任务已存在，跳过添加", signal.getId());
            }
        }
    }

    protected ScheduledExecutorService createTask(Signal signal) {
        log.info("添加通路采集任务：{}", signal.getId());
        Long signalId = signal.getId();
        ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutor
                .scheduleAtFixedRate(
                        () -> {
                            cameraService.syncRtspUrl(signalId);
                        },
                        1,
                        intervalSeconds
                        ,
                        TimeUnit.SECONDS
                );
        return scheduledExecutor;
    }

    public void removeTask(@NotNull(message = "通道不能为空") Long signalId) {
        ScheduledExecutorService remove = signalTasks.remove(signalId);
        if (remove != null) {
            shutdownScheduler(remove);
            log.info("成功移除通路 {} 的任务并取消调度", signalId);
        }
    }

    public void shutdownScheduler(ExecutorService executorService) {
        try {
            executorService.shutdown();
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }

    /**
     * 在 Bean 销毁时关闭线程池
     */
    @PreDestroy
    public void shutdown() {
        for (Map.Entry<Long, ScheduledExecutorService> scheduledFutureEntry : signalTasks.entrySet()) {
            ScheduledExecutorService scheduler = scheduledFutureEntry.getValue();
            try {
                shutdownScheduler(scheduler);
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

