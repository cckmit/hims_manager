package com.cmpay.lemon.monitor.login.success;

import com.cmpay.lemon.framework.data.DefaultRspDTO;
import com.cmpay.lemon.framework.data.InternalDataHelper;
import com.cmpay.lemon.framework.response.ResponseMessageResolver;
import com.cmpay.lemon.framework.security.AbstractAuthenticationSuccessHandler;
import com.cmpay.lemon.framework.security.LemonUser;
import com.cmpay.lemon.framework.security.callback.AuthenticationSuccessPostProcessor;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: zhou_xiong
 */
public class LoginAuthenticationSuccessHandler extends AbstractAuthenticationSuccessHandler {

    public LoginAuthenticationSuccessHandler(ResponseMessageResolver responseMessageResolver, InternalDataHelper internalDataHelper, AuthenticationSuccessPostProcessor authenticationSuccessPostProcessor) {
        super(responseMessageResolver, internalDataHelper, authenticationSuccessPostProcessor);
    }

    @Override
    protected DefaultRspDTO doCreateResponseDTO(HttpServletRequest request, Authentication authentication) {
        Object o = authentication.getPrincipal();
        if (o instanceof LemonUser) {
            LemonUser lemonUser = (LemonUser) o;
            if (lemonUser.isEnabled()) {
                LoginUserBase loginUserBase = new LoginUserBase();
                return DefaultRspDTO.newSuccessInstance(loginUserBase);
            } else {
                return DefaultRspDTO.newInstance(MsgEnum.LOGIN_FAILED);
            }
        }
        return DefaultRspDTO.newSuccessInstance(MsgEnum.LOGIN_FAILED);
    }

}
