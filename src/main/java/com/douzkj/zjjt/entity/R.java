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
public class R<T> implements Serializable {


    public static final int SUCCESS_CODE = 200;
    public static final int INTERVAL_ERRORCODE=500;

    private static final long serialVersionUID = 1L;

    private Integer code;

    private String msg;
    private T data;

    private boolean success;

    private R(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.success = code == SUCCESS_CODE;
    }

    private R(Integer code, String msg) {
        this(code, msg, null);
    }

    private R() {
    }

    public static <T> R<T> success() {
        return new R<T>(SUCCESS_CODE, "成功", null);
    }

    public static <T> R<T> success(T data) {
        return new R<T>(SUCCESS_CODE, "成功", data);
    }


    public static <T> R<T> fail(String msg) {
        return new R<T>(INTERVAL_ERRORCODE, msg);
    }

    public static <T> R<T> fail404(String msg) {
        return new R<T>(404, msg);
    }


    public static <T> R<T> fail(String msg, T data) {
        return new R<T>(INTERVAL_ERRORCODE, msg, data);
    }


}
