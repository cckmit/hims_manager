package com.cmpay.lemon.monitor.config;

import com.cmpay.lemon.common.utils.StringUtils;
import com.cmpay.lemon.monitor.search.EsClientBuilder;
import com.cmpay.lemon.monitor.search.EsProperties;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhou_xiong
 */
@Configuration
@EnableConfigurationProperties(EsProperties.class)
public class EsConfig {

    @Bean(destroyMethod = "close")
    public RestHighLevelClient getRestHighLevelClient(EsProperties properties) {
        List<HttpHost> httpHosts = new ArrayList<>();
        for (String node : properties.getNodes()) {
            try {
                String[] parts = StringUtils.split(node, ":");
                Assert.notNull(parts, "Must defined");
                Assert.state(parts.length == 2, "Must be defined as 'host:port'");
                httpHosts.add(new HttpHost(parts[0], Integer.parseInt(parts[1]), properties.getSchema()));
            } catch (RuntimeException ex) {
                throw new IllegalStateException("Invalid ES nodes " + "property '" + node + "'", ex);
            }
        }
        return EsClientBuilder.build(httpHosts)
                .setConnectionRequestTimeoutMillis(properties.getConnectionRequestTimeoutMillis())
                .setConnectTimeoutMillis(properties.getConnectTimeoutMillis())
                .setSocketTimeoutMillis(properties.getSocketTimeoutMillis())
                .setMaxConnectTotal(properties.getMaxConnectTotal())
                .setMaxConnectPerRoute(properties.getMaxConnectPerRoute())
                .create();
    }
}
