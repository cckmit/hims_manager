package com.cmpay.lemon.monitor.autoconfigure;

import com.cmpay.framework.context.initializer.CmpayAggregatedDataInstantiator;
import com.cmpay.lemon.framework.data.instantiator.AggregatedDataInstantiator;
import com.cmpay.lemon.framework.web.filter.TradeEntryPointFilter;
import com.cmpay.lemon.monitor.MonitorHttpProperties;
import com.cmpay.lemon.monitor.filter.MonitorTradeEntryPointFilter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: zhou_xiong
 */
@Configuration
@EnableConfigurationProperties(MonitorHttpProperties.class)
public class WebMvcConfiguration {
    @Bean
    public TradeEntryPointFilter tradeEntryPointFilter(MonitorHttpProperties properties) {
        return new MonitorTradeEntryPointFilter(properties, null);
    }

    @Bean
    public AggregatedDataInstantiator aggregatedDataInstantiator() {
        return new CmpayAggregatedDataInstantiator();
    }

}
