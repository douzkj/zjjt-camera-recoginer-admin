package com.douzkj.zjjt.infra.hikvision.api.video.resource.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ranger dong
 * @date 23:45 2025/3/24
 * @descrption
 * @copyright Spotter.ink
 */
@NoArgsConstructor
@Data
public class HikvisionRegionModel implements Serializable {
    @JsonProperty("indexCode")
    private String indexCode;
    @JsonProperty("name")
    private String name;
    @JsonProperty("regionPath")
    private String regionPath;
    @JsonProperty("parentIndexCode")
    private String parentIndexCode;
    @JsonProperty("available")
    private Boolean available;
    @JsonProperty("leaf")
    private Boolean leaf;
    @JsonProperty("cascadeCode")
    private String cascadeCode;
    @JsonProperty("cascadeType")
    private Integer cascadeType;
    @JsonProperty("catalogType")
    private Integer catalogType;
    @JsonProperty("externalIndexCode")
    private String externalIndexCode;
    @JsonProperty("parentExternalIndexCode")
    private String parentExternalIndexCode;
    @JsonProperty("sort")
    private Integer sort;
    @JsonProperty("createTime")
    private String createTime;
    @JsonProperty("updateTime")
    private String updateTime;
}
