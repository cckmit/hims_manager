package com.cmpay.lemon.monitor.filter;

import com.cmpay.lemon.framework.response.FailureHandlerResponseFlusher;
import com.cmpay.lemon.framework.web.filter.TradeEntryPointFilter;
import com.cmpay.lemon.monitor.MonitorHttpProperties;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.*;


/**
 * @author: zhou_xiong
 */
public class MonitorTradeEntryPointFilter extends TradeEntryPointFilter {

    private MonitorHttpProperties properties;

    public MonitorTradeEntryPointFilter(MonitorHttpProperties monitorHttpProperties, FailureHandlerResponseFlusher failureHandlerResponseFlusher) {
        super(failureHandlerResponseFlusher);
        this.properties = monitorHttpProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, request.getHeader(ORIGIN));
        response.setHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS, properties.getHttpCredentials());
        response.setHeader(ACCESS_CONTROL_ALLOW_METHODS, properties.getHttpMethods());
        response.setHeader(ACCESS_CONTROL_MAX_AGE, properties.getHttpMaxAge());
        response.setHeader(ACCESS_CONTROL_ALLOW_HEADERS, properties.getHttpHeaders());



        super.doFilterInternal(request, response, filterChain);
    }
}
