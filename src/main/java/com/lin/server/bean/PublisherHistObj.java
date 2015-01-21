package com.lin.server.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Id;

public class PublisherHistObj implements Serializable{

	@Id private Long objectId;
	private long id;
	private String publisherName;
	private List<String> demandPartnerId;
	private List<String> order;
	private String companyId;
	private String pubisherAdserver;
	private String dfp_NetworkId;
	private String dfp_PublisherId;
	private String type;
	private String status;
	private String numberOfStations;
	private Date creationDate;
	private long createdByUserId;
    private Date lastModifiedDate;
    private long lastModifiedByUserId;
    private Date historyDate;
    private long updateDeleteByUserId;
	
    public PublisherHistObj(){
		
	}

	public PublisherHistObj(long id, String publisherName,
			List<String> demandPartnerId, List<String> order, String companyId,
			String pubisherAdserver, String dfp_NetworkId,
			String dfp_PublisherId, String type, String status,
			String numberOfStations, Date creationDate, long createdByUserId,
			Date lastModifiedDate, long lastModifiedByUserId, Date historyDate,
			long updateDeleteByUserId) {
		super();
		this.id = id;
		this.publisherName = publisherName;
		this.demandPartnerId = demandPartnerId;
		this.order = order;
		this.companyId = companyId;
		this.pubisherAdserver = pubisherAdserver;
		this.dfp_NetworkId = dfp_NetworkId;
		this.dfp_PublisherId = dfp_PublisherId;
		this.type = type;
		this.status = status;
		this.numberOfStations = numberOfStations;
		this.creationDate = creationDate;
		this.createdByUserId = createdByUserId;
		this.lastModifiedDate = lastModifiedDate;
		this.lastModifiedByUserId = lastModifiedByUserId;
		this.historyDate = historyDate;
		this.updateDeleteByUserId = updateDeleteByUserId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPublisherName() {
		return publisherName;
	}

	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}

	public List<String> getDemandPartnerId() {
		return demandPartnerId;
	}

	public void setDemandPartnerId(List<String> demandPartnerId) {
		this.demandPartnerId = demandPartnerId;
	}

	public List<String> getOrder() {
		return order;
	}

	public void setOrder(List<String> order) {
		this.order = order;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getPubisherAdserver() {
		return pubisherAdserver;
	}

	public void setPubisherAdserver(String pubisherAdserver) {
		this.pubisherAdserver = pubisherAdserver;
	}

	public String getDfp_NetworkId() {
		return dfp_NetworkId;
	}

	public void setDfp_NetworkId(String dfp_NetworkId) {
		this.dfp_NetworkId = dfp_NetworkId;
	}

	public String getDfp_PublisherId() {
		return dfp_PublisherId;
	}

	public void setDfp_PublisherId(String dfp_PublisherId) {
		this.dfp_PublisherId = dfp_PublisherId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNumberOfStations() {
		return numberOfStations;
	}

	public void setNumberOfStations(String numberOfStations) {
		this.numberOfStations = numberOfStations;
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

	public Date getHistoryDate() {
		return historyDate;
	}

	public void setHistoryDate(Date historyDate) {
		this.historyDate = historyDate;
	}

	public long getUpdateDeleteByUserId() {
		return updateDeleteByUserId;
	}

	public void setUpdateDeleteByUserId(long updateDeleteByUserId) {
		this.updateDeleteByUserId = updateDeleteByUserId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public Long getObjectId() {
		return objectId;
	}
	
	
	
}
