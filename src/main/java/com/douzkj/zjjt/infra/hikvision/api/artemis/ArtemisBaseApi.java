package com.douzkj.zjjt.infra.hikvision.api.artemis;

import cn.hutool.json.JSONUtil;
import com.douzkj.zjjt.infra.hikvision.config.ArtemisConfigProps;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ArtemisBaseApi {

    /**
     * STEP1：设置平台参数，根据实际情况,设置host appkey appsecret 三个参数.
     * <p>
     * ip:port : 平台门户/nginx的IP和端口（必须使用https协议，https端口默认为443）
     * appKey : 请填入appKey
     * appSecret : 请填入appSecret
     */
    private final ArtemisConfig artemisConfig;

    private final ArtemisConfigProps artemisConfigProps;


    public ArtemisBaseApi(ArtemisConfigProps artemisConfigProps) {
        this.artemisConfigProps = artemisConfigProps;
        this.artemisConfig = new ArtemisConfig();
        this.artemisConfig.setHost(artemisConfigProps.getHost());
        this.artemisConfig.setAppKey(artemisConfigProps.getAppKey());
        this.artemisConfig.setAppSecret(artemisConfigProps.getAppSecret());
    }

    public String doPostJson(String requestRelativePath, Object request) throws Exception {
        return doPostJsonWithHeaders(requestRelativePath, request, null);
    }

    protected String doPostJsonWithHeaders(String requestRelativePath, Object request, Map<String, String> headers) throws Exception {
        String requestPath = artemisConfigProps.getPath() + requestRelativePath;
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put(artemisConfigProps.getSchema() + "://", requestPath);
            }
        };
        String body = JSONUtil.toJsonStr(request);
        return ArtemisHttpUtil.doPostStringArtemis(artemisConfig, path, body, null, null, "application/json", headers);

    }

}