package com.douzkj.zjjt.scheduler;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.douzkj.zjjt.infra.hikvision.api.video.resource.entity.HikvisionCameraV2Model;
import com.douzkj.zjjt.repository.CameraRepository;
import com.douzkj.zjjt.repository.dao.Camera;
import com.douzkj.zjjt.service.HikvisionService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author ranger dong
 * @date 23:25 2025/3/23
 * @descrption
 * @copyright Spotter.ink
 */
@Component
@Data
@Slf4j
public class HikCameraSyncScheduler {

    private  AtomicBoolean running = new AtomicBoolean(false);

    private final HikvisionService hikvisionService;
    private final CameraRepository cameraRepository;

    @Async("asyncExecutor")
    @Scheduled(fixedRate = 60 * 60, timeUnit = java.util.concurrent.TimeUnit.SECONDS, initialDelay = 30 * 60)
    public void syncCamera() {
        if (running.compareAndSet(false, true)) {
            int pageNo = 1;
            int pageSize = 200;
            int curPageSize = 200;
            while (pageSize == curPageSize) {
                List<HikvisionCameraV2Model> cameraList = hikvisionService.getCameraListV2(pageNo, pageSize);
                log.info("抓取海康第{}页数据... res: {}", pageNo, cameraList.size());
                for (HikvisionCameraV2Model hikvisionCameraModel : cameraList) {
                    Camera camera = toCamera(hikvisionCameraModel);
                    boolean b = cameraRepository.saveOrUpdate(
                            camera, Wrappers.<Camera>lambdaQuery()
                                    .eq(Camera::getIndexCode, hikvisionCameraModel.getIndexCode())
                    );
                    log.info("保存海康摄像头信息: camera={} ==> res:{}", hikvisionCameraModel, b);
                }
                curPageSize = cameraList.size();
                pageNo += 1;
            }
        }
        running.set(false);
    }

    public Camera toCamera(HikvisionCameraV2Model hikvisionCameraModel) {
        Camera camera = new Camera();
        camera.setIndexCode(hikvisionCameraModel.getIndexCode());
        camera.setName(hikvisionCameraModel.getName());
        camera.setHkMeta(JSONUtil.toJsonStr(hikvisionCameraModel));
        camera.setRegionIndexCode(hikvisionCameraModel.getRegionIndexCode());
        camera.setRegionName(hikvisionCameraModel.getRegionName());
        camera.setRegionPath(hikvisionCameraModel.getRegionPath());
        camera.setRegionPathName(hikvisionCameraModel.getRegionPathName());
        return camera;
    }


}
