/*
 * @ClassName PermiUserDO
 * @Description 
 * @version 1.0
 * @Date 2019-11-01 17:34:10
 */
package com.cmpay.lemon.monitor.entity;

import com.cmpay.framework.data.BaseDO;
import com.cmpay.lemon.framework.annotation.DataObject;
import java.time.LocalDateTime;

@DataObject
public class PermiUserDO extends BaseDO {
    /**
     * @Fields seqId 
     */
    private Long seqId;
    /**
     * @Fields userId 
     */
    private String userId;
    /**
     * @Fields userName 
     */
    private String userName;
    /**
     * @Fields password 
     */
    private String password;
    /**
     * @Fields deptName 
     */
    private String deptName;
    /**
     * @Fields deptId 
     */
    private String deptId;
    /**
     * @Fields createDate 
     */
    private LocalDateTime createDate;
    /**
     * @Fields updateDate 
     */
    private LocalDateTime updateDate;
    /**
     * @Fields mobileNum 
     */
    private String mobileNum;
    /**
     * @Fields registerIp 
     */
    private String registerIp;
    /**
     * @Fields registerTime 
     */
    private LocalDateTime registerTime;
    /**
     * @Fields email 
     */
    private String email;
    /**
     * @Fields loginDate 
     */
    private LocalDateTime loginDate;
    /**
     * @Fields loginIp 
     */
    private String loginIp;
    /**
     * @Fields lockedDate 
     */
    private LocalDateTime lockedDate;
    /**
     * @Fields isEnabled 
     */
    private Boolean isEnabled;
    /**
     * @Fields isLocked 
     */
    private Boolean isLocked;
    /**
     * @Fields loginCount 
     */
    private Integer loginCount;
    /**
     * @Fields loginFailureCount 
     */
    private Long loginFailureCount;
    /**
     * @Fields isDelFlag 
     */
    private Boolean isDelFlag;
    /**
     * @Fields emailPassword 
     */
    private String emailPassword;

    public Long getSeqId() {
        return seqId;
    }

    public void setSeqId(Long seqId) {
        this.seqId = seqId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public String getRegisterIp() {
        return registerIp;
    }

    public void setRegisterIp(String registerIp) {
        this.registerIp = registerIp;
    }

    public LocalDateTime getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(LocalDateTime registerTime) {
        this.registerTime = registerTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(LocalDateTime loginDate) {
        this.loginDate = loginDate;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public LocalDateTime getLockedDate() {
        return lockedDate;
    }

    public void setLockedDate(LocalDateTime lockedDate) {
        this.lockedDate = lockedDate;
    }

    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Boolean getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    public Long getLoginFailureCount() {
        return loginFailureCount;
    }

    public void setLoginFailureCount(Long loginFailureCount) {
        this.loginFailureCount = loginFailureCount;
    }

    public Boolean getIsDelFlag() {
        return isDelFlag;
    }

    public void setIsDelFlag(Boolean isDelFlag) {
        this.isDelFlag = isDelFlag;
    }

    public String getEmailPassword() {
        return emailPassword;
    }

    public void setEmailPassword(String emailPassword) {
        this.emailPassword = emailPassword;
    }
}