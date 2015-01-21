package com.lin.server.bean;

import java.io.Serializable;

import javax.persistence.Id;

/*
 * @author Youdhveer Panwar
 * This entity is used in Media Planner to create Insertion order account manger and executive person details 
 * under an IO in datastore
 */

@SuppressWarnings("serial")
public class IOAccountPersonObj implements Serializable {
	@Id	private Long id;	
	private String accountpersonName;
	private String role;
	private String phone;
	private String fax;
	private String email;
	
	
	public IOAccountPersonObj(){
	}


	public IOAccountPersonObj(String accountpersonName,String role,
			String phone, String fax, String email) {		
		this.accountpersonName = accountpersonName;
		this.role=role;
		this.phone = phone;
		this.fax = fax;
		this.email = email;
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("IOAccountPersonObj [id=").append(id)
				.append(", accountpersonName=").append(accountpersonName)
				.append(", role=").append(role)
				.append(", phone=").append(phone).append(", fax=").append(fax)
				.append(", email=").append(email).append("]");
		return builder.toString();
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}



	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getFax() {
		return fax;
	}


	public void setFax(String fax) {
		this.fax = fax;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}

	public void setRole(String role) {
		this.role = role;
	}


	public String getRole() {
		return role;
	}

	public void setAccountpersonName(String accountpersonName) {
		this.accountpersonName = accountpersonName;
	}


	public String getAccountpersonName() {
		return accountpersonName;
	}
	
	
	
}
