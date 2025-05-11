package com.douzkj.zjjt.web;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.douzkj.zjjt.entity.R;
import com.douzkj.zjjt.infra.algo.AlgoServer;
import com.douzkj.zjjt.infra.algo.Response;
import com.douzkj.zjjt.infra.algo.entity.CleanupCount;
import com.douzkj.zjjt.repository.TaskDetailRepository;
import com.douzkj.zjjt.repository.dao.TaskDetail;
import com.douzkj.zjjt.service.DownloadService;
import com.douzkj.zjjt.utils.TimeUtils;
import com.douzkj.zjjt.web.convertor.TaskConvertor;
import com.douzkj.zjjt.web.param.CleanupRequest;
import com.douzkj.zjjt.web.param.DownloadRequest;
import com.douzkj.zjjt.web.param.TaskDetailPageRequest;
import com.douzkj.zjjt.web.vo.DownloadVO;
import com.douzkj.zjjt.web.vo.PageVO;
import com.douzkj.zjjt.web.vo.TaskDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;

@RestController()
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskDetailRepository taskDetailRepository;

    @Autowired
    private AlgoServer algoServer;

    @Autowired
    private DownloadService downloadService;

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
    public R<DownloadVO> download(@RequestBody @Valid  DownloadRequest downloadRequest) throws IOException {
        String downloadId = TimeUtils.generateDateId();
        File file = downloadService.startDownload(downloadId, downloadRequest);
        DownloadVO downloadVO = new DownloadVO();
        downloadVO.setDownloadId(downloadId);
        downloadVO.setFilepath(file.getAbsolutePath());
        return R.success(downloadVO);
    }


    @PostMapping("/download-list")
    public R<Boolean> downloadList(@RequestBody TaskDetailPageRequest request) {
        Wrapper<TaskDetail> wrapper = request.toWrapper();
        int page = 1;
        long pageSize = 100;
        long latestPageSize = 100;
        while (latestPageSize == pageSize) {
            Page<TaskDetail> pageResult = taskDetailRepository.page(new Page<>(page, pageSize), wrapper);
            latestPageSize = pageResult.getSize();
        }

        return R.success();
    }
}
