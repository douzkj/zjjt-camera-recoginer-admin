package com.douzkj.zjjt.web;

import com.douzkj.zjjt.entity.R;
import com.douzkj.zjjt.repository.TargetRepository;
import com.douzkj.zjjt.repository.dao.Target;
import com.douzkj.zjjt.repository.entity.TargetStatistic;
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
@RequestMapping("/chart")
@Data
public class ChartController {

    private final TargetRepository targetRepository;

    protected TargetStatisticVO convert(Target target) {
        TargetStatisticVO vo = new TargetStatisticVO();
        vo.setTarget(target.getTarget());
        vo.setTargetName(target.getTargetName());
        vo.setClassification(target.getClassification());
        vo.setValue(0);
        return vo;
    }


    @GetMapping("/statistic")
    public R<List<TargetStatisticVO>> statistic() {
        TargetStatistic targetStatistic = targetRepository.getTargetStatistic();
        List<Target> targets = targetRepository.getTargets();
        List<TargetStatisticVO> vos = Lists.newArrayList();
        for (Target target : targets) {
            TargetStatisticVO instanceVo = convert(target);
            instanceVo.setStatisticType(TYPE_INSTANCE);
            vos.add(instanceVo);
            TargetStatisticVO imageVo = convert(target);
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
        return R.success(vos);
    }
}
