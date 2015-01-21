package com.lin.server.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Id;

@SuppressWarnings("serial")
public class TeamPropertiesDetailsHistObj implements Serializable{
	
	@Id private Long objectId;
	private Long id;
	private String teamId;
	private String publisherId;
	private List<String> propertyId;
	private List<String> dfp_propertyName;
	private List<String> demandPartnersId;
	private Date historyDate;
    private long modifiedByUserId;
	
    public TeamPropertiesDetailsHistObj() {
		
	}

	public TeamPropertiesDetailsHistObj(String teamId,
			String publisherId, List<String> propertyId,
			List<String> dfp_propertyName, List<String> demandPartnersId) {
		this.teamId = teamId;
		this.publisherId = publisherId;
		this.propertyId = propertyId;
		this.dfp_propertyName = dfp_propertyName;
		this.demandPartnersId = demandPartnersId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getPublisherId() {
		return publisherId;
	}

	public void setPublisherId(String publisherId) {
		this.publisherId = publisherId;
	}

	public List<String> getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(List<String> propertyId) {
		this.propertyId = propertyId;
	}

	public List<String> getDfp_propertyName() {
		return dfp_propertyName;
	}

	public void setDfp_propertyName(List<String> dfp_propertyName) {
		this.dfp_propertyName = dfp_propertyName;
	}

	public List<String> getDemandPartnersId() {
		return demandPartnersId;
	}

	public void setDemandPartnersId(List<String> demandPartnersId) {
		this.demandPartnersId = demandPartnersId;
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

	
}
