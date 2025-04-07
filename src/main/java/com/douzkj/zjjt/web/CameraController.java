package com.douzkj.zjjt.web;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.douzkj.zjjt.entity.Response;
import com.douzkj.zjjt.repository.CameraRepository;
import com.douzkj.zjjt.repository.SignalRepository;
import com.douzkj.zjjt.repository.dao.Camera;
import com.douzkj.zjjt.repository.dao.Signal;
import com.douzkj.zjjt.web.convertor.CameraConvertor;
import com.douzkj.zjjt.web.param.CameraPageRequest;
import com.douzkj.zjjt.web.param.CameraSignalBindParam;
import com.douzkj.zjjt.web.vo.CameraVO;
import com.douzkj.zjjt.web.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 设备API
 *
 * @author ranger dong
 * @date 20:47 2025/3/23
 * @descrption
 * @copyright Spotter.ink
 */
@RestController()
@RequestMapping("/camera")
public class CameraController {

    @Autowired
    private CameraRepository cameraRepository;

    @Autowired
    private SignalRepository signalRepository;


    @GetMapping("/page")
    public Response<PageVO<CameraVO>> page(@Valid CameraPageRequest param) {
        Wrapper<Camera> wrapper = param.toWrapper();
        Page<Camera> page = cameraRepository.page(new Page<>(param.getCurrent(), param.getPageSize()), wrapper);
        Set<Long> signalIds = page.getRecords().stream()
                .map(Camera::getSignalId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        List<Signal> signals = ! CollectionUtils.isEmpty(signalIds) ? signalRepository.listByIds(signalIds) : Collections.emptyList();
        return Response.success(PageVO.of(page, o -> {
            CameraVO cameraVO = CameraConvertor.INSTANCE.do2Vo(o);
            signals.stream().filter(s -> Objects.equals(s.getId(), o.getSignalId())).findFirst()
                    .ifPresent(signal -> cameraVO.setSignalName(signal.getName()));
            return cameraVO;
        }));
    }


    /**
     * 绑定设备至指定通路
     */
    @PostMapping("bind")
    public Response<Boolean> bind(@RequestBody @Valid CameraSignalBindParam bindParam) {
        Long signalId = bindParam.getSignalId();
        boolean res = false;
        if (signalId == null) {
            res = cameraRepository.unbind(bindParam.getCameraIds());
        } else {
            res = cameraRepository.bind(bindParam.getCameraIds(), signalId);
        }
        return Response.success(res);
    }

}
