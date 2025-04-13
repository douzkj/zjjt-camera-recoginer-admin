package com.douzkj.zjjt.web;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.douzkj.zjjt.entity.Response;
import com.douzkj.zjjt.repository.CameraRepository;
import com.douzkj.zjjt.repository.SignalRepository;
import com.douzkj.zjjt.repository.dao.Signal;
import com.douzkj.zjjt.repository.dao.SignalCameraCount;
import com.douzkj.zjjt.service.SignalService;
import com.douzkj.zjjt.web.convertor.SignalConvertor;
import com.douzkj.zjjt.web.param.*;
import com.douzkj.zjjt.web.vo.PageVO;
import com.douzkj.zjjt.web.vo.SignalVO;
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

    @GetMapping("/list")
    public Response<List<SignalVO>> list() {
        List<SignalCameraCount> signalCameraCounts = signalRepository.listCameraCnt();
        return Response.success(SignalConvertor.INSTANCE.do2CameraVo(signalCameraCounts));
    }


    @GetMapping("/page")
    public Response<PageVO<SignalVO>> page(@Valid SignalPageRequest pageRequest) {
        LambdaQueryWrapper<Signal> wrapper = pageRequest.toWrapper();
        wrapper.orderByDesc(Signal::getId);
        Page<Signal> page = signalRepository.page(new Page<>(pageRequest.getCurrent(), pageRequest.getPageSize()), wrapper);
        List<Signal> records = page.getRecords();
        if (! CollectionUtils.isEmpty(records)) {
            List<Long> signalIds = records.stream().map(Signal::getId).collect(Collectors.toList());
            Map<Long, Long> longLongMap = cameraRepository.countBySignalIds(signalIds);
        }
        return Response.success(PageVO.of(page, SignalConvertor.INSTANCE::do2Vo));
    }


    @PostMapping("/create")
    public Response<SignalVO> create(@RequestBody  @Valid SignalCreateParam signalCreateParam) {
        Signal signal = SignalConvertor.INSTANCE.create2Do(signalCreateParam);
        signalRepository.save(signal);
        return Response.success(SignalConvertor.INSTANCE.do2Vo(signal));
    }

    @PostMapping("/update")
    public Response<SignalVO> update(@RequestBody @Valid SignalUpdateParam signalUpdateParam) {
        Signal signal = SignalConvertor.INSTANCE.update2Do(signalUpdateParam);
        signalRepository.updateById(signal);
        return Response.success(SignalConvertor.INSTANCE.do2Vo(signal));
    }


    @PostMapping("/open")
    public Response<Boolean> open(@RequestBody @Valid SignalOpenParam signalOpenParam) {
        long closedAtMs = 0L;
        if ("auto".equals(signalOpenParam.getStopType()) && signalOpenParam.getPeriod() != null) {
            closedAtMs = System.currentTimeMillis() + signalOpenParam.getPeriod() * 1000 * 60 * 60;
        }
        boolean open = signalService.open(signalOpenParam.getId(), closedAtMs);
        return Response.success(open);
    }

    @PostMapping("/close")
    public Response<Boolean> close(@RequestBody @Valid SignalCloseParam signalCloseParam) {
        boolean ret = signalService.close(signalCloseParam.getId());
        return Response.success(ret);
    }
}
