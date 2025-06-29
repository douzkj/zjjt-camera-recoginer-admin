package com.douzkj.zjjt.repository.dao;

import lombok.Data;

import java.io.Serializable;

@Data
public class Target implements Serializable {
    private String target;

    private String classification;

    private String targetName;

    private String classificationParentName;
}
