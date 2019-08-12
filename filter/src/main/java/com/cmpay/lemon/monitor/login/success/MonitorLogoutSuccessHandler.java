package com.cmpay.lemon.monitor.login.success;

import com.cmpay.lemon.framework.data.DefaultRspDTO;
import com.cmpay.lemon.framework.response.ResponseMessageResolver;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: zhou_xiong
 */
public class MonitorLogoutSuccessHandler implements LogoutSuccessHandler {
    private ResponseMessageResolver responseMessageResolver;

    public MonitorLogoutSuccessHandler(ResponseMessageResolver responseMessageResolver) {
        this.responseMessageResolver = responseMessageResolver;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_OK);
        this.responseMessageResolver.resolveResponse(request, response, DefaultRspDTO.newInstance(MsgEnum.SUCCESS));
    }
}
