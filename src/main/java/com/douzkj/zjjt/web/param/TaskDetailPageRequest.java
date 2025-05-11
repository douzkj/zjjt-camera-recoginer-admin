package com.douzkj.zjjt.web.param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.douzkj.zjjt.repository.dao.TaskDetail;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class TaskDetailPageRequest extends PageRequest implements Serializable {

    private String cameraIndexCode;

    private String cameraName;

    private List<String> regionIndexCode;

    private String taskId;

    private List<Long> signalId;

    private Long frameStartMs;

    private Long frameEndMs;

    private List<String> labels;


    public Wrapper<TaskDetail> toWrapper() {
        LambdaQueryWrapper<TaskDetail> wrapper = Wrappers.<TaskDetail>lambdaQuery();
        if (StringUtils.isNotBlank(getCameraIndexCode())) {
            wrapper.eq(TaskDetail::getCameraIndexCode, getCameraIndexCode());
        }
        if (StringUtils.isNotBlank(getCameraName())) {
            wrapper.like(TaskDetail::getCameraName,  getCameraName());
        }
        if ( ! CollectionUtils.isEmpty(getRegionIndexCode())) {
            wrapper.in(TaskDetail::getRegionIndexCode, getRegionIndexCode());
        }
        if ( ! CollectionUtils.isEmpty(getSignalId())) {
            wrapper.in(TaskDetail::getSignalId, this.getSignalId());
        }
        if (StringUtils.isNotBlank(this.getTaskId())) {
            wrapper.eq(TaskDetail::getTaskId, this.getTaskId());
        }
        if (this.getFrameStartMs() != null) {
            wrapper.ge(TaskDetail::getFrameTimeMs, this.getFrameStartMs());
        }
        if (this.getFrameEndMs() != null) {
            wrapper.le(TaskDetail::getFrameTimeMs, this.getFrameEndMs());
        }
        if ( ! CollectionUtils.isEmpty(this.getLabels())) {
            boolean isFirst = false;
            for (String labelType : this.getLabels()) {
                if (! isFirst) {
                    isFirst = true;
                    wrapper.apply("find_in_set({0}, label_types) > 0", labelType);
                } else {
                    wrapper.or().apply("find_in_set({0}, label_types) > 0", labelType);
                }
            }
        }
        return wrapper;
    }
}
