package com.douzkj.zjjt.web.param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.douzkj.zjjt.repository.dao.Camera;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * @author ranger dong
 * @date 20:56 2025/3/23
 * @descrption
 * @copyright Spotter.ink
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CameraPageRequest extends PageRequest {

    /**
     * 通路ID
     */
    private Long signalId;

    /**
     * 设备唯一编码
     */
    private String indexCode;

    /**
     * 设备名称
     */
    private String name;

    private String regionIndexCode;


    public Wrapper<Camera> toWrapper() {
        LambdaQueryWrapper<Camera> wrapper = Wrappers.<Camera>lambdaQuery();
        if (this.getSignalId() != null) {
            wrapper.eq(Camera::getSignalId, this.getSignalId());
        }
        if (StringUtils.isNotBlank(this.getIndexCode())) {
            wrapper.eq(Camera::getIndexCode, this.getIndexCode());
        }
        if (StringUtils.isNotBlank(this.getName())) {
            wrapper.like(Camera::getName, this.getName());
        }
        if (StringUtils.isNotBlank(this.getRegionIndexCode())) {
            wrapper.eq(Camera::getRegionIndexCode, this.getRegionIndexCode());
        }
        return wrapper;
    }
}
