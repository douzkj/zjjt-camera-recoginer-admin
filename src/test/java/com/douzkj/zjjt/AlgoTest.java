package com.douzkj.zjjt;


import com.douzkj.zjjt.infra.algo.AlgoServer;
import com.douzkj.zjjt.infra.algo.Response;
import com.douzkj.zjjt.web.vo.AlgoRecoVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = VideoRecognizerApplication.class)
@Slf4j
public class AlgoTest {

    @Autowired
    private AlgoServer algoServer;

    // 并发请求数量
    private static final int CONCURRENT_REQUESTS = 20;
    // 测试用图片路径(请确保resources/test-images目录下有测试图片)
    private static final String TEST_IMAGE_PATH = "test-images/test.jpg";

    @Test
    public void testConcurrentAnnotationRequests() throws IOException, InterruptedException {


        // 计时工具
        long startTime = System.currentTimeMillis();

        // 创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(CONCURRENT_REQUESTS);
        CountDownLatch countDownLatch = new CountDownLatch(CONCURRENT_REQUESTS);
        List<Future<Long>> futures = new ArrayList<>();
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        // 提交20个并发任务
        for (int i = 0; i < CONCURRENT_REQUESTS; i++) {
            final int requestId = i;
            futures.add(executorService.submit(() -> {
                long taskStartTime = System.currentTimeMillis();
                try {
                    // 生成唯一ID和临时文件路径
                    String uniqueId = String.format("%s", requestId);

                    // 调用算法服务
                    Response<AlgoRecoVO> response = algoServer.annotationImage(
                            "/data/zjjt_camera_recognizer/algo/dataset_raw/general/15-20250924193922-bba36ba80e3e4e37a5cb154adb827279.jpg", uniqueId);

                    if (response.isOK()) {
                        successCount.incrementAndGet();
                    } else {
                        failCount.incrementAndGet();
                        System.err.println("请求" + requestId + "失败: " + response.getMsg());
                    }
                    return System.currentTimeMillis() - taskStartTime;
                } catch (Exception e) {
                    failCount.incrementAndGet();
                    System.err.println("请求" + requestId + "发生异常: " + e.getMessage());
                    log.error("请求" + requestId + "发生异常: " + e.getMessage(), e);
                    return System.currentTimeMillis() - taskStartTime;
                } finally {
                    countDownLatch.countDown();
                }
            }));
        }

        // 等待所有任务完成
        countDownLatch.await();
        long totalTime = System.currentTimeMillis() - startTime;

        // 统计各任务耗时
        List<Long> taskTimes = new ArrayList<>();
        for (Future<Long> future : futures) {
            try {
                taskTimes.add(future.get());
            } catch (Exception e) {
                taskTimes.add(-1L); // 标记异常任务
            }
        }

        // 输出统计结果
        System.out.println("===== 并发测试结果 =====");
        System.out.println("总请求数: " + CONCURRENT_REQUESTS);
        System.out.println("成功数: " + successCount.get());
        System.out.println("失败数: " + failCount.get());
        System.out.println("总耗时: " + totalTime + "ms");
        System.out.println("平均耗时: " + (totalTime / CONCURRENT_REQUESTS) + "ms");
        System.out.println("各任务耗时(ms): " + taskTimes);

        // 关闭线程池
        executorService.shutdown();
    }

}
