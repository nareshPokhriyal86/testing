package com.lin.web.dto;


public class LoginDetailsDTO {
	private String errorMessage;
	private String isAuthenticated;
	private long userId;
	private String emailId;
	private String password;
	private String roleId;
	private String roleName;
	private String userName;
	private String loginStatus;
	private boolean isAdminUser;
	private boolean isSuperAdmin;
	private boolean client;
	private boolean publisherPoolPartner;
	private String companyLogoURL;
	
	//Added by Anup
	private String companyName;
	
    public LoginDetailsDTO(){		
	}
	
	public String getErrorMessage(){
		return errorMessage;
	}
	public void setErrorMessage(String ErrorMessage){
		this.errorMessage = ErrorMessage;
	}
	public String getIsAuthenticated(){
		return isAuthenticated;
	}
	public void setIsAuthenticated(String IsAuthenticated){
		this.isAuthenticated=IsAuthenticated;
	}	
	public String getPassword(){
		return password;
	}	
	public void setPassword(String password) {
		this.password = password;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getUserId() {
		return userId;
	}

	public void setLoginStatus(String loginStatus) {
		this.loginStatus = loginStatus;
	}

	public String getLoginStatus() {
		return loginStatus;
	}

	public void setAdminUser(boolean isAdminUser) {
		this.isAdminUser = isAdminUser;
	}

	public boolean isAdminUser() {
		return isAdminUser;
	}

	public void setSuperAdmin(boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}

	public boolean isSuperAdmin() {
		return isSuperAdmin;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleName() {
		return roleName;
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

