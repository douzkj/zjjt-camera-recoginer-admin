package com.douzkj.zjjt.repository.entity;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author ranger dong
 * @date 23:21 2025/4/6
 * @descrption
 * @copyright Spotter.ink
 */
@Data
public class SignalConfig implements Serializable {

    @NotNull
    @Valid
    private SignalFrameConfig frame;

    @NotNull
    @Valid
    private SignalAlgoConfig algo;

}
