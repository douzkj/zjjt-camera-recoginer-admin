package com.douzkj.zjjt.web;

import com.douzkj.zjjt.entity.R;
import com.douzkj.zjjt.infra.algo.AlgoServer;
import com.douzkj.zjjt.infra.algo.Response;
import com.douzkj.zjjt.web.vo.AlgoRecoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/algo")
@Slf4j
public class AlgoController {

    @Resource
    private AlgoServer algoServer;

    @PostMapping(value = "/reco/batch", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<List<AlgoRecoVO>> recoBatch(
            @RequestParam("ids") List<String> ids,
            @RequestParam("files") List<MultipartFile> files
    ) {

        // 校验ids和files数量是否匹配
        if (ids.size() != files.size()) {
            return R.fail("ids和files数量不匹配");
        }

        String tempDir = algoServer.getAlgoServerProps().getAnnotationTempDir();
        List<AlgoRecoVO> algoRecoVOS = new ArrayList<>();
        // 并行处理每个图片文件
        IntStream.range(0, ids.size()).parallel().forEach(index -> {
            String id = ids.get(index);
            MultipartFile file = files.get(index);
            String originalFilename = file.getOriginalFilename();
            // 生成唯一文件名 (使用id避免重复)
            String uniqueFileName = id + "_" + originalFilename;
            Path savePath = Paths.get(tempDir, uniqueFileName);
            try {
                // 保存文件到本地路径
                Files.copy(file.getInputStream(), savePath);
                // 调用算法服务接口处理图片
//                String tmpPath = "/data/zjjt_camera_recognizer/algo/dataset_raw/general/15-20250924193922-bba36ba80e3e4e37a5cb154adb827279.jpg";
                Response<AlgoRecoVO> algoResponse = algoServer.annotationImage(savePath.toString(), id);
//                Response<AlgoRecoVO> algoResponse = algoServer.annotationImage(tmpPath, id);

                // 存储处理结果，与id关联
                if (algoResponse.isOK()) {
                    algoRecoVOS.add(algoResponse.getData());
                } else {
                    algoRecoVOS.add(AlgoRecoVO.error(id, algoResponse.getMsg()));
                }
            } catch (Exception e) {
//                log.error("处理图片 {} 失败", uniqueFileName, e);
                algoRecoVOS.add(AlgoRecoVO.error(id, "处理图片失败. msg=" + e.getMessage()));
            } finally {
                if (savePath.toFile().exists()) {
                    savePath.toFile().delete();
                }
            }
        });
        return R.success(algoRecoVOS);
    }
}
