package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.response.PageableRspDTO;

/**
 * 描述
 *
 * @author : 曾益
 * @date : 2018/11/9
 */
public class UserInfoQueryReqDTO extends PageableRspDTO {
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "UserInfoQueryReqDTO{" +
                "userName='" + userName + '\'' +
                '}';
    }
}
