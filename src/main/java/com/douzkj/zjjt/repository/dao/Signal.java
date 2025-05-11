package com.douzkj.zjjt.repository.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import com.douzkj.zjjt.common.SignalConst;
import com.google.common.base.Objects;
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


    /**
     * 通路类型
     * SPECIAL：专项
     * GENERAL：通用
     *
     */
    private String type;

    /**
     * 采集状态
     */
    private Integer status;

    /**
     * 通路描述
     */
    private String description;

    /**
     * 最近一次通路关闭时间
     */
    private Long latestClosedAtMs;

    /**
     * 采集开启时间
     */
    private Long openedAtMs;
    /**
     * 采集关闭时间
     */
    private Long closedAtMs;

    /**
     * 当前任务ID
     */
    private String currentTaskId;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Signal)) return false;
        if (!super.equals(o)) return false;
        Signal signal = (Signal) o;
        return Objects.equal(getId(), signal.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), getId());
    }

    public boolean isOpened() {
        return SignalConst.SIGNAL_OPENED.equals(status)
                && (
                        closedAtMs != null
                    && (
                            closedAtMs == 0
                            || closedAtMs > System.currentTimeMillis()
                    )
                );
    }

    public boolean openedButNotClosed() {
        return SignalConst.SIGNAL_OPENED.equals(status)
                && closedAtMs != null
                && closedAtMs != 0 && closedAtMs < System.currentTimeMillis();
    }
}
