package com.douzkj.zjjt;

import com.douzkj.zjjt.infra.hikvision.api.entity.ArtemisResponse;
import com.douzkj.zjjt.infra.hikvision.api.video.funcional.ArtemisVideoFunctionApi;
import com.douzkj.zjjt.infra.hikvision.api.video.funcional.entity.CameraPreviewUrl;
import com.douzkj.zjjt.infra.hikvision.api.video.funcional.entity.PreviewURLsRequest;
import com.douzkj.zjjt.infra.hikvision.config.ArtemisConfigProps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class ArtemisVideoFunctionApiTest {

    @Mock
    private ArtemisConfigProps artemisConfigProps;


    @InjectMocks
    private ArtemisVideoFunctionApi api;


    @Test
    public void testGetPreviewURLs() throws Exception {
        api = new ArtemisVideoFunctionApi(artemisConfigProps);
        // 使用Mockito跳过doPostJson方法实际执行
        ArtemisVideoFunctionApi spyApi = spy(api);
        doReturn("{\"code\":\"0\",\"msg\":\"success\",\"data\":{\"url\":\"rtsp://10.2.145.66:655/EUrl/CLJ52BW\"}}")
                .when(spyApi).doPostJson(anyString(), any());

        PreviewURLsRequest request = new PreviewURLsRequest();
        ArtemisResponse<CameraPreviewUrl> response = spyApi.getPreviewURLs(request);
        System.out.println(response);

    }
}
