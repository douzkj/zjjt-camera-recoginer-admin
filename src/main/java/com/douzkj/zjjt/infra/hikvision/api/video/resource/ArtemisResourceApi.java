package com.douzkj.zjjt.infra.hikvision.api.video.resource;

import com.douzkj.zjjt.infra.hikvision.api.artemis.ArtemisBaseApi;
import com.douzkj.zjjt.infra.hikvision.api.entity.ArtemisPageResponse;
import com.douzkj.zjjt.infra.hikvision.api.video.resource.entity.HikvisionRegionModel;
import com.douzkj.zjjt.infra.hikvision.api.video.resource.request.RegionGetByIndexCodesRequest;
import com.douzkj.zjjt.infra.hikvision.config.ArtemisConfigProps;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author ranger dong
 * @date 23:49 2025/3/24
 * @descrption
 * @copyright Spotter.ink
 */
public class ArtemisResourceApi extends ArtemisBaseApi {


    public ArtemisResourceApi(ArtemisConfigProps artemisConfigProps) {
        super(artemisConfigProps);
    }


    public ArtemisPageResponse<HikvisionRegionModel> getRegionsByRegionIndexCodes(List<String> regionIndexCodes) throws Exception {
        if (CollectionUtils.isEmpty(regionIndexCodes)) {
            return ArtemisPageResponse.empty();
        }
        RegionGetByIndexCodesRequest getByIndexCodesRequest = new RegionGetByIndexCodesRequest();
        getByIndexCodesRequest.setIndexCodes(regionIndexCodes);
        String postJson = doPostJson("/api/resource/v1/region/regionCatalog/regionInfo", getByIndexCodesRequest);
        return toPageData(postJson, HikvisionRegionModel.class);
    }
}
