package com.lin.server.bean;

import java.io.Serializable;

import javax.persistence.Id;

public class UserTeamAssocObj implements Serializable{

	@Id	private Long id;
	private long userId;
	private String teamName;
	
	public UserTeamAssocObj(){
		
	}

	public UserTeamAssocObj(long userId, String teamName) {
		this.userId = userId;
		this.teamName = teamName;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getTeamName() {
		return teamName;
	}



	
		
}
