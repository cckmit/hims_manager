package com.cmpay.lemon.monitor.dto;

import java.util.List;

/**
 * 描述
 *
 * @author : 曾益
 * @date : 2018/11/13
 */
public class UserDeleteReqDTO {
    private List<Long> userNos;

    public List<Long> getUserNos() {
        return userNos;
    }

    public void setUserNos(List<Long> userNos) {
        this.userNos = userNos;
    }

    @Override
    public String toString() {
        return "UserDeleteReqDTO{" +
                "userNos=" + userNos +
                '}';
    }
}
