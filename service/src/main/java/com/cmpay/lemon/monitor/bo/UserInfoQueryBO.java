package com.cmpay.lemon.monitor.bo;

import java.time.LocalDateTime;

/**
 * 描述
 *
 * @author : 曾益
 * @date : 2018/11/9
 */
public class UserInfoQueryBO {
    /**
     * 用户名
     */
    private String userName;
    /**
     * 页码
     */
    private Integer pageNum;
    /**
     * 每页记录数
     */
    private Integer pageSize;
    /**
     * 用户ID
     */
    private Long userNo;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 状态
     */
    private Byte status;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * @Fields department 部门
     */
    private String department;
    /**
     * @Fields fullname 用户全名
     */
    private String fullname;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getUserNo() {
        return userNo;
    }

    public void setUserNo(Long userNo) {
        this.userNo = userNo;
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

    @Override
    public String toString() {
        return "UserInfoQueryBO{" +
                "userName='" + userName + '\'' +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", userNo=" + userNo +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", status='" + status + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
