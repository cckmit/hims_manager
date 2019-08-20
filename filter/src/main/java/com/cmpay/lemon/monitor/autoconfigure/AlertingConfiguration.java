package com.cmpay.lemon.monitor.autoconfigure;

import com.cmpay.lemon.framework.alerting.AlertingResolver;
import com.cmpay.lemon.framework.alerting.ConfigurableAlerting;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

/**
 * Created on 14:53 2019/7/26
 *
 * @author wulan
 * <p>
 * description 错误码配置
 */
@Configuration
public class AlertingConfiguration {


    @Bean
    public AlertingResolver alertingResolver() {
        return new MsgCodeEnumAlertingResolver();
    }

    class MsgCodeEnumAlertingResolver implements AlertingResolver {

        @Override
        public ConfigurableAlerting resolve(ConfigurableAlerting configurableAlerting) {
            MsgEnum msg = MsgEnum.getEnum( configurableAlerting.getMsgCd() );
            Optional.ofNullable( msg ).ifPresent( a -> configurableAlerting.setMsgInfo( a.getMsgInfo() ) );
            return configurableAlerting;
        }
    }

}
