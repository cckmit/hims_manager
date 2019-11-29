package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.request.GenericDTO;

public class ErcdmgPordUserDTO  extends GenericDTO {

	private String prodUserId;
	private String prodUserName;
	private String email;
	private String emailPassword;
	private String audiUserId;
	private String audiUserName;
	

	public String getProdUserId() {
		return prodUserId;
	}


	public void setProdUserId(String prodUserId) {
		this.prodUserId = prodUserId;
	}


	public String getProdUserName() {
		return prodUserName;
	}


	public void setProdUserName(String prodUserName) {
		this.prodUserName = prodUserName;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getEmailPassword() {
		return emailPassword;
	}


	public void setEmailPassword(String emailPassword) {
		this.emailPassword = emailPassword;
	}


	public String getAudiUserId() {
		return audiUserId;
	}


	public void setAudiUserId(String audiUserId) {
		this.audiUserId = audiUserId;
	}


	public String getAudiUserName() {
		return audiUserName;
	}


	public void setAudiUserName(String audiUserName) {
		this.audiUserName = audiUserName;
	}

	@Override
	public String toString() {
		return "ErcdmgPordUserBO{" +
				"prodUserId='" + prodUserId + '\'' +
				", prodUserName='" + prodUserName + '\'' +
				", email='" + email + '\'' +
				", emailPassword='" + emailPassword + '\'' +
				", audiUserId='" + audiUserId + '\'' +
				", audiUserName='" + audiUserName + '\'' +
				'}';
	}
}
