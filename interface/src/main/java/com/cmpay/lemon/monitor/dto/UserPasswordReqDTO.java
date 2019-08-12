package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.request.GenericDTO;

/**
 * 描述
 *
 * @author : 曾益
 * @date : 2018/11/13
 */
public class UserPasswordReqDTO extends GenericDTO {
    private String password;
    private String newPassword;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public String toString() {
        return "UserPasswordReqDTO{" +
                "password='" + password + '\'' +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }
}
