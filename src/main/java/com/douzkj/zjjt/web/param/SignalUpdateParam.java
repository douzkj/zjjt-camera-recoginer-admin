package com.douzkj.zjjt.web.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author ranger dong
 * @date 22:13 2025/3/23
 * @descrption
 * @copyright Spotter.ink
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SignalUpdateParam extends SignalCreateParam {

    @NotNull()
    private Long id;

}
