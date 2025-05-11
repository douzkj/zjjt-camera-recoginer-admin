package com.douzkj.zjjt.repository;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.douzkj.zjjt.common.TaskConst;
import com.douzkj.zjjt.repository.dao.Task;
import com.douzkj.zjjt.repository.mapper.TaskMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class TaskRepository extends ServiceImpl<TaskMapper, Task> {

    public boolean cancel(String taskId) {
        LambdaUpdateWrapper<Task> update = Wrappers.<Task>lambdaUpdate()
        .eq(Task::getTaskId, taskId)
        .eq(Task::getStatus, TaskConst.TASK_OPENED)
        .set(Task::getClosedAtMs, System.currentTimeMillis())
        .set(Task::getUpdatedAt, LocalDateTime.now())
        .set(Task::getStatus, TaskConst.TASK_CLOSED);
        return this.update(update);
    }
}
