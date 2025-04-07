package com.douzkj.zjjt.repository.dao;

import lombok.Data;

import java.io.Serializable;

/**
 * 设备区域
 *
 * @author ranger dong
 * @date 00:08 2025/4/3
 * @descrption
 * @copyright Spotter.ink
 */
@Data
public class CameraRegion implements Serializable {
    private String regionPathName;

    private String regionName;

    private String regionIndexCode;

    private String regionPath;
}
