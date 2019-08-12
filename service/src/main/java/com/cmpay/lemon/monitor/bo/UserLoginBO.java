package com.cmpay.lemon.monitor.bo;

/**
 * 描述
 *
 * @author : 曾益
 * @date : 2018/10/31
 */
public class UserLoginBO {
    /**
     * 用户ID
     */
    private Long userNo;
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
    private String mobileNo;
    /**
     * 短信验证码
     */
    private String messageCode;

    public Long getUserNo() {
        return userNo;
    }

    public void setUserNo(Long userNo) {
        this.userNo = userNo;
    }

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

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
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
        return "UserLoginBO{" +
                "userNo=" + userNo +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", captcha='" + captcha + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", messageCode='" + messageCode + '\'' +
                '}';
    }
}
