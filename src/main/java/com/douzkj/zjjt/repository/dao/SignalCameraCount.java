package com.douzkj.zjjt.repository.dao;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author ranger dong
 * @date 21:42 2025/3/23
 * @descrption
 * @copyright Spotter.ink
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SignalCameraCount extends Signal {

    private Integer cameraCnt;
}
