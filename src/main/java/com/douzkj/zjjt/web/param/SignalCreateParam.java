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
public class SignalCreateParam implements Serializable {

    @NotNull(message = "通道名不能为空")
    private String name;

    private Integer sort;

}
