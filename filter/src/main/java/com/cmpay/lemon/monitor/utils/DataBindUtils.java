package com.cmpay.lemon.monitor.utils;

import com.cmpay.lemon.common.exception.LemonException;
import com.cmpay.lemon.monitor.bo.UserLoginBO;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

/**
 * 描述
 *
 * @author : 曾益
 * @date : 2018/10/31
 */
public class DataBindUtils {
    public static UserLoginBO bindLoginData(HttpServletRequest request) {
        UserLoginBO userLoginBO = null;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            InputStream is = request.getInputStream();
            userLoginBO = objectMapper.readValue(is, UserLoginBO.class);
        } catch (IOException e) {
            throw LemonException.create(e);
        } catch (Exception e) {
            LemonException.throwLemonException(MsgEnum.LOGIN_DATA_BIND_FAILED, e.getMessage());
        }
        return userLoginBO;
    }
}
