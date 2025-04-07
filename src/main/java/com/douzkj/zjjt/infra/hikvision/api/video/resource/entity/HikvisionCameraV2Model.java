package com.douzkj.zjjt.infra.hikvision.api.video.resource.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ranger dong
 * @date 00:30 2025/3/25
 * @descrption
 * @copyright Spotter.ink
 */
@NoArgsConstructor
@Data
public class HikvisionCameraV2Model implements Serializable {

    @JsonProperty("indexCode")
    private String indexCode;
    @JsonProperty("resourceType")
    private String resourceType;
    @JsonProperty("externalIndexCode")
    private String externalIndexCode;
    @JsonProperty("name")
    private String name;
    @JsonProperty("chanNum")
    private Integer chanNum;
    @JsonProperty("cascadeCode")
    private String cascadeCode;
    @JsonProperty("parentIndexCode")
    private String parentIndexCode;
    @JsonProperty("longitude")
    private Integer longitude;
    @JsonProperty("latitude")
    private Integer latitude;
    @JsonProperty("elevation")
    private String elevation;
    @JsonProperty("cameraType")
    private Integer cameraType;
    @JsonProperty("capability")
    private String capability;
    @JsonProperty("recordLocation")
    private String recordLocation;
    @JsonProperty("channelType")
    private String channelType;
    @JsonProperty("regionIndexCode")
    private String regionIndexCode;
    @JsonProperty("regionPath")
    private String regionPath;
    @JsonProperty("transType")
    private Integer transType;
    @JsonProperty("treatyType")
    private String treatyType;
    @JsonProperty("installLocation")
    private String installLocation;
    @JsonProperty("createTime")
    private String createTime;
    @JsonProperty("updateTime")
    private String updateTime;
    @JsonProperty("disOrder")
    private Integer disOrder;
    @JsonProperty("resourceIndexCode")
    private String resourceIndexCode;
    @JsonProperty("decodeTag")
    private String decodeTag;
    @JsonProperty("cameraRelateTalk")
    private String cameraRelateTalk;
    @JsonProperty("regionName")
    private String regionName;
    @JsonProperty("regionPathName")
    private String regionPathName;
}
