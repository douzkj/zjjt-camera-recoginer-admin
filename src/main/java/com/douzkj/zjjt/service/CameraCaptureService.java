package com.douzkj.zjjt.service;

import com.douzkj.zjjt.entity.CameraDevice;
import com.douzkj.zjjt.mq.recognizer.RecognizerQueue;
import com.douzkj.zjjt.mq.recognizer.RecognizerTask;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 视频采集服务
 */
@Slf4j
@Service
@Data
public class CameraCaptureService {

    private final SignalService signalService;

    private final HikvisionService hikvisionService;

    private final RecognizerQueue recognizerQueue;

    private AtomicBoolean running = new AtomicBoolean(false);


    /**
     * 开始采集
     */
    public void capture() {
        if (running.compareAndSet(false, true)) {
            //获取所有通路以及设备列表
            Map<String, List<CameraDevice>> signalDevices = signalService.getSignalDevices();

            int captureParallelism = signalService.getCaptureParallelism();
            int signalSize = signalDevices.size();
            if (signalSize == 0) {
                return;
            }
            /*
              判断并行度是否是大于通路数量
              大于则取通路数量
              小于则取并行度
             */
            int poolSize = Math.min(captureParallelism, signalSize);
            ExecutorService executorService = Executors.newFixedThreadPool(poolSize);
            for (Map.Entry<String, List<CameraDevice>> signalEntry : signalDevices.entrySet()) {
                String signalId = signalEntry.getKey();
                List<CameraDevice> signalCameras = signalEntry.getValue();
                SignalRunnerContext signalRunnerContext = new SignalRunnerContext(signalId, signalCameras);
                executorService.execute(() -> executorSignal(signalRunnerContext));
            }
        }
    }

    /**
     * 运行通道
     *
     * @param runnerContext
     */
    public void executorSignal(SignalRunnerContext runnerContext) {
        //依次获取设备的RTSP
        List<CameraDevice> devices = runnerContext.getDevices();
        for (CameraDevice device : devices) {
            String cameraIndexCode = device.getCameraIndexCode();
            // 获取当前视频的rtsp视频流地址
            String cameraRtspUrl = hikvisionService.getCameraRtspUrl(cameraIndexCode);
            if (cameraRtspUrl == null) {
                log.error("cameraRtspUrl is null. cameraIndexCode:{}", cameraIndexCode);
                continue;
            }
            // 封装识别任务
            RecognizerTask task = new RecognizerTask(cameraIndexCode, cameraRtspUrl);
            // 推送至识别队列
            recognizerQueue.deliver(task);
        }
    }



    @Data
    public static class SignalRunnerContext implements Serializable {
        private final String signalId;
        private final List<CameraDevice> devices;
    }

}

