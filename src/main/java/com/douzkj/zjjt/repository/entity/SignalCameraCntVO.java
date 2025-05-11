package com.douzkj.zjjt.repository.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class SignalCameraCntVO implements Serializable {

    private Long signalId;

    private Integer cameraCnt;
}
