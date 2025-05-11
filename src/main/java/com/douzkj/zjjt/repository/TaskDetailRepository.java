package com.douzkj.zjjt.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.douzkj.zjjt.repository.dao.TaskDetail;
import com.douzkj.zjjt.repository.entity.TaskCollectCountVO;
import com.douzkj.zjjt.repository.mapper.TaskDetailMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TaskDetailRepository extends ServiceImpl<TaskDetailMapper, TaskDetail> {

    public List<TaskCollectCountVO> countImageBySignalIds(List<Long> signalIds) {
        LambdaQueryWrapper<TaskDetail> wrapper = Wrappers.<TaskDetail>lambdaQuery().in(TaskDetail::getSignalId, signalIds);
        return getBaseMapper().countBySignalId(wrapper);
    }

    public List<TaskCollectCountVO> countImageByTaskIds(List<String> taskIds) {
        LambdaQueryWrapper<TaskDetail> wrapper = Wrappers.<TaskDetail>lambdaQuery().in(TaskDetail::getTaskId, taskIds);
        return getBaseMapper().countByTaskId(wrapper);
    }
}
