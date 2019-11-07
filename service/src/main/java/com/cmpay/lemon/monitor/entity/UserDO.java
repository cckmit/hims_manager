/*
 * @ClassName UserDO
 * @Description 
 * @version 1.0
 * @Date 2019-11-07 09:40:56
 */
package com.cmpay.lemon.monitor.entity;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;
import java.time.LocalDateTime;

@DataObject
public class UserDO extends BaseDO {
    /**
     * @Fields userNo 用户ID
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
     * @Fields department 部门
     */
    private String department;
    /**
     * @Fields fullname 用户全名
     */
    private String fullname;
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
     * @Fields createUserNo 创建者ID
     */
    private Long createUserNo;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
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

    public Long getCreateUserNo() {
        return createUserNo;
    }

    public void setCreateUserNo(Long createUserNo) {
        this.createUserNo = createUserNo;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "UserDO{" +
                "userNo=" + userNo +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", department='" + department + '\'' +
                ", fullname='" + fullname + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", status=" + status +
                ", createUserNo=" + createUserNo +
                ", createTime=" + createTime +
                '}';
    }
}