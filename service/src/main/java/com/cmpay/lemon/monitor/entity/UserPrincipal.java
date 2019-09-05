/*



 */
package com.cmpay.lemon.monitor.entity;

import java.io.Serializable;

/**
 * 用户身份信息
 * @author
 */
public class UserPrincipal implements Serializable {
	private static final long serialVersionUID = -7205257269130267871L;

	private Long id;
	private String userId;
	private String userName;
	private String deptName;
	private String deptId;
	private String mobileNum;
	private String email;

	public UserPrincipal(Long id, String userId, String userName, String deptName, String deptId, String mobileNum, String email) {
		this.id = id;
		this.userId =  userId;
		this.userName = userName;
		this.deptId = deptId;
		this.deptName = deptName;
		this.mobileNum = mobileNum;
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public String getMobileNum() {
		return mobileNum;
	}

	public void setMobileNum(String mobileNum) {
		this.mobileNum = mobileNum;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return userName;
	}

}