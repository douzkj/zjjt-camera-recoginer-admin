package com.douzkj.zjjt.infra.hikvision.api.video.resource.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CameraPageV2Request {
	@NotNull()
	private Integer pageNo;
	@NotNull()
	@Max(1000)
	private Integer pageSize;

}
