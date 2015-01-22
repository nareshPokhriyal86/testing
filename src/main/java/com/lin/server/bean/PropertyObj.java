package com.lin.server.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity
@Index
public class PropertyObj implements Serializable, Comparable<PropertyObj> {

	@Id	private String id;
	private List<PropertyChildObj> childs;
	private String propertyName;
	private String market;
	private String DMARank;
	private String DFPPropertyName;
	private String DFPPropertyId;
	private String adServerId;
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
	
	public PropertyObj() {
		
	}

	

	public PropertyObj(String id, List<PropertyChildObj> childs,
			String propertyName, String market, String dMARank,
			String dFPPropertyName, String dFPPropertyId, String adServerId,
			String adServerUserName, String companyId, String affiliation,
			String webSite, String mobileWebURL, String tabletWebURL,
			String generalManager, String address, String zipCode,
			String state, String status, String phone, Date creationDate,
			long createdByUserId, Date lastModifiedDate,
			long lastModifiedByUserId) {
		this.id = id;
		this.childs = childs;
		this.propertyName = propertyName;
		this.market = market;
		DMARank = dMARank;
		DFPPropertyName = dFPPropertyName;
		DFPPropertyId = dFPPropertyId;
		this.adServerId = adServerId;
		this.adServerUserName = adServerUserName;
		this.setCompanyId(companyId);
		this.affiliation = affiliation;
		this.webSite = webSite;
		this.mobileWebURL = mobileWebURL;
		this.tabletWebURL = tabletWebURL;
		this.generalManager = generalManager;
		this.address = address;
		this.zipCode = zipCode;
		this.state = state;
		this.status = status;
		this.phone = phone;
		this.creationDate = creationDate;
		this.createdByUserId = createdByUserId;
		this.lastModifiedDate = lastModifiedDate;
		this.lastModifiedByUserId = lastModifiedByUserId;
	}

    

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PropertyObj [id=");
		builder.append(id);
		builder.append(", childs=");
		builder.append(childs);
		builder.append(", propertyName=");
		builder.append(propertyName);
		builder.append(", market=");
		builder.append(market);
		builder.append(", DMARank=");
		builder.append(DMARank);
		builder.append(", DFPPropertyName=");
		builder.append(DFPPropertyName);
		builder.append(", DFPPropertyId=");
		builder.append(DFPPropertyId);
		builder.append(", adServerId=");
		builder.append(adServerId);
		builder.append(", adServerUserName=");
		builder.append(adServerUserName);
		builder.append(", companyId=");
		builder.append(companyId);
		builder.append(", affiliation=");
		builder.append(affiliation);
		builder.append(", webSite=");
		builder.append(webSite);
		builder.append(", mobileWebURL=");
		builder.append(mobileWebURL);
		builder.append(", tabletWebURL=");
		builder.append(tabletWebURL);
		builder.append(", generalManager=");
		builder.append(generalManager);
		builder.append(", address=");
		builder.append(address);
		builder.append(", zipCode=");
		builder.append(zipCode);
		builder.append(", state=");
		builder.append(state);
		builder.append(", status=");
		builder.append(status);
		builder.append(", phone=");
		builder.append(phone);
		builder.append(", creationDate=");
		builder.append(creationDate);
		builder.append(", createdByUserId=");
		builder.append(createdByUserId);
		builder.append(", lastModifiedDate=");
		builder.append(lastModifiedDate);
		builder.append(", lastModifiedByUserId=");
		builder.append(lastModifiedByUserId);
		builder.append("]");
		return builder.toString();
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

	@Override
	public int compareTo(PropertyObj propertyObj) {
		return propertyName.compareToIgnoreCase(propertyObj.getPropertyName());
	}

	public void setDFPPropertyId(String dFPPropertyId) {
		DFPPropertyId = dFPPropertyId;
	}

	public String getDFPPropertyId() {
		return DFPPropertyId;
	}
	
	public String getAdServerUserName() {
		return adServerUserName;
	}

	public void setAdServerUserName(String adServerUserName) {
		this.adServerUserName = adServerUserName;
	}

	public String getAdServerId() {
		return adServerId;
	}

	public void setAdServerId(String adServerId) {
		this.adServerId = adServerId;
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
	
	@Override
	public boolean equals(Object arg0) {
		// TODO Auto-generated method stub
		return super.equals(arg0);
	}		
	
}
