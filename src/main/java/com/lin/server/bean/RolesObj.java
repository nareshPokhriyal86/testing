package com.lin.server.bean;

import java.io.Serializable;

import javax.persistence.Id;

public class RolesObj implements Serializable{

	@Id	private String id;
	private String roleName;
	
	public RolesObj(){
		
	}
	
	public RolesObj(String id, String roleName) {
		super();
		this.id = id;
		this.roleName = roleName;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getRoleName() {
		return roleName;
	}


	
		
}
