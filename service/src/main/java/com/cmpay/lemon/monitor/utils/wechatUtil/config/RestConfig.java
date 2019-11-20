package com.cmpay.lemon.monitor.utils.wechatUtil.config;

import com.cmpay.lemon.monitor.utils.wechatUtil.SendProcessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author: zhou_xiong
 */
@Configuration
public class RestConfig {

    @Bean
    public RestTemplate httpRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public SendProcessor callbackProcessor(RestTemplate httpRestTemplate, ObjectMapper objectMapper) {
        return new SendProcessor(httpRestTemplate, objectMapper);
    }
}
