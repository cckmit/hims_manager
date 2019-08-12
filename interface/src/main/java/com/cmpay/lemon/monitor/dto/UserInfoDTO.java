package com.cmpay.lemon.monitor.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 描述
 *
 * @author : 曾益
 * @date : 2018/11/9
 */
public class UserInfoDTO {
    private Long userNo;
    private String username;
    private String salt;
    private String email;
    private String mobile;
    private Byte status;
    private LocalDateTime createTime;
    private List<Long> roleIds;

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

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }

    @Override
    public String toString() {
        return "UserInfoDTO{" +
                "userNo=" + userNo +
                ", username='" + username + '\'' +
                ", salt='" + salt + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", roleIds=" + roleIds +
                '}';
    }
}
