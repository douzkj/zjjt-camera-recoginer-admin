package com.douzkj.zjjt.infra.hikvision.api.video.funcional.entity;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PreviewURLsRequest {
	/**
	 * 监控点唯一标识，[分页获取监控点资源]@[软件产品-综合安防管理平台-API列表-视频应用服务-视频资源#分页获取监控点资源]接口获取返回参数cameraIndexCode
	 */

	@NotNull
	private String cameraIndexCode;
	/**
	 * 码流类型，0:主码流 1:子码流 2:第三码流 参数不填，默认为主码流
	 */
	private Integer streamType;
	/**
	 * 取流协议（应用层协议），“hik”:HIK私有协议，使用视频SDK进行播放时，传入此类型；“rtsp”:RTSP协议；“rtmp”:RTMP协议；“hls”:HLS协议（HLS协议只支持海康SDK协议、EHOME协议、ONVIF协议接入的设备；只支持H264视频编码和AAC音频编码）。参数不填，默认为HIK协议
	 */
	private String protocol;
	/**
	 * 传输协议（传输层协议），0:UDP 1:TCP 默认是TCP 注： GB28181 2011及以前版本只支持UDP传输
	 */
	private Integer transmode;
	/**
	 * 标识扩展内容，格式：key=value， 调用方根据其播放控件支持的解码格式选择相应的封装类型； 支持的内容详见[附录F expand扩展内容说明]@[软件产品-综合安防管理平台-附录-附录F expand扩展内容说明]
	 */
	private String expand;
	/**
	 * 输出码流转封装格式，“ps”:PS封装格式、“rtp”:RTP封装协议。当protocol=rtsp时生效，且不传值时默认为RTP封装协议。
	 */
	private String streamform;
}