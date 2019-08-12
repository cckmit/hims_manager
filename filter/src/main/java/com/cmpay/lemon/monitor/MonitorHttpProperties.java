package com.cmpay.lemon.monitor;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: zhou_xiong
 */
@ConfigurationProperties("monitor.ui")
public class MonitorHttpProperties {
    private String httpCredentials;
    private String httpMethods;
    private String httpMaxAge;
    private String httpHeaders;

    public String getHttpCredentials() {
        return httpCredentials;
    }

    public void setHttpCredentials(String httpCredentials) {
        this.httpCredentials = httpCredentials;
    }

    public String getHttpMethods() {
        return httpMethods;
    }

    public void setHttpMethods(String httpMethods) {
        this.httpMethods = httpMethods;
    }

    public String getHttpMaxAge() {
        return httpMaxAge;
    }

    public void setHttpMaxAge(String httpMaxAge) {
        this.httpMaxAge = httpMaxAge;
    }

    public String getHttpHeaders() {
        return httpHeaders;
    }

    public void setHttpHeaders(String httpHeaders) {
        this.httpHeaders = httpHeaders;
    }
}
