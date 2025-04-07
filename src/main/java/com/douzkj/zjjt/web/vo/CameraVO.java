package com.douzkj.zjjt.web.vo;

import com.douzkj.zjjt.infra.hikvision.api.video.resource.entity.HikvisionCameraV2Model;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author ranger dong
 * @date 20:55 2025/3/23
 * @descrption
 * @copyright Spotter.ink
 */
@Data
public class CameraVO implements Serializable {

    private Long id;

    private String indexCode;

    private String name;

    private Integer status;

    private HikvisionCameraV2Model hkMeta;

    private String regionPathName;

    private String regionName;

    private String regionIndexCode;

    private String regionPath;


    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Long signalId;

    private String signalName;

}
