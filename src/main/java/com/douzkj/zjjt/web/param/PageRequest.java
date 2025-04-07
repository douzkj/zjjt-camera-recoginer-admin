package com.douzkj.zjjt.web.param;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author ranger dong
 * @date 00:13 2025/3/24
 * @descrption
 * @copyright Spotter.ink
 */
@Data
public class PageRequest implements Serializable {
    @NotNull
    private Long current = 1L;

    @NotNull
    private Long pageSize = 20L;
}
