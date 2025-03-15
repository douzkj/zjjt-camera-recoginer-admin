package com.douzkj.zjjt.infra.hikvision.api.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ArtemisPageResponse<T> extends ArtemisResponse<ArtemisPageResponse.ArtemisPageData<T>> {

    @Data
    public static class ArtemisPageData<T> {
        private Integer total;
        private Integer pageNo;
        private Integer pageSize;
        private List<T> list;
    }

}
