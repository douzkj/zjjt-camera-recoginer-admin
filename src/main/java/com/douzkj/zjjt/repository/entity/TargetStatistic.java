package com.douzkj.zjjt.repository.entity;

import com.google.common.collect.Maps;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class TargetStatistic implements Serializable {

    private Map<String, Integer> instances = Maps.newHashMap();

    private Map<String, Integer> images = Maps.newHashMap();


    private Long numImages;

    private Long numInstances;
}
