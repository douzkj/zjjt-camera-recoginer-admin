package com.douzkj.zjjt.web.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class TargetStatisticVO implements Serializable {

    private String target;

    private String targetName;

    private String classification;

    private String statisticType;

    private Integer value;
}
