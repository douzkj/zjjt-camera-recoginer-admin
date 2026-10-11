package com.douzkj.zjjt.infra.hikvision.api.video.funcional.entity;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CapturePicSearchRequest implements Serializable {

    private Integer pageNo = 1;

    private Integer pageSize = 100;

    private List<String> cameraIndexCodes;

    private String captureId;

    private String startTime;

    private String endTime;

    private String netProtocol;

    private Integer sort;

    private Integer compressRatio;
}
