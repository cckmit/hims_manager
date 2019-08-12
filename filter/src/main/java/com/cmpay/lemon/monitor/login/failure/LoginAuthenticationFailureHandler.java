package com.cmpay.lemon.monitor.login.failure;

import com.cmpay.lemon.framework.data.DefaultRspDTO;
import com.cmpay.lemon.framework.response.ResponseMessageResolver;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: zhou_xiong
 */
public class LoginAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private ResponseMessageResolver responseMessageResolver;

    public LoginAuthenticationFailureHandler(ResponseMessageResolver responseMessageResolver) {
        this.responseMessageResolver = responseMessageResolver;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_OK);
        this.responseMessageResolver.resolve(request, response, DefaultRspDTO.newInstance(MsgEnum.LOGIN_ACCOUNT_OR_PASSWORD_ERROR));
    }
}
