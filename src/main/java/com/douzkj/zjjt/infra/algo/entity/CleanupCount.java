package com.douzkj.zjjt.infra.algo.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class CleanupCount implements Serializable {
    private Integer deletedRecordsCount;
    private Integer similarImagesCount;
}
