package com.cmpay.lemon.monitor.login.auth;

import com.cmpay.lemon.framework.data.DefaultRspDTO;
import com.cmpay.lemon.framework.response.ResponseMessageResolver;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: zhou_xiong
 */
public class NoneLoginAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private ResponseMessageResolver responseMessageResolver;

    public NoneLoginAuthenticationEntryPoint(ResponseMessageResolver responseMessageResolver) {
        this.responseMessageResolver = responseMessageResolver;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        if (!RequestMethod.OPTIONS.name().equalsIgnoreCase(request.getMethod().toUpperCase())) {
            expiredResponse(request, response);
        }
    }

    private void expiredResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        this.responseMessageResolver.resolveResponse(request, response, new DefaultRspDTO(MsgEnum.LOGIN_SESSION_EXPIRE));
    }
}
