package com.douzkj.zjjt.infra.algo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Response<T> implements Serializable {

    private int code;

    private String message;

    private T data;


    public boolean isOK() {
        return code == 200;
    }
}
