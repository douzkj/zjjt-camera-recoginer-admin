package com.douzkj.zjjt.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.douzkj.zjjt.repository.dao.TaskExport;
import com.douzkj.zjjt.repository.mapper.TaskExportMapper;
import org.springframework.stereotype.Repository;

@Repository
public class TaskExportRepository extends ServiceImpl<TaskExportMapper, TaskExport> {
    public boolean finished(String exportId, String filepath) {
        LambdaUpdateWrapper<TaskExport> update = Wrappers.<TaskExport>lambdaUpdate().eq(TaskExport::getExportId, exportId);
        TaskExport taskExport = new TaskExport();
        taskExport.setExportId(exportId);
        taskExport.setFinishedAtMs(System.currentTimeMillis());
        taskExport.setStatus(1);
        taskExport.setFilepath(filepath);
        return this.update(taskExport, update);
    }

    public boolean failed(String exportId) {
        LambdaUpdateWrapper<TaskExport> update = Wrappers.<TaskExport>lambdaUpdate().eq(TaskExport::getExportId, exportId);
        TaskExport taskExport = new TaskExport();
        taskExport.setExportId(exportId);
        taskExport.setFinishedAtMs(System.currentTimeMillis());
        taskExport.setStatus(2);
        return this.update(taskExport, update);
    }


    public TaskExport getByExportId(String exportId) {
        LambdaQueryWrapper<TaskExport> update = Wrappers.<TaskExport>lambdaQuery().eq(TaskExport::getExportId, exportId);
        return this.getOne(update, false);
    }
}
