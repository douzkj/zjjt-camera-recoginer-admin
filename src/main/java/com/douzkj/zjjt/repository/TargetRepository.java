package com.douzkj.zjjt.repository;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.douzkj.zjjt.infra.config.TargetProps;
import com.douzkj.zjjt.repository.dao.Target;
import com.douzkj.zjjt.repository.entity.TargetStatistic;
import com.google.common.base.Charsets;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.List;

@Repository
@Data
public class TargetRepository {

    private final TargetProps targetProps;

    public List<Target> getTargets() {
        JSONArray json = JSONUtil.readJSONArray(new File(targetProps.getClassification()), Charsets.UTF_8);
        return json.toList(Target.class);
    }


    public TargetStatistic getTargetStatistic() {
        File file = new File(targetProps.getStatistic());
        JSONObject jsonObject = JSONUtil.readJSONObject(file, Charsets.UTF_8);
        TargetStatistic targetStatistic = new TargetStatistic();

        jsonObject.getJSONObject("number_of_instances_per_category").forEach((key, value) -> {
            targetStatistic.getInstances().put(key, (Integer) value);
        });
        jsonObject.getJSONObject("number_of_images_per_category").forEach((key, value) -> {
            targetStatistic.getImages().put(key, (Integer) value);
        });
        targetStatistic.setNumImages(jsonObject.getLong("num_images"));
        targetStatistic.setNumInstances(jsonObject.getLong("num_instances"));
        return targetStatistic;
    }


}
