package com.douzkj.zjjt;

import com.douzkj.zjjt.infra.hikvision.api.entity.ArtemisPageResponse;
import com.douzkj.zjjt.infra.hikvision.api.video.resource.ArtemisVideoResourceApi;
import com.douzkj.zjjt.infra.hikvision.api.video.resource.entity.CameraModel;
import com.douzkj.zjjt.infra.hikvision.api.video.resource.entity.CameraPageRequest;
import com.douzkj.zjjt.infra.hikvision.config.ArtemisConfigProps;
import com.douzkj.zjjt.service.HikvisionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = VideoRecognizerApplication.class)
public class HikvisionTest {


    @Autowired
    public ArtemisConfigProps configProps;

    @Autowired
    public HikvisionService hikvisionService;

    @Test
    public void getCameras() throws Exception {
        List<CameraModel> cameraList = hikvisionService.getCameraList(1, 200);
        System.out.println(cameraList);
    }


    @Test
    public void getRtspUrl() {
        String cameraRtspUrl = hikvisionService.getCameraRtspUrl("e4e19628c8354c3db8debd4eaf24963a");
        System.out.println(cameraRtspUrl);
    }
}
