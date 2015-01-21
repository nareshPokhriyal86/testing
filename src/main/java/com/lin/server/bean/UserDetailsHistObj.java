package com.lin.server.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;



import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity
@Index
public class UserDetailsHistObj implements Serializable{

	@Id private Long objectId;
	private long id;
	private String emailId;
	private String password;
	private String userName;
	private String role;
	private String status;
	private List<String> teams ;
	private Date userCreationDate;
    private Date userModifiedDate;
    private long userModifiedUserId;
    private Date historyDate;
    private long updateDeleteByUserId;
    private String historyStatus;
	
	public UserDetailsHistObj(){
		
	}

	public UserDetailsHistObj(long id, String emailId, String password,
			String userName, String role, String status, Date userCreationDate,
			Date userModifiedDate, long userModifiedUserId, Date historyDate,
			long updateDeleteByUserId, String historyStatus) {
		super();
		this.id = id;
		this.emailId = emailId;
		this.password = password;
		this.userName = userName;
		this.role = role;
		this.status = status;
		this.userCreationDate = userCreationDate;
		this.userModifiedDate = userModifiedDate;
		this.userModifiedUserId = userModifiedUserId;
		this.historyDate = historyDate;
		this.updateDeleteByUserId = updateDeleteByUserId;
		this.historyStatus = historyStatus;
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setUserCreationDate(Date userCreationDate) {
		this.userCreationDate = userCreationDate;
	}

	public Date getUserCreationDate() {
		return userCreationDate;
	}

	public void setUserModifiedDate(Date userModifiedDate) {
		this.userModifiedDate = userModifiedDate;
	}

	public Date getUserModifiedDate() {
		return userModifiedDate;
	}

	public void setUserModifiedUserId(long userModifiedUserId) {
		this.userModifiedUserId = userModifiedUserId;
	}

	public long getUserModifiedUserId() {
		return userModifiedUserId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public Long getObjectId() {
		return objectId;
	}



	public void setHistoryDate(Date historyDate) {
		this.historyDate = historyDate;
	}



	public Date getHistoryDate() {
		return historyDate;
	}



	public void setUpdateDeleteByUserId(long updateDeleteByUserId) {
		this.updateDeleteByUserId = updateDeleteByUserId;
	}



	public long getUpdateDeleteByUserId() {
		return updateDeleteByUserId;
	}



	public void setHistoryStatus(String historyStatus) {
		this.historyStatus = historyStatus;
	}



	public String getHistoryStatus() {
		return historyStatus;
	}

	public void setTeams(List<String> teams) {
		this.teams = teams;
	}

	public List<String> getTeams() {
		return teams;
	}

	
		
}
