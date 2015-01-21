package com.lin.server.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity
@SuppressWarnings("serial")
@Index
public class TeamPropertiesHistObj implements Serializable{
	
	@Id private Long objectId;
	private String id;
	private String teamStatus;
	private String teamName;
	private String teamDescription;
	private String companyId;
	private List<String> appViews;
	private String teamType;		// Built-In, Custom
	private List<String> agencyId;
	private List<String> advertiserId;
	private List<String> propertyId;
	private Date creationDate;
	private long createdByUserId;
    private Date lastModifiedDate;
    private long lastModifiedByUserId;
    private Date historyDate;
    private long modifiedByUserId;
	
    public TeamPropertiesHistObj() {
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTeamStatus() {
		return teamStatus;
	}

	public void setTeamStatus(String teamStatus) {
		this.teamStatus = teamStatus;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getTeamDescription() {
		return teamDescription;
	}

	public void setTeamDescription(String teamDescription) {
		this.teamDescription = teamDescription;
	}

	public List<String> getAppViews() {
		return appViews;
	}

	public void setAppViews(List<String> appViews) {
		this.appViews = appViews;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public long getCreatedByUserId() {
		return createdByUserId;
	}

	public void setCreatedByUserId(long createdByUserId) {
		this.createdByUserId = createdByUserId;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public long getLastModifiedByUserId() {
		return lastModifiedByUserId;
	}

	public void setLastModifiedByUserId(long lastModifiedByUserId) {
		this.lastModifiedByUserId = lastModifiedByUserId;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}
	public void setHistoryDate(Date historyDate) {
		this.historyDate = historyDate;
	}
	public Date getHistoryDate() {
		return historyDate;
	}
	public void setModifiedByUserId(long modifiedByUserId) {
		this.modifiedByUserId = modifiedByUserId;
	}
	public long getModifiedByUserId() {
		return modifiedByUserId;
	}

	public void setAgencyId(List<String> agencyId) {
		this.agencyId = agencyId;
	}

	public List<String> getAgencyId() {
		return agencyId;
	}

	public void setAdvertiserId(List<String> advertiserId) {
		this.advertiserId = advertiserId;
	}

	public List<String> getAdvertiserId() {
		return advertiserId;
	}

	public void setTeamType(String teamType) {
		this.teamType = teamType;
	}

	public String getTeamType() {
		return teamType;
	}

	public void setPropertyId(List<String> propertyId) {
		this.propertyId = propertyId;
	}

	public List<String> getPropertyId() {
		return propertyId;
	}
	
	
}
