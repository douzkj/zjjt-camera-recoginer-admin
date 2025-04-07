package com.douzkj.zjjt.infra.hikvision.api.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collections;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ArtemisPageResponse<T> extends ArtemisResponse<ArtemisPageResponse.ArtemisPageData<T>> {

    @Data
    public static class ArtemisPageData<T> {
        private Integer total = 0;
        private Integer pageNo = 0;
        private Integer pageSize = 0;
        private List<T> list = Collections.emptyList();
    }


    public static <T> ArtemisPageResponse<T> empty() {
        return new ArtemisPageResponse<>();
    }

}
