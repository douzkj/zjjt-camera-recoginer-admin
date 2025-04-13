package com.douzkj.zjjt.web.param;

import com.douzkj.zjjt.repository.entity.SignalConfig;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.Valid;
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


    private String description;

    private Integer sort;

    private String type;

    @NotNull
    @Valid
    private SignalConfig config;

}
