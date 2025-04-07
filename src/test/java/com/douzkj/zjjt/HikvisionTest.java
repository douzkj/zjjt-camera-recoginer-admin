package com.douzkj.zjjt;

import com.douzkj.zjjt.infra.hikvision.api.video.resource.entity.HikvisionCameraModel;
import com.douzkj.zjjt.infra.hikvision.api.video.resource.entity.HikvisionRegionModel;
import com.douzkj.zjjt.infra.hikvision.config.ArtemisConfigProps;
import com.douzkj.zjjt.scheduler.HikCameraSyncScheduler;
import com.douzkj.zjjt.service.HikvisionService;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = VideoRecognizerApplication.class)
public class HikvisionTest {


    @Autowired
    public ArtemisConfigProps configProps;

    @Autowired
    public HikvisionService hikvisionService;

    @Autowired
    public HikCameraSyncScheduler hikCameraSyncScheduler;

    @Test
    public void getCameras() throws Exception {
        List<HikvisionCameraModel> cameraList = hikvisionService.getCameraList(1, 200);
        System.out.println(cameraList);
    }

    @Test
    public void getCamerasV2() throws Exception {
        List<HikvisionCameraModel> cameraList = hikvisionService.getCameraList(1, 200);
        System.out.println(cameraList);
    }


    @Test
    public void getRtspUrl() {
        String cameraRtspUrl = hikvisionService.getCameraRtspUrl("0830b36563804ce390db5fb02810102a");
        System.out.println(cameraRtspUrl);
    }

    @Test
    public void getRegions() {
        Map<String, HikvisionRegionModel> regions = hikvisionService.getRegions(Lists.newArrayList("2a400d08-fbe0-403e-a7c9-1b758ca7c91e", "ca80fab0-a934-415e-b02f-634b97be3f7b"));
        System.out.println(regions);
    }

    @Test
    public void syncCamera() {
        hikCameraSyncScheduler.syncCamera();
    }
}
