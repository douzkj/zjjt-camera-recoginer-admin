package com.douzkj.zjjt.repository.entity;

import lombok.Data;

import java.io.Serializable;

public class AlgoConfigs {


    @Data
    public static class AlgoLabelConfig implements Serializable {
        private Boolean enabled = false;
    }

}
