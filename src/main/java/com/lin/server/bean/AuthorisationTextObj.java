package com.lin.server.bean;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity
@Index
public class AuthorisationTextObj implements Serializable{

	@Id	private Long id;
	private String rolesAndAuthorisationColumnName;
	private String authorisationText;
	private String authorisationTextKeyword;
	@Index private String authorisationForPage;
	private String authorisationTextStatus;
	private String permission;
	
	
	public AuthorisationTextObj() {
	}
	
	public AuthorisationTextObj(String RolesAndAuthorisationColumnName, String AuthorisationText, String AuthorisationTextKeyword, String AuthorisationForPage, String AuthorisationTextStatus) {
		rolesAndAuthorisationColumnName = RolesAndAuthorisationColumnName;
		authorisationText = AuthorisationText;
		authorisationTextStatus = AuthorisationTextStatus;
		authorisationTextKeyword = AuthorisationTextKeyword;
		authorisationForPage = AuthorisationForPage;
	}

	public String getRolesAndAuthorisationColumnName() {
		return rolesAndAuthorisationColumnName;
	}

	public void setRolesAndAuthorisationColumnName(
			String rolesAndAuthorisationColumnName) {
		this.rolesAndAuthorisationColumnName = rolesAndAuthorisationColumnName;
	}

	public String getAuthorisationText() {
		return authorisationText;
	}

	public void setAuthorisationText(String authorisationText) {
		this.authorisationText = authorisationText;
	}

	public String getAuthorisationTextStatus() {
		return authorisationTextStatus;
	}

	public void setAuthorisationTextStatus(String authorisationTextStatus) {
		this.authorisationTextStatus = authorisationTextStatus;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getPermission() {
		return permission;
	}

	public void setAuthorisationTextKeyword(String authorisationTextKeyword) {
		this.authorisationTextKeyword = authorisationTextKeyword;
	}

	public String getAuthorisationTextKeyword() {
		return authorisationTextKeyword;
	}

	public void setAuthorisationForPage(String authorisationForPage) {
		this.authorisationForPage = authorisationForPage;
	}

	public String getAuthorisationForPage() {
		return authorisationForPage;
	}

	
		
	
}
