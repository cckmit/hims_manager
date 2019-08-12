package com.cmpay.lemon.monitor.login.auth;

import com.cmpay.lemon.framework.data.DefaultRspDTO;
import com.cmpay.lemon.framework.response.ResponseMessageResolver;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: zhou_xiong
 */
public class LoginSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {

    private ResponseMessageResolver responseMessageResolver;

    public LoginSessionInformationExpiredStrategy(ResponseMessageResolver responseMessageResolver) {
        this.responseMessageResolver = responseMessageResolver;
    }

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
        HttpServletResponse response = event.getResponse();
        HttpServletRequest request = event.getRequest();
        response.setStatus(HttpServletResponse.SC_OK);
        responseMessageResolver.resolve(request, response, DefaultRspDTO.newInstance(MsgEnum.LOGIN_SESSION_EXPIRE));
    }
}
