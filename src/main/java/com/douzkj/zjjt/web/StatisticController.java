package com.douzkj.zjjt.web;

import com.douzkj.zjjt.entity.R;
import com.douzkj.zjjt.repository.TargetRepository;
import com.douzkj.zjjt.repository.dao.Target;
import com.douzkj.zjjt.repository.entity.TargetStatistic;
import com.douzkj.zjjt.web.convertor.TargetConvertor;
import com.douzkj.zjjt.web.vo.TargetLabelVO;
import com.douzkj.zjjt.web.vo.TargetStatisticItemVO;
import com.douzkj.zjjt.web.vo.TargetStatisticVO;
import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.douzkj.zjjt.common.TargetConst.TYPE_IMAGE;
import static com.douzkj.zjjt.common.TargetConst.TYPE_INSTANCE;

@RestController
@RequestMapping("/statistic")
@Data
public class StatisticController {

    private final TargetRepository targetRepository;

    protected TargetStatisticItemVO convert(Target target) {
        TargetStatisticItemVO vo = new TargetStatisticItemVO();
        vo.setTarget(target.getTarget());
        vo.setTargetName(target.getTargetName());
        vo.setClassification(target.getClassification());
        vo.setClassificationParentName(target.getClassificationParentName());
        vo.setValue(0);
        return vo;
    }


    @GetMapping("/chart")
    public R<TargetStatisticVO> chart() {
        TargetStatistic targetStatistic = targetRepository.getTargetStatistic();
        List<Target> targets = targetRepository.getTargets();
        TargetStatisticVO result = new TargetStatisticVO();
        List<TargetStatisticItemVO> vos = Lists.newArrayList();
        for (Target target : targets) {
            TargetStatisticItemVO instanceVo = convert(target);
            instanceVo.setStatisticType(TYPE_INSTANCE);
            vos.add(instanceVo);
            TargetStatisticItemVO imageVo = convert(target);
            imageVo.setStatisticType(TYPE_IMAGE);
            vos.add(imageVo);
        }
        targetStatistic.getInstances().forEach((key, value) -> {
            vos.stream()
                    .filter(vo -> vo.getTarget().equals(key) && vo.getStatisticType().equals(TYPE_INSTANCE))
                    .forEach(vo -> vo.setValue(value));
        });
        targetStatistic.getImages().forEach((key, value) -> {
            vos.stream()
                    .filter(vo -> vo.getTarget().equals(key) && vo.getStatisticType().equals(TYPE_IMAGE))
                    .forEach(vo -> vo.setValue(value));
        });
        result.setRecords(vos);
        result.setNumImages(targetStatistic.getNumImages());
        result.setNumInstances(targetStatistic.getNumInstances());
        return R.success(result);
    }


    @GetMapping("/auto")
    public R<TargetStatisticVO> autoLabel() {
        TargetStatistic targetStatistic = targetRepository.getSystemAutoLabeledStatistic();
        List<Target> targets = targetRepository.getTargets();
        TargetStatisticVO result = new TargetStatisticVO();
        List<TargetStatisticItemVO> vos = Lists.newArrayList();
        for (Target target : targets) {
            TargetStatisticItemVO instanceVo = convert(target);
            instanceVo.setStatisticType(TYPE_INSTANCE);
            vos.add(instanceVo);
            TargetStatisticItemVO imageVo = convert(target);
            imageVo.setStatisticType(TYPE_IMAGE);
            vos.add(imageVo);
        }
        targetStatistic.getInstances().forEach((key, value) -> {
            vos.stream()
                    .filter(vo -> vo.getTarget().equals(key) && vo.getStatisticType().equals(TYPE_INSTANCE))
                    .forEach(vo -> vo.setValue(value));
        });
        targetStatistic.getImages().forEach((key, value) -> {
            vos.stream()
                    .filter(vo -> vo.getTarget().equals(key) && vo.getStatisticType().equals(TYPE_IMAGE))
                    .forEach(vo -> vo.setValue(value));
        });
        result.setRecords(vos);
        result.setNumImages(targetStatistic.getNumImages());
        result.setNumInstances(targetStatistic.getNumInstances());
        return R.success(result);
    }


    @GetMapping("/tags")
    public R<List<TargetLabelVO>> tags() {
        List<Target> targets = targetRepository.getTargets();
        return R.success(TargetConvertor.INSTANCE.do2LabelVO(targets));
    }
}
