package com.lin.server.bean;

import java.io.Serializable;

import javax.persistence.Id;

public class TeamsObj implements Serializable{

	@Id	private String id;
	private String teamName;
	
	public TeamsObj(){
		
	}
	
	public TeamsObj(String id, String teamName) {
		super();
		this.id = id;
		this.teamName = teamName;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public String getTeamName() {
		return teamName;
	}


	
		
}
