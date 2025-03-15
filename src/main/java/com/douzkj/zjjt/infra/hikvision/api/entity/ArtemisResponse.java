package com.douzkj.zjjt.infra.hikvision.api.entity;


import lombok.Data;

@Data
public class ArtemisResponse<T> {

    /**
     * 返回码，0 – 成功，其他- 失败，参考[附录E.other.1 资源目录错误码]@[软件产品-综合安防管理平台-附录-附录E 返回码说明-返回码说明#附录E.other.1 资源目录错误码]
     */
    private String code;

    private String msg;

    private T data;


    public boolean isSuccess() {
        return code != null && code.equals("0");
    }
}
