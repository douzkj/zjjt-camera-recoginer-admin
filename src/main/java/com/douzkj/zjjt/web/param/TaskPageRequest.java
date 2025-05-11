package com.douzkj.zjjt.web.param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.douzkj.zjjt.repository.dao.Task;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class TaskPageRequest extends PageRequest implements Serializable {

    private Long signalId;

    private String taskId;

    private Integer status;

    private Long openedAtStartMs;

    private Long openedAtEndMs;


    public Wrapper<Task> toWrapper() {
        LambdaQueryWrapper<Task> wrapper = Wrappers.<Task>lambdaQuery();
        if (this.getSignalId() != null) {
            wrapper.eq(Task::getSignalId, this.getSignalId());
        }
        if (StringUtils.isNotBlank(this.getTaskId())) {
            wrapper.eq(Task::getTaskId, this.getTaskId());
        }
        if (this.getOpenedAtStartMs() != null) {
            wrapper.eq(Task::getOpenedAtMs, this.getOpenedAtStartMs());
        }
        if (this.getOpenedAtEndMs() != null) {
            wrapper.eq(Task::getOpenedAtMs, this.getOpenedAtEndMs());
        }
        if (this.getStatus() != null) {
            wrapper.eq(Task::getStatus, this.getStatus());
        }
        return wrapper;
    }
}
