package com.douzkj.zjjt.web.param;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class CameraViewParam implements Serializable {

    @NotNull(message = "code 不能为空")
    private String indexCode;
}
