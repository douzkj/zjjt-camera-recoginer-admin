package com.douzkj.zjjt.repository;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.douzkj.zjjt.repository.dao.Camera;
import com.douzkj.zjjt.repository.entity.SignalCameraCntVO;
import com.douzkj.zjjt.repository.mapper.CameraMapper;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author ranger dong
 * @date 22:29 2025/3/23
 * @descrption
 * @copyright Spotter.ink
 */
@Repository
@Data
public class CameraRepository extends ServiceImpl<CameraMapper, Camera> {


    public boolean unbind(List<Long> cameraIds) {
        LambdaUpdateWrapper<Camera> set = Wrappers.<Camera>lambdaUpdate()
                .in(Camera::getId, cameraIds)
                .set(Camera::getSignalId, null)
                .set(Camera::getUpdatedAt, LocalDateTime.now());
        return super.update(set);
    }

    public boolean bind(List<Long> cameraIds, Long signalId) {
        LambdaUpdateWrapper<Camera> set = Wrappers.<Camera>lambdaUpdate()
                .in(Camera::getId, cameraIds)
                .set(Camera::getSignalId, signalId)
                .set(Camera::getUpdatedAt, LocalDateTime.now());
        return super.update(set);
    }

    public List<Camera> listBySignalId(Long signalId) {
        return signalId == null ? Collections.emptyList() : list(Wrappers.<Camera>lambdaQuery().eq(Camera::getSignalId, signalId));
    }

    public Map<Long, Integer> countBySignalIds(List<Long> signalIds) {
        if (signalIds == null || signalIds.isEmpty()) {
            return Collections.emptyMap();
        }
        Wrapper<Camera> wrapper = Wrappers.<Camera>lambdaQuery().in(Camera::getSignalId, signalIds);
        List<SignalCameraCntVO> signalCameraCntVOS = getBaseMapper().countBySignalId(wrapper);
        if (signalCameraCntVOS == null || signalCameraCntVOS.isEmpty()) {
            return Collections.emptyMap();
        }
        return signalCameraCntVOS.stream().collect(java.util.stream.Collectors.toMap(SignalCameraCntVO::getSignalId, SignalCameraCntVO::getCameraCnt));
    }

    public List<Camera> getCamerasBySignalId(Long signalId) {
        return list(Wrappers.<Camera>lambdaQuery().eq(Camera::getSignalId, signalId));
    }

    public List<Camera> getCamerasBySignalIds(List<Long> signalIds) {
        return list(Wrappers.<Camera>lambdaQuery().in(Camera::getSignalId, signalIds));
    }


    public Camera getByIndexCode(String indexCode) {
        return getOne(Wrappers.<Camera>lambdaQuery().eq(Camera::getIndexCode, indexCode), false);
    }

    public boolean updateRtspUrl(Long id, String rtspUrl, long rtspCreatedTimeMs, long rtspExpiredTimeMs) {
        LambdaUpdateWrapper<Camera> set = Wrappers.<Camera>lambdaUpdate()
                .eq(Camera::getId, id)
                .set(Camera::getLatestRtspUrl, rtspUrl)
                .set(Camera::getLatestRtspCreatedTime, rtspCreatedTimeMs)
                .set(Camera::getLatestRtspExpiredTime, rtspExpiredTimeMs);
        return this.update(set);
    }
}
