package com.douzkj.zjjt.web.param;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.douzkj.zjjt.repository.dao.Signal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

@EqualsAndHashCode(callSuper = true)
@Data
public class SignalPageRequest extends PageRequest {


    private String name;

    private Integer status;

    private String type;


    public LambdaQueryWrapper<Signal> toWrapper() {
        LambdaQueryWrapper<Signal> wrapper = Wrappers.<Signal>lambdaQuery();

        if (StringUtils.isNotBlank(this.getName())) {
            wrapper.like(Signal::getName, this.getName());
        }
        if (StringUtils.isNotBlank(this.getType())) {
            wrapper.eq(Signal::getType, this.getType());
        }
        if (this.getStatus() != null) {
            wrapper.eq(Signal::getStatus, this.getStatus());
        }
        return wrapper;
    }
}
