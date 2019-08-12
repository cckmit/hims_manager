package com.cmpay.lemon.monitor.bo;

import java.time.LocalDateTime;

/**
 * 描述
 *
 * @author : 曾益
 * @date : 2018/11/12
 */
public class UserInfoBO {
    /**
     * @Fields userNo
     */
    private Long userNo;
    /**
     * @Fields username 用户名
     */
    private String username;
    /**
     * @Fields password 密码
     */
    private String password;
    /**
     * @Fields salt 盐
     */
    private String salt;
    /**
     * @Fields email 邮箱
     */
    private String email;
    /**
     * @Fields mobile 手机号
     */
    private String mobile;
    /**
     * @Fields status 状态  0：禁用   1：正常
     */
    private Byte status;
    /**
     * @Fields createUserId 创建者ID
     */
    private Long createUserId;
    /**
     * @Fields createTime 创建时间
     */
    private LocalDateTime createTime;

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

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "UserInfoBO{" +
                "userNo=" + userNo +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", status=" + status +
                ", createUserId=" + createUserId +
                ", createTime=" + createTime +
                '}';
    }
}
