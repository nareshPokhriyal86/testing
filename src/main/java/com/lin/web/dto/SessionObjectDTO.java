package com.lin.web.dto;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;


public class SessionObjectDTO implements Serializable{	
	private long userId;
	private String emailId;
	private String roleId;
	private String roleName;
	private String userName;
	private String pageName;
	private boolean adminUser;
	private boolean superAdmin;
	private boolean client;

	private String error;
	private String companyLogoURL;
	private boolean companyLogo;
	private boolean publisherPoolPartner;	// Use only if user in session is Admin user
	
	//Added by Anup
	private String companyName;
	
	public SessionObjectDTO(){
		
	}

	@Override
	public String toString() {
		return "SessionObjectDTO [userId=" + userId + ", emailId=" + emailId
				+ ", roleId=" + roleId + ", roleName=" + roleName
				+ ", userName=" + userName + ", pageName=" + pageName
				+ ", adminUser=" + adminUser + ", superAdmin=" + superAdmin
				+ ", client=" + client + ", error=" + error
				+ ", companyLogoURL=" + companyLogoURL + ", companyLogo="
				+ companyLogo + ", publisherPoolPartner="
				+ publisherPoolPartner + ", companyName=" + companyName + "]";
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public void setAdminUser(boolean adminUser) {
		this.adminUser = adminUser;
	}
	
	public boolean isAdminUser() {
		return adminUser;
	}

	public void setSuperAdmin(boolean superAdmin) {
		this.superAdmin = superAdmin;
	}

	public boolean isSuperAdmin() {
		return superAdmin;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setPublisherPoolPartner(boolean publisherPoolPartner) {
		this.publisherPoolPartner = publisherPoolPartner;
	}

	public boolean isPublisherPoolPartner() {
		return publisherPoolPartner;
	}

	public void setCompanyLogoURL(String companyLogoURL) {
		this.companyLogoURL = companyLogoURL;
	}

	public String getCompanyLogoURL() {
		return companyLogoURL;
	}

	public void setCompanyLogo(boolean companyLogo) {
		this.companyLogo = companyLogo;
	}

	public boolean isCompanyLogo() {
		return companyLogo;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	public boolean isClient() {
		return client;
	}

	public void setClient(boolean client) {
		this.client = client;
	}
	

}
