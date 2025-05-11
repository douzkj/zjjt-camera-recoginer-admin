package com.douzkj.zjjt.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.douzkj.zjjt.entity.R;
import com.douzkj.zjjt.repository.CameraRepository;
import com.douzkj.zjjt.repository.SignalRepository;
import com.douzkj.zjjt.repository.TaskDetailRepository;
import com.douzkj.zjjt.repository.TaskRepository;
import com.douzkj.zjjt.repository.dao.Signal;
import com.douzkj.zjjt.repository.dao.SignalCameraCount;
import com.douzkj.zjjt.repository.dao.Task;
import com.douzkj.zjjt.repository.entity.TaskCollectCountVO;
import com.douzkj.zjjt.service.SignalService;
import com.douzkj.zjjt.web.convertor.SignalConvertor;
import com.douzkj.zjjt.web.convertor.TaskConvertor;
import com.douzkj.zjjt.web.param.*;
import com.douzkj.zjjt.web.vo.PageVO;
import com.douzkj.zjjt.web.vo.SignalVO;
import com.douzkj.zjjt.web.vo.TaskVO;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 通路接口
 *
 * @author ranger dong
 * @date 20:58 2025/3/23
 * @descrption
 * @copyright Spotter.ink
 */
@RestController()
@RequestMapping("/signal")
public class SignalController {

    @Autowired
    private SignalRepository signalRepository;

    @Autowired
    private CameraRepository cameraRepository;
    @Autowired
    private SignalService signalService;

    @Autowired
    private TaskDetailRepository taskDetailRepository;
    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("/list")
    public R<List<SignalVO>> list() {
        List<SignalCameraCount> signalCameraCounts = signalRepository.listCameraCnt();
        return R.success(SignalConvertor.INSTANCE.do2CameraVo(signalCameraCounts));
    }


    @GetMapping("/page")
    public R<PageVO<SignalVO>> page(@Valid SignalPageRequest pageRequest) {
        LambdaQueryWrapper<Signal> wrapper = pageRequest.toWrapper();
        wrapper.orderByDesc(Signal::getId);
        Page<Signal> page = signalRepository.page(new Page<>(pageRequest.getCurrent(), pageRequest.getPageSize()), wrapper);
        List<Signal> records = page.getRecords();
        Map<Long, Integer> cameraCountMap;
        Map<Long, TaskCollectCountVO> collectCountMap;
        if (! CollectionUtils.isEmpty(records)) {
            List<Long> signalIds = records.stream().map(Signal::getId).collect(Collectors.toList());
            Map<Long, Integer> ret = cameraRepository.countBySignalIds(signalIds);
            if (! CollectionUtils.isEmpty(ret)) {
                cameraCountMap = ret;
            } else {
                cameraCountMap = Maps.newHashMap();
            }
            // 获取任务的总量数据
            List<TaskCollectCountVO> taskCollectCountVOS = taskDetailRepository.countImageBySignalIds(signalIds);
            if (! CollectionUtils.isEmpty(taskCollectCountVOS)) {
                collectCountMap = taskCollectCountVOS.stream().collect(Collectors.toMap(TaskCollectCountVO::getSignalId, t -> t));
            } else {
                collectCountMap = Maps.newHashMap();
            }
        } else {
            collectCountMap = Maps.newHashMap();
            cameraCountMap = Maps.newHashMap();
        }

        return R.success(PageVO.of(page, (r) -> {
            SignalVO signalVO = SignalConvertor.INSTANCE.do2Vo(r);
            signalVO.setCameraCnt(cameraCountMap.get(r.getId()));
            if (collectCountMap.containsKey(r.getId())) {
                TaskCollectCountVO taskCollectCountVO = collectCountMap.get(r.getId());
                signalVO.setFrameImageCnt(taskCollectCountVO.getFrameImageCnt());
                signalVO.setLabelImageCnt(taskCollectCountVO.getLabelImageCnt());
                signalVO.setLabelJsonCnt(taskCollectCountVO.getLabelJsonCnt());
            }
            return signalVO;
        }));
    }


    @PostMapping("/create")
    public R<SignalVO> create(@RequestBody  @Valid SignalCreateParam signalCreateParam) {
        Signal signal = SignalConvertor.INSTANCE.create2Do(signalCreateParam);
        signalRepository.save(signal);
        return R.success(SignalConvertor.INSTANCE.do2Vo(signal));
    }

    @PostMapping("/update")
    public R<SignalVO> update(@RequestBody @Valid SignalUpdateParam signalUpdateParam) {
        Signal signal = SignalConvertor.INSTANCE.update2Do(signalUpdateParam);
        signalRepository.updateById(signal);
        return R.success(SignalConvertor.INSTANCE.do2Vo(signal));
    }


    @PostMapping("/open")
    public R<Boolean> open(@RequestBody @Valid SignalOpenParam signalOpenParam) {
        long closedAtMs = 0L;
        if ("auto".equals(signalOpenParam.getStopType()) && signalOpenParam.getPeriod() != null) {
            closedAtMs = (long) (System.currentTimeMillis() + signalOpenParam.getPeriod() * 1000 * 60 * 60);
        }
        boolean open = signalService.open(signalOpenParam.getId(), closedAtMs);
        return R.success(open);
    }

    @PostMapping("/close")
    public R<Boolean> close(@RequestBody @Valid SignalCloseParam signalCloseParam) {
        boolean ret = signalService.close(signalCloseParam.getId());
        return R.success(ret);
    }


    @GetMapping("/tasks")
    public R<PageVO<TaskVO>> tasks(@Valid TaskPageRequest taskPageRequest) {
        LambdaQueryWrapper<Task> wrapper = (LambdaQueryWrapper<Task>) taskPageRequest.toWrapper();
        wrapper.orderByDesc(Task::getOpenedAtMs);
        Page<Task> page = taskRepository.page(new Page<>(taskPageRequest.getCurrent(), taskPageRequest.getPageSize()), wrapper);
        Map<String, TaskCollectCountVO> collectCountMap;
        List<Task> records = page.getRecords();
        if (! CollectionUtils.isEmpty(records)) {
            List<String> taskIds = records.stream().map(Task::getTaskId).collect(Collectors.toList());
            List<TaskCollectCountVO> taskCollectCountVOS = taskDetailRepository.countImageByTaskIds(taskIds);
            // 获取任务的总量数据
            if (! CollectionUtils.isEmpty(taskCollectCountVOS)) {
                collectCountMap = taskCollectCountVOS.stream().collect(Collectors.toMap(TaskCollectCountVO::getTaskId, t -> t));
            } else {
                collectCountMap = Maps.newHashMap();
            }
        } else {
            collectCountMap = Maps.newHashMap();
        }

        return R.success(PageVO.of(page, (t) -> {
            TaskVO taskVO = TaskConvertor.INSTANCE.do2Vo(t);
            if (collectCountMap.containsKey(t.getTaskId())) {
                TaskCollectCountVO taskCollectCountVO = collectCountMap.get(t.getTaskId());
                taskVO.setFrameImageCnt(taskCollectCountVO.getFrameImageCnt());
                taskVO.setLabelImageCnt(taskCollectCountVO.getLabelImageCnt());
                taskVO.setLabelJsonCnt(taskCollectCountVO.getLabelJsonCnt());
            }
            return taskVO;
        }));
    }
}
