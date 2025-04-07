package com.douzkj.zjjt.infra.hikvision.api.video.resource;

import com.douzkj.zjjt.infra.hikvision.api.artemis.ArtemisBaseApi;
import com.douzkj.zjjt.infra.hikvision.api.entity.ArtemisPageResponse;
import com.douzkj.zjjt.infra.hikvision.api.video.resource.entity.HikvisionCameraV2Model;
import com.douzkj.zjjt.infra.hikvision.api.video.resource.request.CameraPageRequest;
import com.douzkj.zjjt.infra.hikvision.api.video.resource.entity.HikvisionCameraModel;
import com.douzkj.zjjt.infra.hikvision.api.video.resource.request.CameraPageV2Request;
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
    public ArtemisPageResponse<HikvisionCameraModel> getCameras(CameraPageRequest camerasRequest) throws Exception {
        String postJson = doPostJson("/api/resource/v1/cameras", camerasRequest);
        return toPageData(postJson, HikvisionCameraModel.class);
    }


    public ArtemisPageResponse<HikvisionCameraV2Model> getCamerasV2(CameraPageV2Request camerasRequest) throws Exception {
        String postJson = doPostJson("/api/resource/v2/camera/search", camerasRequest);
        return toPageData(postJson, HikvisionCameraV2Model.class);
    }
}
