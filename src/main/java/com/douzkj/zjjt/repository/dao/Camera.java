package com.douzkj.zjjt.repository.dao;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 摄像头模型
 *
 * @author ranger dong
 * @date 19:59 2025/3/23
 * @descrption
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("cameras")
public class Camera extends Base {

    /**
     * 监控点唯一标识
     */
    private String indexCode;
    /**
     * 监控点名称
     */
    private String name;

    private Integer status;

    private Long signalId;

    private String hkMeta;

    private String regionPathName;

    private String regionName;

    private String regionIndexCode;

    private String regionPath;

    private String currentRunId;

    private String currentRtspUrl;

    private LocalDateTime hkCreatedAt;

    private LocalDateTime hkUpdatedAt;

}

