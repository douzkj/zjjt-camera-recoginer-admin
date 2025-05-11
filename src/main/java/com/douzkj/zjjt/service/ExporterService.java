package com.douzkj.zjjt.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.douzkj.zjjt.repository.TaskDetailRepository;
import com.douzkj.zjjt.repository.TaskExportRepository;
import com.douzkj.zjjt.repository.dao.TaskDetail;
import com.douzkj.zjjt.web.param.DownloadRequest;
import com.douzkj.zjjt.web.param.TaskDetailPageRequest;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.zip.ParallelScatterZipCreator;
import org.apache.commons.compress.archivers.zip.UnixStat;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.springframework.aop.framework.AopContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Data
@Slf4j
public class ExporterService {

    private  final TaskDetailRepository taskDetailRepository;

    private final TaskExportRepository taskExportRepository;

    public static final int BATCH_SIZE = 1000;

    private static ZipArchiveEntry createZipEntry(File file) {
        ZipArchiveEntry entry = new ZipArchiveEntry(file.getName());
        entry.setMethod(ZipArchiveEntry.DEFLATED);
        entry.setSize(file.length());
        entry.setUnixMode(UnixStat.FILE_FLAG | 0755); // 设置文件权限
        return entry;
    }


    private void attachFileToZip(ParallelScatterZipCreator scatterCreator, String filepath) {
        try {
            if (filepath == null || filepath.isEmpty()) return;
            File imageFile = new File(filepath);
            if (!imageFile.exists()) return;
            scatterCreator.addArchiveEntry(
                    createZipEntry(imageFile),
                    () -> {
                        try {
                            return Files.newInputStream(imageFile.toPath());
                        } catch (IOException e) {
                            log.error("attachFileToZip error. filepath={}", filepath,  e);
                            return null;
                        }
                    }
            );
        } catch (Exception e) {
            log.error("attachFileToZip error. filepath={}", filepath, e);
        }
    }

    private void attachFileToZip(String filePath, ZipOutputStream zos)  {
        try {
            if (filePath == null || filePath.isEmpty()) return;
            File imageFile = new File(filePath);
            if (!imageFile.exists()) return;
            // 添加文件到 ZIP
            try (FileInputStream fis = new FileInputStream(imageFile)) {
                ZipEntry zipEntry = new ZipEntry(imageFile.getName());
                zos.putNextEntry(zipEntry);
                byte[] buffer = new byte[8192];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
                zos.closeEntry();
            }
        } catch (Exception e) {
            log.error("attachFileToZip error. filepath={}", filePath, e);
        }
    }
    @Async("asyncExecutor")
    public void add(String downloadId, DownloadRequest downloadRequest) {
        try {
            File file = startDownload(downloadId, downloadRequest);
            taskExportRepository.finished(downloadId, file.getAbsolutePath());
            log.info("Download completed for downloadId: {}", downloadId);
        } catch (Exception e) {
            taskExportRepository.failed(downloadId);
            log.error("Error during download for downloadId: {}", downloadId, e);
        }
    }

    public File startDownload(String downloadId, DownloadRequest downloadRequest) throws IOException, ExecutionException, InterruptedException {
        TaskDetailPageRequest pageRequest = downloadRequest.getPage();
        Wrapper<TaskDetail> wrapper = pageRequest.toWrapper();
        int total = taskDetailRepository.count(wrapper);
        long totalPages = (total + BATCH_SIZE - 1) / BATCH_SIZE;
        // 创建临时文件用于存储 ZIP 文件
        // 1. 初始化线程池（建议核心线程数=CPU核心数）
        ExecutorService zipExecutor = new ThreadPoolExecutor(
                16, 128, 60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                new ThreadFactoryBuilder().setNameFormat("zip-pool-%d").build()
        );
        ExecutorService executor = Executors.newFixedThreadPool((int) Math.min(32, totalPages));
        File zipFile = File.createTempFile(String.format("download-%s-", downloadId), ".zip");
        DownloadRequest.DownloadOptions options = downloadRequest.getOptions();
        List<Future<Void>> futures = new ArrayList<>();
        try (ZipArchiveOutputStream zos = new ZipArchiveOutputStream(Files.newOutputStream(zipFile.toPath()))) {
            ParallelScatterZipCreator scatterCreator = new ParallelScatterZipCreator(zipExecutor);
            for (long page = 1; page <= totalPages; page++) {
                final long currentPage = page;
                CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
                    log.info("startDownload start page={}", currentPage);
                    Page<TaskDetail> taskDetailPage = taskDetailRepository.page(new Page<>(currentPage, BATCH_SIZE), wrapper);
                    return taskDetailPage.getRecords();
                }, executor).thenAccept(taskDetails -> {
                    log.info("compress tasks. records={}", taskDetails.size());
                    for (TaskDetail taskDetail : taskDetails) {
                        String frameImagePath = taskDetail.getFrameImagePath();
                        String labelImagePath = taskDetail.getLabelImagePath();
                        String labelJsonPath = taskDetail.getLabelJsonPath();
                        if (options.getFrameImage()) {
                            attachFileToZip(scatterCreator, frameImagePath);
                        }
                        if (options.getLabelImage()) {
                            attachFileToZip(scatterCreator, labelImagePath);
                        }
                        if (options.getLabelJson()) {
                            attachFileToZip(scatterCreator, labelJsonPath);
                        }
                    }
                });
                futures.add(future);
            }
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            scatterCreator.writeTo(zos);
        }
        return zipFile;
    }
}
