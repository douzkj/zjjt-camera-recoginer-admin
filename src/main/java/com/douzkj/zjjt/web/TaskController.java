package com.douzkj.zjjt.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.douzkj.zjjt.entity.R;
import com.douzkj.zjjt.infra.algo.AlgoServer;
import com.douzkj.zjjt.infra.algo.Response;
import com.douzkj.zjjt.infra.algo.entity.CleanupCount;
import com.douzkj.zjjt.repository.TaskDetailRepository;
import com.douzkj.zjjt.repository.TaskExportRepository;
import com.douzkj.zjjt.repository.dao.TaskDetail;
import com.douzkj.zjjt.repository.dao.TaskExport;
import com.douzkj.zjjt.service.ExporterService;
import com.douzkj.zjjt.utils.TimeUtils;
import com.douzkj.zjjt.web.convertor.TaskConvertor;
import com.douzkj.zjjt.web.param.CleanupRequest;
import com.douzkj.zjjt.web.param.DownloadRequest;
import com.douzkj.zjjt.web.param.TaskDetailPageRequest;
import com.douzkj.zjjt.web.vo.DownloadVO;
import com.douzkj.zjjt.web.vo.PageVO;
import com.douzkj.zjjt.web.vo.TaskDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController()
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskDetailRepository taskDetailRepository;

    @Autowired
    private AlgoServer algoServer;

    @Autowired
    private ExporterService exporterService;

    @Autowired
    private TaskExportRepository exportRepository;
    @Autowired
    private TaskExportRepository taskExportRepository;

    @RequestMapping("/page")
    public R<PageVO<TaskDetailVO>> page(TaskDetailPageRequest request) {
        LambdaQueryWrapper<TaskDetail> wrapper = (LambdaQueryWrapper<TaskDetail>) request.toWrapper();
        wrapper.orderByDesc(TaskDetail::getId);
        Page<TaskDetail> page = taskDetailRepository.page(new Page<>(request.getCurrent(), request.getPageSize()), wrapper);
        return R.success(PageVO.of(page, TaskConvertor.INSTANCE::do2Vo));
    }


    @PostMapping("/cleanup")
    public R<CleanupCount> cleanup(@RequestBody CleanupRequest cleanupRequest) {
        Response<CleanupCount> cleanupCountResponse = algoServer.cleanupSimilarImages(cleanupRequest.getFolder());
        if (cleanupCountResponse.isOK()) {
            return R.success(cleanupCountResponse.getData());
        }
        return R.fail("执行清理作业失败: " + cleanupCountResponse.getMsg());
    }


    @PostMapping("/download")
    public R<DownloadVO> download(@RequestBody @Valid  DownloadRequest downloadRequest) throws Exception {
        String downloadId = TimeUtils.generateDateId();
        TaskExport export = new TaskExport();;
        export.setExportId(downloadId);
        export.setStartedAtMs(System.currentTimeMillis());
        boolean save = exportRepository.save(export);
        if (save) {
            exporterService.add(downloadId, downloadRequest);
        }
        DownloadVO downloadVO = new DownloadVO();
        downloadVO.setDownloadId(downloadId);
        return R.success(downloadVO);
    }


    @GetMapping("/download/status")
    public R<TaskExport> downloadList(@RequestParam("downloadId") String downloadId) {
        TaskExport exporter = taskExportRepository.getByExportId(downloadId);
        return R.success(exporter);
    }
}
