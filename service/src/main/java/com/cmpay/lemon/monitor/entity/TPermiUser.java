package com.cmpay.lemon.monitor.entity;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * TPermiUser entity. @author MyEclipse Persistence Tools
 */

public class TPermiUser extends AbstractDO{

	private static final long serialVersionUID = -6832604687036624724L;

	private Long seqId;
	private String userId;
	private String userName;
	private String password;
	private String deptName;
	private String deptId;
	private Date createDate;
	private Date updateDate;
	private String mobileNum;
	private String registerIp;
	private Date registerTime;
	private String email;
	private Date loginDate;
	private String loginIp;
	private Date lockedDate;
	private Boolean isEnabled;
	private Boolean isLocked;
	private Integer loginCount;
	private Integer loginFailureCount;
	private Boolean isDelFlag;


	/** default constructor */
	public TPermiUser() {
	}

	/** minimal constructor */
	public TPermiUser(String password) {
		this.password = password;
	}
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
			.append("SeqId",getSeqId())
			.append("UserId",getUserId())
			.append("UserName",getUserName())
			.toString();
	}
	/** full constructor */
	public TPermiUser(String userId, String userName, String password,
                      String deptName, String deptId, Date createDate,
                      Date updateDate, String mobileNum, String registerIp,
                      Date registerTime, String email, Date loginDate,
                      String loginIp, Date lockedDate, Boolean isEnabled,
                      Boolean isLocked, Integer loginCount, Integer loginFailureCount) {
		this.userId = userId;
		this.userName = userName;
		this.password = password;
		this.deptName = deptName;
		this.deptId = deptId;
		this.createDate = createDate;
		this.updateDate = updateDate;
		this.mobileNum = mobileNum;
		this.registerIp = registerIp;
		this.registerTime = registerTime;
		this.email = email;
		this.loginDate = loginDate;
		this.loginIp = loginIp;
		this.lockedDate = lockedDate;
		this.isEnabled = isEnabled;
		this.isLocked = isLocked;
		this.loginCount = loginCount;
		this.loginFailureCount = loginFailureCount;
	}

	// Property accessors

	public Long getSeqId() {
		return this.seqId;
	}

	public void setSeqId(Long seqId) {
		this.seqId = seqId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDeptName() {
		return this.deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDeptId() {
		return this.deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getMobileNum() {
		return this.mobileNum;
	}

	public void setMobileNum(String mobileNum) {
		this.mobileNum = mobileNum;
	}

	public String getRegisterIp() {
		return this.registerIp;
	}

	public void setRegisterIp(String registerIp) {
		this.registerIp = registerIp;
	}

	public Date getRegisterTime() {
		return this.registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getLoginDate() {
		return this.loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public String getLoginIp() {
		return this.loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public Date getLockedDate() {
		return this.lockedDate;
	}

	public void setLockedDate(Date lockedDate) {
		this.lockedDate = lockedDate;
	}

	public Boolean getIsEnabled() {
		return this.isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public Boolean getIsLocked() {
		return this.isLocked;
	}

	public void setIsLocked(Boolean isLocked) {
		this.isLocked = isLocked;
	}

	public Integer getLoginCount() {
		return this.loginCount;
	}

	public void setLoginCount(Integer loginCount) {
		this.loginCount = loginCount;
	}

	public Integer getLoginFailureCount() {
		return this.loginFailureCount;
	}

	public void setLoginFailureCount(Integer loginFailureCount) {
		this.loginFailureCount = loginFailureCount;
	}

	public Boolean getIsDelFlag() {
		return isDelFlag;
	}

	public void setIsDelFlag(Boolean isDelFlag) {
		this.isDelFlag = isDelFlag;
	}

	@Override
	public Serializable getId() {
		// TODO Auto-generated method stub
		return seqId;
	}

}