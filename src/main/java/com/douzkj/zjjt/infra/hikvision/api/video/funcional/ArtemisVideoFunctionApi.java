package com.douzkj.zjjt.infra.hikvision.api.video.funcional;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.douzkj.zjjt.infra.hikvision.api.artemis.ArtemisBaseApi;
import com.douzkj.zjjt.infra.hikvision.api.entity.ArtemisResponse;
import com.douzkj.zjjt.infra.hikvision.api.video.funcional.entity.CameraPreviewUrl;
import com.douzkj.zjjt.infra.hikvision.api.video.funcional.entity.PreviewURLsRequest;
import com.douzkj.zjjt.infra.hikvision.config.ArtemisConfigProps;

/**
 * 视频业务 -> 视频功能API
 */
public class ArtemisVideoFunctionApi extends ArtemisBaseApi {

    public ArtemisVideoFunctionApi(ArtemisConfigProps artemisConfigProps) {
        super(artemisConfigProps);
    }

    /**
     * 获取监控点预览取流URLv2
     * @param previewURLsRequest
     * @return
     * @throws Exception
     */
    public ArtemisResponse<CameraPreviewUrl> getPreviewURLs(PreviewURLsRequest previewURLsRequest) throws Exception {
        String string = doPostJson("/api/video/v2/cameras/previewURLs", previewURLsRequest);
        return JSONUtil.toBean(string, new TypeReference<ArtemisResponse<CameraPreviewUrl>>() {
        }, true);

    }

}
