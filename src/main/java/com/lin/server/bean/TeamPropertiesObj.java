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
public class TeamPropertiesObj implements Serializable, Comparable<TeamPropertiesObj>{
	
	@Id	private String id;
	private String teamStatus;
	@Index private String teamName;
	private String teamDescription;
	private String companyId;		// PublisherPoolPartner_ID, DemandPartner_ID, Client_ID
	private List<String> appViews;
	private String teamType;		// Built-In, Custom
	private List<String> agencyId;
	private List<String> advertiserId;
	private List<String> propertyId;
	private Date creationDate;
	private long createdByUserId;
    private Date lastModifiedDate;
    private long lastModifiedByUserId;
	
    public TeamPropertiesObj() {
		
	}
    
    

	@Override
	public String toString() {
		return "TeamPropertiesObj [id=" + id + ", teamStatus=" + teamStatus
				+ ", teamName=" + teamName + ", teamDescription="
				+ teamDescription + ", companyId=" + companyId + ", appViews="
				+ appViews + ", teamType=" + teamType + ", agencyId="
				+ agencyId + ", advertiserId=" + advertiserId + ", propertyId="
				+ propertyId + ", creationDate=" + creationDate
				+ ", createdByUserId=" + createdByUserId
				+ ", lastModifiedDate=" + lastModifiedDate
				+ ", lastModifiedByUserId=" + lastModifiedByUserId + "]";
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

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setTeamType(String teamType) {
		this.teamType = teamType;
	}

	public String getTeamType() {
		return teamType;
	}

	@Override
	public int compareTo(TeamPropertiesObj teamPropertiesObj) {
		return teamName.compareToIgnoreCase(teamPropertiesObj.getTeamName());
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

	public void setPropertyId(List<String> propertyId) {
		this.propertyId = propertyId;
	}

	public List<String> getPropertyId() {
		return propertyId;
	}

	
}
