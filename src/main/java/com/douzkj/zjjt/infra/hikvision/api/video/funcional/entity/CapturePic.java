package com.douzkj.zjjt.infra.hikvision.api.video.funcional.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Data
public class CapturePic implements Serializable {

    @JsonProperty("captureId")
    private String captureId;
    @JsonProperty("cameraIndexCode")
    private String cameraIndexCode;
    @JsonProperty("cameraName")
    private String cameraName;
    @JsonProperty("cameraType")
    private String cameraType;
    @JsonProperty("captureUrl")
    private String captureUrl;
    @JsonProperty("thumbnailUrl")
    private String thumbnailUrl;
    @JsonProperty("captureTime")
    private String captureTime;
}
