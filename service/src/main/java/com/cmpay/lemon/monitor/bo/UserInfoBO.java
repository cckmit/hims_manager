package com.cmpay.lemon.monitor.bo;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 描述
 *
 * @author : 曾益
 * @date : 2018/11/12
 */
public class UserInfoBO {
    private Long userNo;
    private String username;
    private String password;
    private String salt;
    private String email;
    private String mobile;
    private Byte status;
    /**
     * @Fields department 部门
     */
    private String department;
    /**
     * @Fields fullname 用户全名
     */
    private String fullname;
    /**
     * @Fields createUserNo 创建者ID
     */
    private Long createUserNo;
    /**
     * @Fields createTime 创建时间
     */
    private LocalDateTime createTime;

//    private List<Long> roleIds;
//
//    public List<Long> getRoleIds() {
//        return roleIds;
//    }
//
//    public void setRoleIds(List<Long> roleIds) {
//        this.roleIds = roleIds;
//    }

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
        return "UserInfoBO{" +
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
