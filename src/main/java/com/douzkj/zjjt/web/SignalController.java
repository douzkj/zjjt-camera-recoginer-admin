package com.douzkj.zjjt.web;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.douzkj.zjjt.entity.Response;
import com.douzkj.zjjt.repository.SignalRepository;
import com.douzkj.zjjt.repository.dao.Camera;
import com.douzkj.zjjt.repository.dao.Signal;
import com.douzkj.zjjt.repository.dao.SignalCameraCount;
import com.douzkj.zjjt.web.convertor.SignalConvertor;
import com.douzkj.zjjt.web.param.PageRequest;
import com.douzkj.zjjt.web.param.SignalCreateParam;
import com.douzkj.zjjt.web.param.SignalUpdateParam;
import com.douzkj.zjjt.web.vo.PageVO;
import com.douzkj.zjjt.web.vo.SignalVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @GetMapping("/list")
    public Response<List<SignalVO>> list() {
        List<SignalCameraCount> signalCameraCounts = signalRepository.listCameraCnt();
        return Response.success(SignalConvertor.INSTANCE.do2CameraVo(signalCameraCounts));
    }


    @GetMapping("/page")
    public Response<PageVO<SignalVO>> page(PageRequest pageRequest) {
        LambdaQueryWrapper<Signal> cameraLambdaQueryWrapper = Wrappers.<Signal>lambdaQuery();
        cameraLambdaQueryWrapper.orderByDesc(Signal::getId);
        Page<Signal> page = signalRepository.page(new Page<>(pageRequest.getCurrent(), pageRequest.getPageSize()), cameraLambdaQueryWrapper);
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
}
