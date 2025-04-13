package com.douzkj.zjjt.repository;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.douzkj.zjjt.repository.dao.Camera;
import com.douzkj.zjjt.repository.mapper.CameraMapper;
import com.google.common.collect.Maps;
import lombok.Data;
import org.assertj.core.util.Lists;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public Map<Long, Long> countBySignalIds(List<Long> signalIds) {
        if (signalIds == null || signalIds.isEmpty()) {
            return Collections.emptyMap();
        }
//        Wrapper<Camera> wrapper = QueryWrapper<>.in(Camera::getSignalId, signalIds).groupBy(Camera::getSignalId).select("signal_id", "count(*)");
//        List<Map<String, Object>> maps = getBaseMapper().selectMaps(wrapper);
//        Map<Long, Long> res = Maps.newHashMap();
        return Collections.emptyMap();
    }

    public List<Camera> getCamerasBySignalId(Long signalId) {
        return list(Wrappers.<Camera>lambdaQuery().eq(Camera::getSignalId, signalId));
    }
}
