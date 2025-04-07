package com.douzkj.zjjt.infra.hikvision.config;

import com.douzkj.zjjt.infra.hikvision.api.video.funcional.ArtemisVideoFunctionApi;
import com.douzkj.zjjt.infra.hikvision.api.video.resource.ArtemisResourceApi;
import com.douzkj.zjjt.infra.hikvision.api.video.resource.ArtemisVideoResourceApi;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class HikvisionConfiguration {



    @Bean
    public ArtemisVideoResourceApi artemisVideoResourceApi(ArtemisConfigProps artemisConfigProps) {
        return new ArtemisVideoResourceApi(artemisConfigProps);
    }

    @Bean
    public ArtemisVideoFunctionApi artemisVideoFunctionApi(ArtemisConfigProps artemisConfigProps) {
        return new ArtemisVideoFunctionApi(artemisConfigProps);
    }


    @Bean
    public ArtemisResourceApi artemisResourceApi(ArtemisConfigProps artemisConfigProps) {
        return new ArtemisResourceApi(artemisConfigProps);
    }
}
