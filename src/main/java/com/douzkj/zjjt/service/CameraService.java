package com.douzkj.zjjt.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.douzkj.zjjt.repository.CameraRepository;
import com.douzkj.zjjt.repository.SignalRepository;
import com.douzkj.zjjt.repository.dao.Base;
import com.douzkj.zjjt.repository.dao.Camera;
import com.douzkj.zjjt.repository.dao.Signal;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Data
@Slf4j
public class CameraService {

    private final SignalRepository signalRepository;
    private final CameraRepository cameraRepository;
    private final HikvisionService hikvisionService;
    /**
     * 5分钟过期
     */
    private static final long rtspExpiredSeconds = 5 * 60L;

    /**
     * 安全更新域值
     */
    private static final long syncSafeThresholdSeconds = 30L;

    /**
     * 更新rtsp地址
     *
     * @param signalId 通路ID
     */
    public void syncRtspUrl(Long signalId) {
        log.info("open syncRtsp job. signalId:{}", signalId);
        List<Camera> cameras = cameraRepository.list(
                Wrappers.<Camera>lambdaQuery().eq(Camera::getSignalId, signalId)
                        .and(c -> {
                            c.eq(Camera::getLatestRtspUrl, null)
                                    .or()
                                    .lt(Camera::getLatestRtspExpiredTime, System.currentTimeMillis() + syncSafeThresholdSeconds * 1000);
                        })
                        .select(Camera::getId, Camera::getIndexCode, Camera::getName)
        );
        syncSyncRtspUrl(cameras);
    }

    public void syncSyncRtspUrl(List<Camera> cameras) {
        if (cameras == null || cameras.isEmpty()) {
            return;
        }
        for (Camera camera : cameras) {
            String cameraIndexCode = camera.getIndexCode();
            // 获取当前视频的rtsp视频流地址
            String cameraRtspUrl = hikvisionService.getCameraRtspUrl(cameraIndexCode);
            //更新设备地址
            if (cameraRtspUrl == null) {
                log.error("cameraRtspUrl is null. cameraIndexCode:{}, cameraName: {}", cameraIndexCode, camera.getName());
                continue;
            }
            long rtspCreatedTime = System.currentTimeMillis();
            long rtspExpiredTime = rtspCreatedTime + rtspExpiredSeconds * 1000;
            boolean b = cameraRepository.updateRtspUrl(camera.getId(), cameraRtspUrl, rtspCreatedTime, rtspExpiredTime);
            if ( ! b) {
                log.error("updateRtspUrl fail. cameraIndexCode:{}, cameraName: {}", cameraIndexCode, camera.getName());
            } else {
                log.info("updateRtspUrl success [{} - {}]. new_rtsp: {}", cameraIndexCode, camera.getName(), cameraRtspUrl);
            }
        }
    }


    public void syncRtspUrl() {
        log.info("open batch syncRtsp job");
        List<Signal> signals = signalRepository.getOpenedSignals();
        if (signals == null || signals.isEmpty()) {
            return;
        }
        List<Long> signalIds = signals.stream().map(Base::getId).collect(Collectors.toList());
        List<Camera> cameras = cameraRepository.list(
                Wrappers.<Camera>lambdaQuery().in(Camera::getSignalId, signalIds)
                        .and(c -> {
                            c.eq(Camera::getLatestRtspUrl, null)
                                    .or()
                                    .lt(Camera::getLatestRtspCreatedTime, System.currentTimeMillis() - rtspExpiredSeconds * 1000);
                        })
                        .select(Camera::getId, Camera::getIndexCode, Camera::getName)
        );
        syncSyncRtspUrl(cameras);
    }

}
