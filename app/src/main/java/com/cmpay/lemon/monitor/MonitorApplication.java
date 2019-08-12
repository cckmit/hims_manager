package com.cmpay.lemon.monitor;

import com.cmpay.lemon.common.LemonFramework;
import com.cmpay.lemon.framework.LemonBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;

/**
 * @author: zhou_xiong
 */
@LemonBootApplication(exclude = {RabbitAutoConfiguration.class},value = {"com.cmpay"})
public class MonitorApplication {
    public static void main(String[] args) {
        LemonFramework.run(MonitorApplication.class, args);
    }
}
