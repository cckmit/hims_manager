package com.cmpay.lemon.monitor.config;

import com.cmpay.lemon.monitor.http.SendProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author: zhou_xiong
 */
@Configuration
public class RestConfig {
    @Bean
    public SendProcessor callbackProcessor(@Qualifier("loadBalancedRestTemplate")
                                                   RestTemplate loadBalancedRestTemplate,
                                           @Qualifier("httpRestTemplate")
                                                   RestTemplate httpRestTemplate) {
        return new SendProcessor(loadBalancedRestTemplate, httpRestTemplate);
    }

    @LoadBalanced
    @Bean
    public RestTemplate loadBalancedRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RestTemplate httpRestTemplate() {
        return new RestTemplate();
    }
}
