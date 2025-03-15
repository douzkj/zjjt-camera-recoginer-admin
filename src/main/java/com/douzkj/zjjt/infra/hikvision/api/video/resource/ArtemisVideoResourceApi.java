package com.douzkj.zjjt.infra.hikvision.api.video.resource;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.douzkj.zjjt.infra.hikvision.api.video.resource.entity.CameraModel;
import com.douzkj.zjjt.infra.hikvision.api.video.resource.entity.CameraPageRequest;
import com.douzkj.zjjt.infra.hikvision.api.artemis.ArtemisBaseApi;
import com.douzkj.zjjt.infra.hikvision.api.entity.ArtemisPageResponse;
import com.douzkj.zjjt.infra.hikvision.config.ArtemisConfigProps;

/**
 * 视频业务 -> 视频资源API
 */
public class ArtemisVideoResourceApi extends ArtemisBaseApi {


    public ArtemisVideoResourceApi(ArtemisConfigProps artemisConfigProps) {
        super(artemisConfigProps);
    }


   /**
    * 分页获取监控点资源
    */
    public ArtemisPageResponse<CameraModel> getCameras(CameraPageRequest camerasRequest) throws Exception {
        String postJson = doPostJson("/api/resource/v1/cameras", camerasRequest);
        return JSONUtil.toBean(postJson, new TypeReference<ArtemisPageResponse<CameraModel>>() {
        }, true);
    }
}
