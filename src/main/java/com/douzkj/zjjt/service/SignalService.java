package com.douzkj.zjjt.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.douzkj.zjjt.common.SignalConst;
import com.douzkj.zjjt.repository.CameraRepository;
import com.douzkj.zjjt.repository.SignalRepository;
import com.douzkj.zjjt.repository.TaskRepository;
import com.douzkj.zjjt.repository.dao.Camera;
import com.douzkj.zjjt.repository.dao.Signal;
import com.douzkj.zjjt.repository.dao.Task;
import com.douzkj.zjjt.scheduler.SignalTaskManager;
import com.google.common.collect.Maps;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 设备通路服务
 */
@Component
@Data
public class SignalService {

    private final SignalRepository signalRepository;

    private final CameraRepository cameraRepository;

    private final SignalTaskManager signalTaskManager;

    private final TaskRepository taskRepository;


    /**
     * 获取通路以及设备列表
     *
     * @return
     */
    public Map<Long, List<Camera>> getSignalDevices() {
        List<Signal> signalCameraCounts = signalRepository.list();
        if (CollectionUtils.isEmpty(signalCameraCounts)) {
            return Collections.emptyMap();
        }
        Map<Long, List<Camera>> res = Maps.newHashMap();
        for (Signal signal : signalCameraCounts) {
            Long signalId = signal.getId();
            List<Camera> cameras = cameraRepository.listBySignalId(signalId);
            if ( ! CollectionUtils.isEmpty(cameras)) {
                res.put(signalId, cameras);
            }
        }
        return res;
    }

    public List<Camera> getCameras(Long signalId) {
        return cameraRepository.list(Wrappers.<Camera>lambdaQuery().eq(Camera::getSignalId, signalId));
    }


    /**
     * 开始采集
     * @param id
     */
    @Transactional
    public boolean open(@NotNull(message = "通道不能为空") Long id, Long closedAtMs) {
        String taskId = CameraCaptureService.generateTaskId();
        Task task = new Task();
        task.setTaskId(taskId);
        task.setSignalId(id);
        task.setStatus(SignalConst.SIGNAL_OPENED);
        task.setClosedAtMs(closedAtMs);
        task.setOpenedAtMs(System.currentTimeMillis());
        boolean save = taskRepository.save(task);
        if (save) {
            LambdaUpdateWrapper<Signal> update = Wrappers.<Signal>lambdaUpdate()
                    .eq(Signal::getId, id)
                    .eq(Signal::getStatus, SignalConst.SIGNAL_CLOSED)
                    .set(Signal::getOpenedAtMs, System.currentTimeMillis())
                    .set(Signal::getClosedAtMs, closedAtMs)
                    .set(Signal::getUpdatedAt, LocalDateTime.now())
                    .set(Signal::getCurrentTaskId, taskId)
                    .set(Signal::getStatus, SignalConst.SIGNAL_OPENED);
            boolean ret = signalRepository.update(update);
            if (ret) {
                signalTaskManager.addTask(id);
            }
            return ret;
        }
        return save;
    }


    @Transactional
    public boolean close(@NotNull(message = "通道不能为空") Long id) {
        Signal signal = signalRepository.getById(id);
        if (signal ==null) {
            return false;
        }
        if (signal.getCurrentTaskId() == null) {
            return false;
        }
        boolean ret = taskRepository.cancel(signal.getCurrentTaskId());
        if (ret) {
            LambdaUpdateWrapper<Signal> update = Wrappers.<Signal>lambdaUpdate()
                    .eq(Signal::getId, id)
                    .eq(Signal::getStatus, SignalConst.SIGNAL_OPENED)
                    .set(Signal::getClosedAtMs, System.currentTimeMillis())
                    .set(Signal::getUpdatedAt, LocalDateTime.now())
                    .set(Signal::getStatus, SignalConst.SIGNAL_CLOSED)
                    .set(Signal::getLatestClosedAtMs, System.currentTimeMillis());
            ret = signalRepository.update(update);
        }
        if (ret) {
            signalTaskManager.removeTask(id);
        }
        return ret;
    }
}
