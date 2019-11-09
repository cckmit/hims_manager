package com.cmpay.lemon.monitor.autoconfigure;

import com.cmpay.lemon.common.utils.OrderUtils;
import com.cmpay.lemon.framework.autoconfigure.security.SecurityProperties;
import com.cmpay.lemon.framework.data.InternalDataHelper;
import com.cmpay.lemon.framework.jackson.ObjectMapperObjectCodec;
import com.cmpay.lemon.framework.response.ResponseMessageResolver;
import com.cmpay.lemon.framework.security.auth.GenericAuthenticationFilter;
import com.cmpay.lemon.framework.security.callback.AuthenticationSuccessPostProcessor;
import com.cmpay.lemon.framework.security.callback.AuthenticationSuccessProcessorComposite;
import com.cmpay.lemon.monitor.login.auth.LoginAuthenticationProcessor;
import com.cmpay.lemon.monitor.login.auth.LoginSessionInformationExpiredStrategy;
import com.cmpay.lemon.monitor.login.auth.NoneLoginAuthenticationEntryPoint;
import com.cmpay.lemon.monitor.login.failure.LoginAuthenticationFailureHandler;
import com.cmpay.lemon.monitor.login.success.LoginAuthenticationSuccessHandler;
import com.cmpay.lemon.monitor.login.success.MonitorLogoutSuccessHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import java.util.List;
import java.util.Optional;

/**
 * @author: zhou_xiong
 */
@Configuration
public class LoginAutoConfiguration {
    private ResponseMessageResolver responseMessageResolver;
    private InternalDataHelper internalDataHelper;
    private AuthenticationSuccessProcessorComposite authenticationSuccessProcessorComposite;

    @Autowired
    public LoginAutoConfiguration(ObjectProvider<List<AuthenticationSuccessPostProcessor>> authenticationSuccessPostProcessors,
                                  ResponseMessageResolver responseMessageResolver,
                                  InternalDataHelper internalDataHelper) {
        this.internalDataHelper = internalDataHelper;
        this.responseMessageResolver = responseMessageResolver;
        this.authenticationSuccessProcessorComposite = new AuthenticationSuccessProcessorComposite();
        Optional.ofNullable(authenticationSuccessPostProcessors.getIfAvailable()).ifPresent(s ->
                OrderUtils.sortByOrder(s).stream().forEachOrdered(authenticationSuccessProcessorComposite::addAuthenticationSuccessPostProcessor));
    }

    @Bean
    public SessionInformationExpiredStrategy lemonSessionInformationExpiredStrategy() {
        return new LoginSessionInformationExpiredStrategy(responseMessageResolver);
    }

    @Bean(name = "lemonAuthenticationSuccessHandler")
    public AuthenticationSuccessHandler lemonAuthenticationSuccessHandler() {
        return new LoginAuthenticationSuccessHandler(this.responseMessageResolver, this.internalDataHelper, authenticationSuccessProcessorComposite);
    }

//    @Bean(name = "lemonAuthenticationFailureHandler")
//    public AuthenticationFailureHandler lemonAuthenticationFailureHandler() {
//        return new LoginAuthenticationFailureHandler(this.responseMessageResolver);
//    }

    @Bean
    public AuthenticationEntryPoint lemonAuthenticationEntryPoint() {
        return new NoneLoginAuthenticationEntryPoint(this.responseMessageResolver);
    }

    @Bean
    public LogoutSuccessHandler lemonLogoutSuccessHandler() {
        return new MonitorLogoutSuccessHandler(this.responseMessageResolver);
    }

    @EnableConfigurationProperties(SecurityProperties.class)
    @Configuration
    public static class AuthenticationProcessorConfiguration {
        private SecurityProperties properties;

        public AuthenticationProcessorConfiguration(SecurityProperties properties) {
            this.properties = properties;
        }

        @Bean
        public LoginAuthenticationProcessor loginAuthenticationProcessor(ObjectMapper objectMapper) {
            ObjectMapperObjectCodec omoc = new ObjectMapperObjectCodec(objectMapper);
            return new LoginAuthenticationProcessor(getPasswordPath(), omoc);
        }

        private String getPasswordPath() {
            return Optional.ofNullable(this.properties.getAuthentication())
                    .map(SecurityProperties.Authentication::getLoginPathPrefix)
                    .orElse(GenericAuthenticationFilter.DEFAULT_PREFIX_FILTER_PROCESSES_URL);
        }
    }
}
