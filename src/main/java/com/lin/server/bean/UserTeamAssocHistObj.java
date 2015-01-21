package com.lin.server.bean;

import java.io.Serializable;

import javax.persistence.Id;

public class UserTeamAssocHistObj implements Serializable{

	@Id	private Long objectId;
	private long id;
	private long userId;
	private String teamId;
	private String historyStatus;
	private long modifiedByUserId;
	
	public UserTeamAssocHistObj(){
		
	}



	public UserTeamAssocHistObj(long id, long userId, String teamId,
			String historyStatus, long modifiedByUserId) {
		super();
		this.id = id;
		this.userId = userId;
		this.teamId = teamId;
		this.historyStatus = historyStatus;
		this.modifiedByUserId = modifiedByUserId;
	}



	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public void setHistoryStatus(String historyStatus) {
		this.historyStatus = historyStatus;
	}

	public String getHistoryStatus() {
		return historyStatus;
	}

	public void setModifiedByUserId(long modifiedByUserId) {
		this.modifiedByUserId = modifiedByUserId;
	}

	public long getModifiedByUserId() {
		return modifiedByUserId;
	}
		
}
