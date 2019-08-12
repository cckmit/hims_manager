package com.cmpay.lemon.monitor.login.auth;

import com.cmpay.lemon.common.codec.CodecException;
import com.cmpay.lemon.common.codec.ObjectDecoder;
import com.cmpay.lemon.common.exception.ErrorMsgCode;
import com.cmpay.lemon.common.exception.LemonException;
import com.cmpay.lemon.common.utils.JudgeUtils;
import com.cmpay.lemon.framework.security.SimpleUserInfo;
import com.cmpay.lemon.framework.security.UserInfoBase;
import com.cmpay.lemon.framework.security.auth.AbstractGenericMatchableAuthenticationProcessor;
import com.cmpay.lemon.framework.security.auth.AuthenticationRequest;
import com.cmpay.lemon.framework.security.auth.GenericAuthenticationToken;
import com.cmpay.lemon.monitor.bo.UserLoginBO;
import com.cmpay.lemon.monitor.service.SystemLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;

import java.util.Map;

/**
 * @author: zhou_xiong
 */
public class LoginAuthenticationProcessor extends AbstractGenericMatchableAuthenticationProcessor<GenericAuthenticationToken> {
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    private ObjectDecoder objectDecoder;

    @Autowired
    private SystemLoginService systemLoginService;

    /**
     * @param filterProcessesUrl 认证Url, 前缀必须与GenericAuthenticationFilter拦截的Url前缀一致
     */
    public LoginAuthenticationProcessor(String filterProcessesUrl, ObjectDecoder objectDecoder) {
        super(filterProcessesUrl);
        this.objectDecoder = objectDecoder;
    }

    @Override
    protected UserInfoBase doProcessAuthentication(GenericAuthenticationToken genericAuthenticationToken) throws AuthenticationException {
        AuthenticationRequest authenticationRequest = genericAuthenticationToken.getAuthenticationRequest();
        Map<String, String> authenticationRequestParameters = null;
        try {
            authenticationRequestParameters = this.objectDecoder.readValue(getRequestInputStream(authenticationRequest), Map.class);
        } catch (CodecException e) {
            LemonException.throwLemonException(ErrorMsgCode.AUTHENTICATION_FAILURE, e);
        }
        if (JudgeUtils.isEmpty(authenticationRequestParameters)) {
            LemonException.throwLemonException(ErrorMsgCode.AUTHENTICATION_FAILURE, "No authentication parameter found in request body.");
        }
        UserLoginBO userLoginBO = new UserLoginBO();
        userLoginBO.setUsername(authenticationRequestParameters.get(USERNAME));
        userLoginBO.setPassword(authenticationRequestParameters.get(PASSWORD));
        userLoginBO = systemLoginService.login(userLoginBO);
        return new SimpleUserInfo(String.valueOf(userLoginBO.getUserNo()), userLoginBO.getUsername(), userLoginBO.getMobileNo());
    }
}


