package com.douzkj.zjjt.service;

import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONUtil;
import com.douzkj.zjjt.repository.CameraRepository;
import com.douzkj.zjjt.repository.SignalRepository;
import com.douzkj.zjjt.repository.dao.Camera;
import com.douzkj.zjjt.repository.dao.Signal;
import com.douzkj.zjjt.repository.entity.SignalConfig;
import com.douzkj.zjjt.task.RecognizerQueue;
import com.douzkj.zjjt.task.RecognizerTask;
import com.douzkj.zjjt.task.TaskConvertor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 视频采集服务
 */
@Slf4j
@Service
@Data
public class CameraCaptureService {

    private final CameraRepository cameraRepository;

    private final SignalRepository signalRepository;

    private final HikvisionService hikvisionService;

    private final RecognizerQueue recognizerQueue;

    private AtomicBoolean running = new AtomicBoolean(false);


    public static String generateTaskId() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return LocalDateTime.now(ZoneId.of("Asia/Shanghai")).format(formatter);
    }


    public void capture(Long signalId) {
        Signal signal = signalRepository.getById(signalId);
        if (signal == null) {
            log.warn("signal {} not exist", signalId);
            return;
        }
        if (!signal.isOpened()) {
            log.warn("signal {} is not opened", signalId);
            return;
        }
        List<Camera> signalCameras = cameraRepository.getCamerasBySignalId(signal.getId());
        SignalRunnerContext signalRunnerContext = new SignalRunnerContext(generateTaskId(), signal, signalCameras);
        execute(signalRunnerContext);
    }

    /**
     * 运行通道
     *
     * @param runnerContext
     */
    public void execute(SignalRunnerContext runnerContext) {
        //依次获取设备的RTSP
        List<Camera> devices = runnerContext.getDevices();
        for (Camera device : devices) {
            String cameraIndexCode = device.getIndexCode();
            // 获取当前视频的rtsp视频流地址
            String cameraRtspUrl = hikvisionService.getCameraRtspUrl(cameraIndexCode);
            if (cameraRtspUrl == null) {
                log.error("cameraRtspUrl is null. cameraIndexCode:{}", cameraIndexCode);
                continue;
            }
            log.info("获取设备【{}-{}】的RTSP地址为:{}", device.getIndexCode(), device.getName(), cameraRtspUrl);
            RecognizerTask.CameraDTO cameraDTO = TaskConvertor.INSTANCE.do2TaskDTO(device);
            RecognizerTask.CameraRtspDTO rtspDTO = new RecognizerTask.CameraRtspDTO(cameraRtspUrl);
            RecognizerTask.SignalDTO signalDTO = null;
            if (runnerContext.getSignal() != null) {
                signalDTO = new RecognizerTask.SignalDTO();
                signalDTO.setSignalId(runnerContext.getSignal().getId().toString());
                signalDTO.setSignalName(runnerContext.getSignal().getName());
                signalDTO.setConfig(JSONUtil.toBean(runnerContext.getSignal().getConfig(), SignalConfig.class));
            }
            // 封装识别任务
            RecognizerTask task = new RecognizerTask(runnerContext.getRunnerId(), cameraDTO, rtspDTO, signalDTO);
            // 推送至识别队列
            recognizerQueue.deliver(task);
        }
    }



    @Data
    public static class SignalRunnerContext implements Serializable {
        private final String runnerId;
        private final List<Camera> devices;
        private final Signal signal;

        public SignalRunnerContext(String runnerId, Signal signal, List<Camera> devices) {
            this.runnerId = runnerId != null ? runnerId : UUID.fastUUID().toString();
            this.signal = signal;
            this.devices = devices;
        }

        public SignalRunnerContext( Signal signal, List<Camera> devices) {
            this(null, signal, devices);
        }
    }

}

