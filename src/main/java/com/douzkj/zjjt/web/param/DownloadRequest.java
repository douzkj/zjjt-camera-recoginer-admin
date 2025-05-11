package com.douzkj.zjjt.web.param;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class DownloadRequest implements Serializable {

    @NotNull
    private TaskDetailPageRequest page;


    @Valid
    private DownloadOptions options;

    @Data
    public static class DownloadOptions implements Serializable {

        private Boolean frameImage = Boolean.TRUE;

        private Boolean labelImage;

        private Boolean labelJson;
    }
}
