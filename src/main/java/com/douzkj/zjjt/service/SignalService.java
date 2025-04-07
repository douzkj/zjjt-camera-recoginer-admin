package com.douzkj.zjjt.service;

import com.douzkj.zjjt.repository.CameraRepository;
import com.douzkj.zjjt.repository.SignalRepository;
import com.douzkj.zjjt.repository.dao.Camera;
import com.douzkj.zjjt.repository.dao.SignalCameraCount;
import com.google.common.collect.Maps;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 设备通路服务
 */
@Component
@Data
public class SignalService {

    private final SignalRepository signalRepository;

    private final CameraRepository cameraRepository;

    /**
     * 获取通路以及设备列表
     *
     * @return
     */
    public Map<Long, List<Camera>> getSignalDevices() {
        List<SignalCameraCount> signalCameraCounts = signalRepository.listCameraCnt();
        if (CollectionUtils.isEmpty(signalCameraCounts)) {
            return Collections.emptyMap();
        }
        Map<Long, List<Camera>> res = Maps.newHashMap();
        for (SignalCameraCount signalCameraCount : signalCameraCounts) {
            Long signalId = signalCameraCount.getId();
            List<Camera> cameras = cameraRepository.listBySignalId(signalId);
            if ( ! CollectionUtils.isEmpty(cameras)) {
                res.put(signalId, cameras);
            }
        }
        return res;
    }


}
