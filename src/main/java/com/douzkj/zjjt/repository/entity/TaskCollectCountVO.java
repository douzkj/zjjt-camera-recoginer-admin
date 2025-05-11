package com.douzkj.zjjt.repository.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class TaskCollectCountVO implements Serializable {

    private Long signalId;

    private String taskId;

    private Long frameImageCnt;

    private Long labelImageCnt;

    private Long labelJsonCnt;
}
