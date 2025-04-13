package com.douzkj.zjjt.web.param;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author ranger dong
 * @date 22:13 2025/3/23
 * @descrption
 * @copyright Spotter.ink
 */
@Data
public class SignalOpenParam implements Serializable {

    @NotNull(message = "通道不能为空")
    private Long id;


    @NotNull(message = "停止方式不能为空")
    private String stopType;

    private Integer period;

}
