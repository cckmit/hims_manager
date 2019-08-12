package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.request.GenericDTO;
import io.swagger.annotations.ApiModel;

/**
 * 登录请求数据
 * @author : 曾益
 * @date : 2018/10/31
 */
@ApiModel(value = "UserLoginReqDTO", description = "登录请求数据")
public class UserLoginReqDTO extends GenericDTO {
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 图形验证码
     */
    private String captcha;
    /**
     * 手机号
     */
    private Integer mobileNo;
    /**
     * 短信验证码
     */
    private String messageCode;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public Integer getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(Integer mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    @Override
    public String toString() {
        return "UserLoginReqDTO{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", captcha='" + captcha + '\'' +
                ", mobileNo=" + mobileNo +
                ", messageCode='" + messageCode + '\'' +
                '}';
    }
}
