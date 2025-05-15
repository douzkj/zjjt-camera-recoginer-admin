package com.douzkj.zjjt.web.param;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
public class DownloadRequest implements Serializable {

    @NotNull
    private TaskDetailPageRequest page;


    @Valid
    private DownloadOptions options;


    private List<Long> idList;

    @Data
    public static class DownloadOptions implements Serializable {

        private Boolean frameImage = Boolean.TRUE;

        private Boolean labelImage;

        private Boolean labelJson;
    }
}
