package com.douzkj.zjjt.infra.hikvision.api.video.resource.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 海康设备模型
 */
@NoArgsConstructor
@Data
public class HikvisionCameraModel implements Serializable {

    /**
     * 海拔
     */
    private String altitude;
    /**
     * 监控点唯一标识
     */
    private String cameraIndexCode;
    /**
     * 监控点名称
     */
    private String cameraName;
    /**
     * 监控点类型
     */
    private Integer cameraType;
    /**
     * 监控点类型说明
     */
    private String cameraTypeName;
    /**
     * 能力集（详见数据字典，typeCode为xresmgr.capability_set）
     */
    private String capabilitySet;
    /**
     * 能力集说明
     */
    private String capabilitySetName;
    /**
     * 智能分析能力集
     */
    private String intelligentSet;
    /**
     * 智能分析能力集说明
     */
    private String intelligentSetName;
    /**
     * 通道编号
     */
    private String channelNo;
    /**
     * 通道类型
     */
    private String channelType;
    /**
     * 通道子类型说明
     */
    private String channelTypeName;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 所属编码设备唯一标识
     */
    private String encodeDevIndexCode;
    /**
     * 所属设备类型（详见数据字典，typeCode为xresmgr.resource_type）
     */
    private String encodeDevResourceType;
    /**
     * 所属设备类型说明
     */
    private String encodeDevResourceTypeName;
    /**
     * 监控点国标编号
     */
    private String gbIndexCode;
    /**
     * 安装位置
     */
    private String installLocation;
    /**
     *
     * 键盘控制码
     */
    private String keyBoardCode;
    /**
     * 纬度
     */
    private String latitude;
    /**
     * 经度
     */
    private String longitude;
    /**
     * 摄像机像素（1-普通像素，2-130万高清，3-200万高清，4-300万高清，取值参考数据字典，typeCode为xresmgr.piexl）
     */
    private Integer pixel;
    /**
     * 云镜类型（1-全方位云台（带转动和变焦），2-只有变焦,不带转动，3-只有转动，不带变焦，4-无云台，无变焦，取值参考数据字典，typeCode为xresmgr.ptz_type）
     */
    private Integer ptz;
    /**
     * 云台控制(1-DVR，2-模拟矩阵，3-MU4000，4-NC600，取值参考数据字典，typeCode为xresmgr.ptz_control_type)
     */
    private Integer ptzController;
    /**
     * 云台控制说明
     */
    private String ptzControllerName;
    /**
     * 云镜类型说明
     */
    private String ptzName;
    /**
     * 录像存储位置（0-中心存储，1-设备存储，取值参考数据字典，typeCode为xresmgr.record_location）
     */
    private String recordLocation;
    /**
     * 录像存储位置说明
     */
    private String recordLocationName;
    /**
     * 所属区域唯一标识
     */
    private String regionIndexCode;
    /**
     * 在线状态（0-不在线，1-在线，取值参考数据字典，typeCode为xresmgr.status）
     */
    private Integer status;
    /**
     * 状态说明
     */
    private String statusName;
    /**
     * 传输协议（0-UDP，1-TCP）
     */
    private Integer transType;
    /**
     * 传输协议类型说明
     */
    private String transTypeName;
    /**
     * 接入协议（详见数据字典，typeCode为xresmgr.protocol_type）
     */
    private String treatyType;
    /**
     * 接入协议类型说明
     */
    private String treatyTypeName;
    /**
     * 可视域相关（JSON格式）
     */
    private String viewshed;
    /**
     * 更新时间 采用ISO8601标准，如2018-07-26T21:30:08+08:00 表示北京时间2017年7月26日21时30分08秒
     */
    private String updateTime;
}

