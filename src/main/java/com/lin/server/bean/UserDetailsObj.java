package com.lin.server.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity
@Index
public class UserDetailsObj implements Serializable, Comparable<UserDetailsObj>{

	@Id private long id;
	private String emailId;
	private String password;
	private String timezone;
	@Index private String userName;
	private String role;
	private String status;
	private List<String> teams ;
	private String reservedStatus;
	private Date userCreationDate;
	private long createdByUserId;
    private Date userModifiedDate;
    private long userModifiedUserId;
    private boolean deleted;
	private boolean optEmail;
	
	public UserDetailsObj(){
		
	}
	
	public UserDetailsObj(long id, String emailId, String password,
			String userName, String role, String status) {
		this.id = id;
		this.emailId = emailId;
		this.password = password;
		this.userName = userName;
		this.role = role;
		this.status = status;
	}
	
	

	public UserDetailsObj(long id, String emailId, String password,
			String timezone, String userName, String role, String status,
			List<String> teams, String reservedStatus, Date userCreationDate,
			long createdByUserId, Date userModifiedDate,
			long userModifiedUserId, boolean deleted, boolean optEmail) {
		this.id = id;
		this.emailId = emailId;
		this.password = password;
		this.timezone = timezone;
		this.userName = userName;
		this.role = role;
		this.status = status;
		this.teams = teams;
		this.reservedStatus = reservedStatus;
		this.userCreationDate = userCreationDate;
		this.createdByUserId = createdByUserId;
		this.userModifiedDate = userModifiedDate;
		this.userModifiedUserId = userModifiedUserId;
		this.deleted = deleted;
		this.optEmail = optEmail;
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

	public void setReservedStatus(String reservedStatus) {
		this.reservedStatus = reservedStatus;
	}

	public String getReservedStatus() {
		return reservedStatus;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTeams(List<String> teams) {
		this.teams = teams;
	}

	public List<String> getTeams() {
		return teams;
	}

	public void setCreatedByUserId(long createdByUserId) {
		this.createdByUserId = createdByUserId;
	}

	public long getCreatedByUserId() {
		return createdByUserId;
	}

	@Override
	public int compareTo(UserDetailsObj userDetailsObj) {
		return userName.compareToIgnoreCase(userDetailsObj.getUserName());
	}

	public boolean isOptEmail() {
		return optEmail;
	}

	public void setOptEmail(boolean optEmail) {
		this.optEmail = optEmail;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

@Override
public boolean equals(Object arg0) {
	// TODO Auto-generated method stub
	return super.equals(arg0);
}	
@Override
public int hashCode() {
	// TODO Auto-generated method stub
	return super.hashCode();
}		
}
