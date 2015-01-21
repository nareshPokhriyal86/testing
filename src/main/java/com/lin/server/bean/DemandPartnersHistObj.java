package com.lin.server.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Id;

@SuppressWarnings("serial")
public class DemandPartnersHistObj implements Serializable{
	
	@Id private Long objectId;
	private long id;
	private String demandPartnerName;
	private String dataSource;
	private String demandPartnerCategory;
	private List<String> Passback_Site_type;
	private String status;
	private String demandPartnerType;		// Direct, Indirect, House
	private Date creationDate;
	private long createdByUserId;
    private Date lastModifiedDate;
    private long lastModifiedByUserId;
    private Date historyDate;
    private long updateDeleteByUserId;
	
	public DemandPartnersHistObj() {
		
	}

	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getDemandPartnerName() {
		return demandPartnerName;
	}


	public void setDemandPartnerName(String demandPartnerName) {
		this.demandPartnerName = demandPartnerName;
	}


	public String getDataSource() {
		return dataSource;
	}


	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}


	public String getDemandPartnerType() {
		return demandPartnerType;
	}


	public void setDemandPartnerType(String demandPartnerType) {
		this.demandPartnerType = demandPartnerType;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getStatus() {
		return status;
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

	public void setPassback_Site_type(List<String> passback_Site_type) {
		Passback_Site_type = passback_Site_type;
	}

	public List<String> getPassback_Site_type() {
		return Passback_Site_type;
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

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setDemandPartnerCategory(String demandPartnerCategory) {
		this.demandPartnerCategory = demandPartnerCategory;
	}

	public String getDemandPartnerCategory() {
		return demandPartnerCategory;
	}
	

}
