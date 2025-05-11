package com.douzkj.zjjt.web.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class DownloadVO implements Serializable {

    private String filepath;

    private String downloadId;
}
