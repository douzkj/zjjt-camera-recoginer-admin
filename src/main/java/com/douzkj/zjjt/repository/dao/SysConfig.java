package com.douzkj.zjjt.repository.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author ranger dong
 * @date 22:57 2025/3/23
 * @descrption
 * @copyright Spotter.ink
 */
@EqualsAndHashCode(callSuper = true)
@TableName("sys_config")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysConfig extends Base {

    private String cfKey;

    private String cfValue;

}
