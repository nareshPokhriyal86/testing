package com.lin.server.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity
@Index
public class CompanyHistObj implements Serializable{

	@Id	private Long ObjectId;
	private long id;
	private String companyName;
	private String companyType;
	private String companyLogoURL;
	private String companyLogoName;
	private String status;
	private String webURL;
	private String companyEmail;
	private String phone;
	private String fax;
	private String contactPersonName;
	private String companyAddress;
	private String adServerInfo;
	private List<String> adServerId;
	private List<String> adServerUsername;
	private List<String> adServerPassword;
	private List<String> serviceURL;
	private List<String> appViews;
	private boolean accessToAccounts;
	private boolean accessToProperties;
	private List<String> demandPartnerId;
	private String dataSource;
	private String demandPartnerCategory;
	private List<String> Passback_Site_type;
	private String demandPartnerType;
	private Date creationDate;
	private long createdByUserId;
    private Date lastModifiedDate;
    private long lastModifiedByUserId;
	private Date historyDate;
    private long updateDeleteByUserId;
    
	public CompanyHistObj() {
		
	}

	public Long getObjectId() {
		return ObjectId;
	}

	public void setObjectId(Long objectId) {
		ObjectId = objectId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public void setLastModifiedByUserId(long lastModifiedByUserId) {
		this.lastModifiedByUserId = lastModifiedByUserId;
	}

	public long getLastModifiedByUserId() {
		return lastModifiedByUserId;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setCreatedByUserId(long createdByUserId) {
		this.createdByUserId = createdByUserId;
	}

	public long getCreatedByUserId() {
		return createdByUserId;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setContactPersonName(String contactPersonName) {
		this.contactPersonName = contactPersonName;
	}

	public String getContactPersonName() {
		return contactPersonName;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getFax() {
		return fax;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhone() {
		return phone;
	}

	public void setCompanyEmail(String companyEmail) {
		this.companyEmail = companyEmail;
	}

	public String getCompanyEmail() {
		return companyEmail;
	}

	public void setWebURL(String webURL) {
		this.webURL = webURL;
	}

	public String getWebURL() {
		return webURL;
	}

	public void setAdServerId(List<String> adServerId) {
		this.adServerId = adServerId;
	}

	public List<String> getAdServerId() {
		return adServerId;
	}

	public void setAdServerUsername(List<String> adServerUsername) {
		this.adServerUsername = adServerUsername;
	}

	public List<String> getAdServerUsername() {
		return adServerUsername;
	}

	public void setAdServerPassword(List<String> adServerPassword) {
		this.adServerPassword = adServerPassword;
	}

	public List<String> getAdServerPassword() {
		return adServerPassword;
	}

	public void setDemandPartnerId(List<String> demandPartnerId) {
		this.demandPartnerId = demandPartnerId;
	}

	public List<String> getDemandPartnerId() {
		return demandPartnerId;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDemandPartnerCategory(String demandPartnerCategory) {
		this.demandPartnerCategory = demandPartnerCategory;
	}

	public String getDemandPartnerCategory() {
		return demandPartnerCategory;
	}

	public void setPassback_Site_type(List<String> passback_Site_type) {
		Passback_Site_type = passback_Site_type;
	}

	public List<String> getPassback_Site_type() {
		return Passback_Site_type;
	}

	public void setDemandPartnerType(String demandPartnerType) {
		this.demandPartnerType = demandPartnerType;
	}

	public String getDemandPartnerType() {
		return demandPartnerType;
	}

	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}

	public String getCompanyType() {
		return companyType;
	}

	public void setServiceURL(List<String> serviceURL) {
		this.serviceURL = serviceURL;
	}

	public List<String> getServiceURL() {
		return serviceURL;
	}

	public void setAdServerInfo(String adServerInfo) {
		this.adServerInfo = adServerInfo;
	}

	public String getAdServerInfo() {
		return adServerInfo;
	}

	public List<String> getAppViews() {
		return appViews;
	}

	public void setAppViews(List<String> appViews) {
		this.appViews = appViews;
	}

	public boolean isAccessToAccounts() {
		return accessToAccounts;
	}

	public void setAccessToAccounts(boolean accessToAccounts) {
		this.accessToAccounts = accessToAccounts;
	}

	public boolean isAccessToProperties() {
		return accessToProperties;
	}

	public void setAccessToProperties(boolean accessToProperties) {
		this.accessToProperties = accessToProperties;
	}

	public void setCompanyLogoURL(String companyLogoURL) {
		this.companyLogoURL = companyLogoURL;
	}

	public String getCompanyLogoURL() {
		return companyLogoURL;
	}

	public void setCompanyLogoName(String companyLogoName) {
		this.companyLogoName = companyLogoName;
	}

	public String getCompanyLogoName() {
		return companyLogoName;
	}
	
	
    
    
}
