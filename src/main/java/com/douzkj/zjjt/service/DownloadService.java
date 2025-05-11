package com.douzkj.zjjt.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.douzkj.zjjt.repository.TaskDetailRepository;
import com.douzkj.zjjt.repository.dao.TaskDetail;
import com.douzkj.zjjt.web.param.DownloadRequest;
import com.douzkj.zjjt.web.param.TaskDetailPageRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Files;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Data
@Slf4j
public class DownloadService {

    private  final TaskDetailRepository taskDetailRepository;

    public static final int BATCH_SIZE = 1000;


    private void attachFileToZip(String filePath, ZipOutputStream zos)  {
        try {
            if (filePath != null && !filePath.isEmpty()) {
                File imageFile = new File(filePath);
                if (imageFile.exists()) {
                    // 添加文件到 ZIP
                    try (FileInputStream fis = new FileInputStream(imageFile)) {
                        ZipEntry zipEntry = new ZipEntry(imageFile.getName());
                        zos.putNextEntry(zipEntry);
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = fis.read(buffer)) > 0) {
                            zos.write(buffer, 0, length);
                        }
                        zos.closeEntry();
                    }
                }
            }
        } catch (Exception e) {
            log.error("attachFileToZip error. filepath={}", filePath, e);
        }
    }

    public File startDownload(String downloadId, DownloadRequest downloadRequest) throws IOException {
        TaskDetailPageRequest pageRequest = downloadRequest.getPage();
        Wrapper<TaskDetail> wrapper = pageRequest.toWrapper();
        int total = taskDetailRepository.count(wrapper);
        long totalPages = (total + BATCH_SIZE - 1) / BATCH_SIZE;
        // 创建临时文件用于存储 ZIP 文件
        File zipFile = File.createTempFile(String.format("download-%s-", downloadId), ".zip");
        DownloadRequest.DownloadOptions options = downloadRequest.getOptions();
        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            for (long page = 1; page <= totalPages; page++) {
                Page<TaskDetail> pageParam = new Page<>(page, BATCH_SIZE);
                Page<TaskDetail> taskDetailPage = taskDetailRepository.page(pageParam, wrapper);
                List<TaskDetail> taskDetails = taskDetailPage.getRecords();
                for (TaskDetail taskDetail : taskDetails) {
                    String frameImagePath = taskDetail.getFrameImagePath();
                    String labelImagePath = taskDetail.getLabelImagePath();
                    String labelJsonPath = taskDetail.getLabelJsonPath();
                    if (options.getFrameImage()) {
                        attachFileToZip(frameImagePath, zos);
                    }
                    if (options.getLabelImage()) {
                        attachFileToZip(labelImagePath, zos);
                    }
                    if (options.getLabelJson()) {
                        attachFileToZip(labelJsonPath, zos);
                    }
                }
            }
        }
        return zipFile;
    }
}
