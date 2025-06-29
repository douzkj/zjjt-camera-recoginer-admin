package com.douzkj.zjjt.web.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TargetStatisticVO implements Serializable {

    private List<TargetStatisticItemVO> records;

    private Long numImages;

    private Long numInstances;

}
