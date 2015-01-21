package com.lin.web.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;

public class RolesAndAuthorisationDTO implements Serializable{

	@Id	private Long id;
	private String roleName;
	private String roleType;			// Built-In or Custom Role
	private String roleStatus;
	private String roleDescription;
	private String companyName;
	
	
	public RolesAndAuthorisationDTO(){
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleStatus() {
		return roleStatus;
	}

	public void setRoleStatus(String roleStatus) {
		this.roleStatus = roleStatus;
	}


	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}

	public String getRoleDescription() {
		return roleDescription;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyName() {
		return companyName;
	}
	
	
		
}
