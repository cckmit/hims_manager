package com.cmpay.lemon.monitor;

import com.cmpay.lemon.common.LemonFramework;
import com.cmpay.lemon.framework.LemonBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author: zhou_xiong
 */
@LemonBootApplication(exclude = {RabbitAutoConfiguration.class},value = {"com.cmpay"})
@EnableAsync
public class HimsManagerApplication {
    public static void main(String[] args) {
        LemonFramework.run(HimsManagerApplication.class, args);
    }
}
