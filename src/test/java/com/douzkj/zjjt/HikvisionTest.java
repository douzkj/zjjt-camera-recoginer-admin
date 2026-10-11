package com.douzkj.zjjt;

import com.douzkj.zjjt.infra.hikvision.api.entity.ArtemisPageResponse;
import com.douzkj.zjjt.infra.hikvision.api.video.funcional.entity.CapturePic;
import com.douzkj.zjjt.infra.hikvision.api.video.funcional.entity.CapturePicSearchRequest;
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

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
        String cameraRtspUrl = hikvisionService.getCameraRtspUrl("39a647d3b4f34db5b7290b44d8aaa7c7");
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

    @Test
    public void captrue() {
        String capture = hikvisionService.capture("5bc7eace54644d6bacf836d9818f4118");
        System.out.println("手动抓图的图片结果:" + capture);
    }


    @Test
    public void captureSearch() {

        CapturePicSearchRequest searchRequest = new CapturePicSearchRequest();
        searchRequest.setPageSize(100);
        searchRequest.setPageNo(1);
        searchRequest.setCameraIndexCodes(Lists.newArrayList("f5649d247dfc417a805a8c2d21eb7e60"));
        ZonedDateTime now = ZonedDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        ZonedDateTime endTime = now.plusDays(1);
        searchRequest.setStartTime(now.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        searchRequest.setEndTime(endTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

        ArtemisPageResponse<CapturePic> capture = hikvisionService.capture(searchRequest);
        System.out.println(capture);

    }
}
