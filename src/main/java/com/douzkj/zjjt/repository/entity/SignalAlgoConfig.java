package com.douzkj.zjjt.repository.entity;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class SignalAlgoConfig implements Serializable {

    @NotNull
    private AlgoConfigs.AlgoLabelConfig label;
}
