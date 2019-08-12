package com.cmpay.lemon.monitor.search;

import org.apache.http.HttpHost;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.List;

/**
 * @author: zhou_xiong
 */
public class EsClientBuilder {
    private int connectTimeoutMillis = 5000;
    private int socketTimeoutMillis = 1800000;
    private int connectionRequestTimeoutMillis = 5000;
    private int maxConnectPerRoute = 10;
    private int maxConnectTotal = 30;
    private final List<HttpHost> httpHosts;

    private EsClientBuilder(List<HttpHost> httpHosts) {
        this.httpHosts = httpHosts;
    }

    public EsClientBuilder setConnectTimeoutMillis(int connectTimeoutMillis) {
        this.connectTimeoutMillis = connectTimeoutMillis;
        return this;
    }

    public EsClientBuilder setSocketTimeoutMillis(int socketTimeoutMillis) {
        this.socketTimeoutMillis = socketTimeoutMillis;
        return this;
    }

    public EsClientBuilder setConnectionRequestTimeoutMillis(int connectionRequestTimeoutMillis) {
        this.connectionRequestTimeoutMillis = connectionRequestTimeoutMillis;
        return this;
    }

    public EsClientBuilder setMaxConnectPerRoute(int maxConnectPerRoute) {
        this.maxConnectPerRoute = maxConnectPerRoute;
        return this;
    }

    public EsClientBuilder setMaxConnectTotal(int maxConnectTotal) {
        this.maxConnectTotal = maxConnectTotal;
        return this;
    }


    public static EsClientBuilder build(List<HttpHost> httpHosts) {
        return new EsClientBuilder(httpHosts);
    }

    public RestHighLevelClient create() {
        HttpHost[] httpHostArr = httpHosts.toArray(new HttpHost[0]);
        RestClientBuilder builder = RestClient.builder(httpHostArr).setMaxRetryTimeoutMillis(60 * 60 * 1000);

        builder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(connectTimeoutMillis);
            requestConfigBuilder.setSocketTimeout(socketTimeoutMillis);
            requestConfigBuilder.setConnectionRequestTimeout(connectionRequestTimeoutMillis);
            return requestConfigBuilder;
        });

        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.setMaxConnTotal(maxConnectTotal);
            httpClientBuilder.setMaxConnPerRoute(maxConnectPerRoute);
            httpClientBuilder.setDefaultIOReactorConfig(IOReactorConfig.custom()
                    .setIoThreadCount(Runtime.getRuntime().availableProcessors() * 2)
                    .build());
            return httpClientBuilder;
        });

        return new RestHighLevelClient(builder);
    }
}
