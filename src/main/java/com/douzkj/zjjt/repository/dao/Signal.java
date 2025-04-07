package com.douzkj.zjjt.repository.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author ranger dong
 * @date 21:38 2025/3/23
 * @descrption
 * @copyright Spotter.ink
 */
@EqualsAndHashCode(callSuper = true)
@TableName("signals")
@NoArgsConstructor
@Data
public class Signal extends Base {

    /**
     * 通路名称
     */
    private String name;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 通路配置(包含流读取配置、存储配置)
     */
    private String config;
}
