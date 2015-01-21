package com.lin.server.bean;

import java.io.Serializable;
import java.util.Date;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity

/*
 * @author Naresh Pokhriyal
 * This entity is used in Admin(Accounts) to maintain history for AccountsObj entity
 */
@Index
public class AccountsHistObj implements Serializable {
	@Id	private Long objectId;
	private String id;				// "adServerId" + "adServerUserName" + "," + "accountId"
	private String accountDfpId;
	private String accountName;
	private String accountType;
	private String status;
	private String dfpAccountName;
	private String industry;
	private String webURL;
	private String contact;
	private String phone;
	private String email;
	private String fax;
	private String adServerId;
	private String adServerUserName;
	private String companyId;
	private Date creationDate;
	private long createdByUserId;
    private Date lastModifiedDate;
    private long lastModifiedByUserId;
    private Date historyDate;
    private long updateDeleteByUserId;
	
	public AccountsHistObj(){
	}

	public AccountsHistObj(String id, String accountDfpId, String accountName,
			String accountType, String status, String dfpAccountName,
			String industry, String webURL, String contact, String phone,
			String email, String fax, String adServerId,
			String adServerUserName, String companyId, Date creationDate, long createdByUserId,
			Date lastModifiedDate, long lastModifiedByUserId, Date historyDate,
			long updateDeleteByUserId) {
		this.id = id;
		this.accountDfpId = accountDfpId;
		this.accountName = accountName;
		this.accountType = accountType;
		this.status = status;
		this.dfpAccountName = dfpAccountName;
		this.industry = industry;
		this.webURL = webURL;
		this.contact = contact;
		this.phone = phone;
		this.email = email;
		this.fax = fax;
		this.adServerId = adServerId;
		this.adServerUserName = adServerUserName;
		this.companyId = companyId;
		this.creationDate = creationDate;
		this.createdByUserId = createdByUserId;
		this.lastModifiedDate = lastModifiedDate;
		this.lastModifiedByUserId = lastModifiedByUserId;
		this.historyDate = historyDate;
		this.updateDeleteByUserId = updateDeleteByUserId;
	}

	@Override
	public String toString() {
		return "AccountsHistObj [id=" + id + ", accountDfpId=" + accountDfpId
				+ ", accountName=" + accountName + ", accountType="
				+ accountType + ", status=" + status + ", dfpAccountName="
				+ dfpAccountName + ", industry=" + industry + ", webURL="
				+ webURL + ", contact=" + contact + ", phone=" + phone
				+ ", email=" + email + ", fax=" + fax + ", adServerId="
				+ adServerId + ", adServerUserName=" + adServerUserName
				 + ", companyId=" + companyId
				+ ", creationDate=" + creationDate + ", createdByUserId="
				+ createdByUserId + ", lastModifiedDate=" + lastModifiedDate
				+ ", lastModifiedByUserId=" + lastModifiedByUserId
				+ ", historyDate=" + historyDate + ", updateDeleteByUserId="
				+ updateDeleteByUserId + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccountDfpId() {
		return accountDfpId;
	}

	public void setAccountDfpId(String accountDfpId) {
		this.accountDfpId = accountDfpId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDfpAccountName() {
		return dfpAccountName;
	}

	public void setDfpAccountName(String dfpAccountName) {
		this.dfpAccountName = dfpAccountName;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getWebURL() {
		return webURL;
	}

	public void setWebURL(String webURL) {
		this.webURL = webURL;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getAdServerId() {
		return adServerId;
	}

	public void setAdServerId(String adServerId) {
		this.adServerId = adServerId;
	}

	public String getAdServerUserName() {
		return adServerUserName;
	}

	public void setAdServerUserName(String adServerUserName) {
		this.adServerUserName = adServerUserName;
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

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	

	
}
