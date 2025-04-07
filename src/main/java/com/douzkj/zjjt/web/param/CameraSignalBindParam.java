package com.douzkj.zjjt.web.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author ranger dong
 * @date 22:26 2025/3/23
 * @descrption
 * @copyright Spotter.ink
 */
@Data
public class CameraSignalBindParam implements Serializable {

    @NotEmpty()
    @NotNull()
    private List<Long> cameraIds;

    private Long signalId;
}
