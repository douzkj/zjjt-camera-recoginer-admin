package com.douzkj.zjjt.service;

import com.douzkj.zjjt.infra.hikvision.api.entity.ArtemisPageResponse;
import com.douzkj.zjjt.infra.hikvision.api.entity.ArtemisResponse;
import com.douzkj.zjjt.infra.hikvision.api.video.funcional.ArtemisVideoFunctionApi;
import com.douzkj.zjjt.infra.hikvision.api.video.funcional.constant.VideoFunctionalConstant;
import com.douzkj.zjjt.infra.hikvision.api.video.funcional.entity.CameraPreviewUrl;
import com.douzkj.zjjt.infra.hikvision.api.video.funcional.entity.PreviewURLsRequest;
import com.douzkj.zjjt.infra.hikvision.api.video.resource.ArtemisResourceApi;
import com.douzkj.zjjt.infra.hikvision.api.video.resource.ArtemisVideoResourceApi;
import com.douzkj.zjjt.infra.hikvision.api.video.resource.entity.HikvisionCameraModel;
import com.douzkj.zjjt.infra.hikvision.api.video.resource.entity.HikvisionCameraV2Model;
import com.douzkj.zjjt.infra.hikvision.api.video.resource.entity.HikvisionRegionModel;
import com.douzkj.zjjt.infra.hikvision.api.video.resource.request.CameraPageRequest;
import com.douzkj.zjjt.infra.hikvision.api.video.resource.request.CameraPageV2Request;
import com.douzkj.zjjt.infra.hikvision.config.HikvisionSyncConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 海康威视摄像Service
 */
@Slf4j
@Data
@Service
public class HikvisionService {

    private final ArtemisVideoFunctionApi artemisVideoFunctionApi;

    private final ArtemisVideoResourceApi artemisVideoResourceApi;

    private final ArtemisResourceApi artemisResourceApi;

    private final HikvisionSyncConfig config;

    /**
     * 获取设备的rtsp 地址
     *
     * @param cameraIndexCode 设备唯一标识
     * @return rtsp地址，如果获取失败返回null
     */
    public String getCameraRtspUrl(String cameraIndexCode) {
        PreviewURLsRequest request = new PreviewURLsRequest();
        request.setCameraIndexCode(cameraIndexCode);
        request.setProtocol(VideoFunctionalConstant.PROTOCOL_RTSP);
        try {
            ArtemisResponse<CameraPreviewUrl> previewURLs = artemisVideoFunctionApi.getPreviewURLs(request);
            if (previewURLs.isSuccess()) {
                return previewURLs.getData().getUrl();
            }
        } catch (Exception e) {
            log.error("读取海康威视rtsp地址失败. cameraIndexCode={}", cameraIndexCode, e);
        }
        return null;
    }


    public List<HikvisionCameraModel> getCameraList(int page, int pageSize) {
        CameraPageRequest cameraPageRequest = new CameraPageRequest();
        cameraPageRequest.setPageSize(pageSize);
        cameraPageRequest.setPageNo(page);
        try {
            ArtemisPageResponse<HikvisionCameraModel> cameras = artemisVideoResourceApi.getCameras(cameraPageRequest);
            if (cameras.isSuccess()) {
                return cameras.getData().getList();
            }
        } catch (Exception e) {
            log.error("读取列表失败", e);
        }
        return Collections.emptyList();
    }

    public List<HikvisionCameraV2Model> getCameraListV2(int page, int pageSize) {
        CameraPageV2Request cameraPageRequest = new CameraPageV2Request();
        cameraPageRequest.setPageSize(pageSize);
        cameraPageRequest.setPageNo(page);
        try {
            ArtemisPageResponse<HikvisionCameraV2Model> cameras = artemisVideoResourceApi.getCamerasV2(cameraPageRequest);
            if (cameras.isSuccess()) {
                return cameras.getData().getList();
            }
        } catch (Exception e) {
            log.error("读取列表失败", e);
        }
        return Collections.emptyList();
    }


    public Map<String, HikvisionRegionModel> getRegions(List<String> regionIndexCodes) {
        try {
            ArtemisPageResponse<HikvisionRegionModel> regionsByRegionIndexCodes = artemisResourceApi.getRegionsByRegionIndexCodes(regionIndexCodes);
            if (regionsByRegionIndexCodes.isSuccess()) {
                return regionsByRegionIndexCodes.getData().getList().stream()
                        .collect(Collectors.toMap(HikvisionRegionModel::getIndexCode, a -> a));
            }
        } catch (Exception e) {
            log.error("读取区域失败。 codes={}",  regionIndexCodes, e);
        }
        return Collections.emptyMap();

    }


    public Long getCameraSyncIntervalSeconds() {
        return config.getCameraIntervalSeconds();
    }
}


