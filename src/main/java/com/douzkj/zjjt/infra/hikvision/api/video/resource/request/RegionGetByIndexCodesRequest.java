package com.douzkj.zjjt.infra.hikvision.api.video.resource.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author ranger dong
 * @date 23:54 2025/3/24
 * @descrption
 * @copyright Spotter.ink
 */
@Data
public class RegionGetByIndexCodesRequest implements Serializable {
    private List<String> indexCodes;
}
