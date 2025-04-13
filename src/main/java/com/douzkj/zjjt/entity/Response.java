package com.douzkj.zjjt.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ranger dong
 * @date 20:49 2025/3/23
 * @descrption
 * @copyright Spotter.ink
 */
@Data
public class Response<T> implements Serializable {


    public static final int SUCCESS_CODE = 200;
    public static final int INTERVAL_ERRORCODE=500;

    private static final long serialVersionUID = 1L;

    private Integer code;

    private String msg;
    private T data;

    private boolean success;

    private Response(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.success = code == SUCCESS_CODE;
    }

    private Response(Integer code, String msg) {
        this(code, msg, null);
    }

    private Response() {
    }

    public static <T> Response<T> success() {
        return new Response<T>(SUCCESS_CODE, "成功", null);
    }

    public static <T> Response<T> success(T data) {
        return new Response<T>(SUCCESS_CODE, "成功", data);
    }


    public static <T> Response<T> fail(String msg) {
        return new Response<T>(INTERVAL_ERRORCODE, msg);
    }

    public static <T> Response<T> fail404(String msg) {
        return new Response<T>(404, msg);
    }


    public static <T> Response<T> fail(String msg, T data) {
        return new Response<T>(INTERVAL_ERRORCODE, msg, data);
    }


}
