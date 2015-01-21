package com.lin.web.dto;

import java.io.Serializable;


public class TeamSetupDTO implements Serializable {

	private String teamName;
	private String teamStatus;
	
	
	public TeamSetupDTO(){
		
	}	
	
	public TeamSetupDTO(String teamName, String teamStatus) {
		this.teamName = teamName;
		this.teamStatus = teamStatus;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public void setTeamStatus(String teamStatus) {
		this.teamStatus = teamStatus;
	}

	public String getTeamStatus() {
		return teamStatus;
	}
	
	
}
