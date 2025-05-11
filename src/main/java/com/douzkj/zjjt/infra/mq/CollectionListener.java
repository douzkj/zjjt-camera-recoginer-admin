package com.douzkj.zjjt.infra.mq;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.douzkj.zjjt.entity.TaskCollection;
import com.douzkj.zjjt.entity.TaskV2Collection;
import com.douzkj.zjjt.repository.CameraRepository;
import com.douzkj.zjjt.repository.SignalRepository;
import com.douzkj.zjjt.repository.TaskDetailRepository;
import com.douzkj.zjjt.repository.TaskRepository;
import com.douzkj.zjjt.repository.dao.Camera;
import com.douzkj.zjjt.repository.dao.TaskDetail;
import com.douzkj.zjjt.task.RecognizerTask;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

import static com.douzkj.zjjt.task.RecognizerQueue.COLLECTION_QUEUE;
import static com.douzkj.zjjt.task.RecognizerQueue.COLLECTION_QUEUE_V2;

@Component
@Slf4j
@Data
public class CollectionListener {

    private final TaskRepository taskRepository;

    private final TaskDetailRepository taskDetailRepository;

    private final CameraRepository cameraRepository;

    private final SignalRepository signalRepository;


    @RabbitListener(queues = COLLECTION_QUEUE, concurrency = "8-32")
    public void listen(TaskCollection message) {
        log.info("接收设备采集信息: {}", message);
        RecognizerTask task = message.getTask();
        RecognizerTask.CameraDTO camera = task.getCamera();
        RecognizerTask.SignalDTO signal = task.getSignal();
        String taskId = task.getTaskId();
        Map<String, Object> collect = message.getCollect();
        JSONObject collectJsonObj = JSON.parseObject(JSON.toJSONString(collect));
        TaskDetail taskDetail = new TaskDetail();
        taskDetail.setTaskId(taskId);
        taskDetail.setSignalId(Convert.toLong(signal.getSignalId()));
        taskDetail.setCameraIndexCode(camera.getIndexCode());
        taskDetail.setCameraName(camera.getName());
        Camera existsCamera = cameraRepository.getByIndexCode(camera.getIndexCode());
        if (existsCamera != null) {
            taskDetail.setCameraAddr(existsCamera.getAddr());
            taskDetail.setRegionPathName(existsCamera.getRegionPathName());
            taskDetail.setRegionIndexCode(existsCamera.getRegionIndexCode());
        }
        String frameStoragePath = Convert.toStr(collectJsonObj.getByPath("$.frame.frameImagePath"));
        taskDetail.setFrameImagePath(frameStoragePath);
        taskDetail.setFrameTimeMs(Convert.toLong(collectJsonObj.getByPath("$.frame.timestamp")));
        taskDetail.setLabelImagePath(Convert.toStr(collectJsonObj.getByPath("$.label.labelImagePath")));
        taskDetail.setLabelJsonPath(Convert.toStr(collectJsonObj.getByPath("$.label.labelJsonPath")));
        Object shapesLabels =  collectJsonObj.getByPath("$.label.shapes[*].label");
        HashSet<String> labels = new HashSet<>();
        if (shapesLabels instanceof Collection && !((Collection<?>) shapesLabels).isEmpty()) {
            JSONArray shapesLabelsArr = (JSONArray) shapesLabels;
            for (Object shapesLabel : shapesLabelsArr) {
                if ( ! Objects.isNull(shapesLabel)) {
                    labels.add(shapesLabel.toString());
                }
            }
        }
        taskDetail.setLabelTypes(String.join(",", labels));
        taskDetail.setLabelTimeMs(Convert.toLong(collectJsonObj.getByPath("$.label.timestamp")));
        taskDetailRepository.save(taskDetail);
        if ( ! frameStoragePath.contains(camera.getIndexCode())) {
            log.error("设备采集数据错误：id : {}, camera: {}, storage_path = {}", taskDetail.getId(), camera.getIndexCode(), frameStoragePath);
        }

    }


    @RabbitListener(queues = COLLECTION_QUEUE_V2, concurrency = "8-32")
    public void listenV2(TaskV2Collection message) {
//        log.info("接收设备采集信息: {}", message);
        RecognizerTask.CameraDTO camera = message.getCamera();
        RecognizerTask.SignalDTO signal = message.getSignal();
        String taskId = message.getTaskId();
        Map<String, Object> collect = message.getCollect();
        JSONObject collectJsonObj = JSON.parseObject(JSON.toJSONString(collect));
        TaskDetail taskDetail = new TaskDetail();
        taskDetail.setTaskId(taskId);
        taskDetail.setSignalId(Convert.toLong(signal.getSignalId()));
        taskDetail.setCameraIndexCode(camera.getIndexCode());
        Camera existsCamera = cameraRepository.getByIndexCode(camera.getIndexCode());
        if (existsCamera != null) {
            taskDetail.setCameraName(existsCamera.getName());
            taskDetail.setCameraAddr(existsCamera.getAddr());
            taskDetail.setRegionPathName(existsCamera.getRegionPathName());
            taskDetail.setRegionIndexCode(existsCamera.getRegionIndexCode());
        }

        String frameStoragePath = Convert.toStr(collectJsonObj.getByPath("$.frame.frameImagePath"));
        taskDetail.setFrameImagePath(frameStoragePath);
        taskDetail.setFrameTimeMs(Convert.toLong(collectJsonObj.getByPath("$.frame.timestamp")));
        taskDetail.setLabelImagePath(Convert.toStr(collectJsonObj.getByPath("$.label.labelImagePath")));
        taskDetail.setLabelJsonPath(Convert.toStr(collectJsonObj.getByPath("$.label.labelJsonPath")));
        Object shapesLabels =  collectJsonObj.getByPath("$.label.shapes[*].label");
        HashSet<String> labels = new HashSet<>();
        if (shapesLabels instanceof Collection && !((Collection<?>) shapesLabels).isEmpty()) {
            JSONArray shapesLabelsArr = (JSONArray) shapesLabels;
            for (Object shapesLabel : shapesLabelsArr) {
                if ( ! Objects.isNull(shapesLabel)) {
                    labels.add(shapesLabel.toString());
                }
            }
        }
        taskDetail.setLabelTypes(String.join(",", labels));
        taskDetail.setLabelTimeMs(Convert.toLong(collectJsonObj.getByPath("$.label.timestamp")));
        taskDetailRepository.save(taskDetail);
        if ( frameStoragePath != null && !frameStoragePath.contains(camera.getIndexCode())) {
            log.error("设备采集数据错误：id : {}, camera: {}, storage_path = {}", taskDetail.getId(), camera.getIndexCode(), frameStoragePath);
        }
    }





    public static void main(String[] args) {
        String js = "{\"version\":\"5.1.1\",\"flags\":{},\"shapes\":[{\"label\":\"hook\",\"points\":[[1068,446],[1057,448],[1053,449],[1051,452],[1050,460],[1050,490],[1051,557],[1053,586],[1054,619],[1056,648],[1057,674],[1059,688],[1060,718],[1062,740],[1063,768],[1065,789],[1066,832],[1068,850],[1069,887],[1071,910],[1072,961],[1074,972],[1075,987],[1077,995],[1078,1007],[1080,1015],[1081,1022],[1085,1025],[1090,1026],[1131,1028],[1168,1027],[1173,1023],[1174,1012],[1174,942],[1173,883],[1171,871],[1170,838],[1168,809],[1167,785],[1165,767],[1164,712],[1162,696],[1161,654],[1159,607],[1158,553],[1156,529],[1155,476],[1153,468],[1152,461],[1150,456],[1149,452],[1147,450],[1143,447],[1134,446]],\"group_id\":null,\"shape_type\":\"polygon\",\"flags\":{},\"probability\":0.9998601675033569}],\"imagePath\":\"/data/zjjt_camera_recognizer/algo/dataset_raw/hook/2289225ac9004afe9428d3d1e18464a9-20250419190838.jpg\",\"imageHeight\":1080,\"imageWidth\":1920}";

        com.alibaba.fastjson2.JSONObject jsObj = JSON.parseObject(js);
//        JSONPath path = JSONPath.of("$.shapes[*].label");
        JSONArray shapesLabels = (JSONArray) jsObj.getByPath("$.shapes[*].label");
        // 缓存起来重复使用能提升性能        JSONArray shapesLabels = JSONUtil.parseObj(js).getByPath("$.shapes[*].label", JSONArray.class);
        HashSet<String> labels = new HashSet<>();
        if (! CollectionUtils.isEmpty(shapesLabels)) {
            for (Object shapesLabel : shapesLabels) {
                if ( ! Objects.isNull(shapesLabel)) {
                    labels.add(shapesLabel.toString());
                }
            }
        }
        System.out.println(labels);

        System.out.println(String.join(",", Lists.newArrayList()));
    }
}
