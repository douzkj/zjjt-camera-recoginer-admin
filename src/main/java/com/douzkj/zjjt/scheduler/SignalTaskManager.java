package com.douzkj.zjjt.scheduler;

import com.douzkj.zjjt.repository.SignalRepository;
import com.douzkj.zjjt.repository.dao.Signal;
import com.douzkj.zjjt.service.CameraCaptureService;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
@Data
public class SignalTaskManager {

    private final Map<Long, ScheduledFuture<?>> signalTasks = Maps.newConcurrentMap();

    private final static long intervalSeconds = 330;

    private final CameraCaptureService cameraCaptureService;

    private final SignalRepository signalRepository;

    // 共享的 ScheduledExecutorService，线程数量可以根据实际情况调整
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(Math.max(Runtime.getRuntime().availableProcessors() * 2, 16));


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
        if (signal != null && signal.isOpened()) {
            signalTasks.putIfAbsent(signal.getId(), createTask(signal));
        }
    }

    protected ScheduledFuture<?> createTask(Signal signal) {
        log.info("添加通路采集任务：{}", signal.getId());
        return scheduler
                .scheduleAtFixedRate(
                        () -> {
                            cameraCaptureService.capture(signal.getId());
                        },
                        0,
                        intervalSeconds
                        ,
                        TimeUnit.SECONDS
                );
    }

    public void removeTask(@NotNull(message = "通道不能为空") Long signalId) {
        ScheduledFuture<?> remove = signalTasks.remove(signalId);
        if (remove != null) {
            remove.cancel(true);
        }
    }

    /**
     * 在 Bean 销毁时关闭线程池
     */
    @PreDestroy
    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}

