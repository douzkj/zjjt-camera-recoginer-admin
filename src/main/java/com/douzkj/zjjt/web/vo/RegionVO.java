package com.douzkj.zjjt.web.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ranger dong
 * @date 00:22 2025/4/2
 * @descrption
 * @copyright Spotter.ink
 */
@Data
public class RegionVO implements Serializable {
    private String indexCode;
    private String name;
    private String pathName;
    private String path;
}
