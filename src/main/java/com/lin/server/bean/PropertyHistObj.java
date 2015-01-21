package com.lin.server.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Embedded;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity
@Index
public class PropertyHistObj implements Serializable {

	@Id	private Long objectId;
	private String id;
	private String propertyName;
	private String publisherId;
	private String market;
	private String DMARank;
	private String DFPPropertyName;
	private String DFPPropertyId;
	@Embedded private List<PropertyChildObj> childs;
	private String adserverId;
	private String adServerUserName;
	private String companyId;
	private String affiliation;
	private String webSite;
	private String mobileWebURL;
	private String tabletWebURL;
	private String generalManager;
	private String address;
	private String zipCode;
	private String state;
	private String status;
	private String phone;
	private Date creationDate;
	private long createdByUserId;
    private Date lastModifiedDate;
    private long lastModifiedByUserId;
    private Date historyDate;
    private long updateDeleteByUserId;
	
	public PropertyHistObj() {
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMarket() {
		return market;
	}

	public void setMarket(String market) {
		this.market = market;
	}

	public String getDMARank() {
		return DMARank;
	}

	public void setDMARank(String dMARank) {
		DMARank = dMARank;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getDFPPropertyName() {
		return DFPPropertyName;
	}

	public void setDFPPropertyName(String dFPPropertyName) {
		DFPPropertyName = dFPPropertyName;
	}

	public String getAffiliation() {
		return affiliation;
	}

	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}

	public String getWebSite() {
		return webSite;
	}

	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

	public String getMobileWebURL() {
		return mobileWebURL;
	}

	public void setMobileWebURL(String mobileWebURL) {
		this.mobileWebURL = mobileWebURL;
	}

	public String getTabletWebURL() {
		return tabletWebURL;
	}

	public void setTabletWebURL(String tabletWebURL) {
		this.tabletWebURL = tabletWebURL;
	}

	public String getGeneralManager() {
		return generalManager;
	}

	public void setGeneralManager(String generalManager) {
		this.generalManager = generalManager;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setPublisherId(String publisherId) {
		this.publisherId = publisherId;
	}

	public String getPublisherId() {
		return publisherId;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreatedByUserId(long createdByUserId) {
		this.createdByUserId = createdByUserId;
	}

	public long getCreatedByUserId() {
		return createdByUserId;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedByUserId(long lastModifiedByUserId) {
		this.lastModifiedByUserId = lastModifiedByUserId;
	}

	public long getLastModifiedByUserId() {
		return lastModifiedByUserId;
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

	public void setDFPPropertyId(String dFPPropertyId) {
		DFPPropertyId = dFPPropertyId;
	}

	public String getDFPPropertyId() {
		return DFPPropertyId;
	}

	public String getAdserverId() {
		return adserverId;
	}

	public void setAdserverId(String adserverId) {
		this.adserverId = adserverId;
	}

	public String getAdServerUserName() {
		return adServerUserName;
	}

	public void setAdServerUserName(String adServerUserName) {
		this.adServerUserName = adServerUserName;
	}

	public List<PropertyChildObj> getChilds() {
		return childs;
	}

	public void setChilds(List<PropertyChildObj> childs) {
		this.childs = childs;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
	
	
}
