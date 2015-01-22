package com.lin.server.bean;

import java.io.Serializable;
import java.util.Date;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity

/*
 * @author Naresh Pokhriyal
 * This entity is used in Admin(Accounts) to save advertisers/agencies of company's DFP
 */
@Index
public class AccountsEntity implements Serializable, Comparable<AccountsEntity>{
	
	@Id	private String id;				// adServerId_accountId_companyId
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
    private String address;
    private String state;
    private String contactPersonName;
    private String zip;
    
	public AccountsEntity(){
	}
	
	public AccountsEntity(String accountName,
			String accountType, String status, String dfpAccountName,
			String industry, String contact, String phone,
			String email, String fax, String adServerId,
			String companyId, long createdByUserId,String state,String zip,String address){
	
		this.accountName=accountName;
		this.accountType=accountType;
		this.status=status;
		this.dfpAccountName=dfpAccountName;
		this.industry=industry;
		this.contact=contact;
		this.phone=phone;
		this.email=email;
		this.fax=fax;
		this.adServerId=adServerId;
		this.companyId=companyId;
		this.createdByUserId=createdByUserId;
		this.adServerId=adServerId;
		this.state=state;
		this.zip=zip;
		this.address=address;
	}
	public AccountsEntity(String id, String accountDfpId, String accountName,
			String accountType, String status, String dfpAccountName,
			String industry, String webURL, String contact, String phone,
			String email, String fax, String adServerId,
			String adServerUserName, String companyId, Date creationDate, long createdByUserId,
			Date lastModifiedDate, long lastModifiedByUserId) {
		this(id, accountDfpId, accountName, accountType, status, dfpAccountName, industry, webURL, contact, phone, email, fax, adServerId, adServerUserName, companyId, creationDate, createdByUserId, 
				lastModifiedDate, lastModifiedByUserId, null, null);
	}
	public AccountsEntity(String id, String accountDfpId, String accountName,
			String accountType, String status, String dfpAccountName,
			String industry, String webURL, String contact, String phone,
			String email, String fax, String adServerId,
			String adServerUserName, String companyId, Date creationDate, long createdByUserId,
			Date lastModifiedDate, long lastModifiedByUserId, String address, String contactPersonName) {
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
		this.contactPersonName = contactPersonName;
		this.address = address;
	}



	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AccountsEntity [id=");
		builder.append(id);
		builder.append(", accountDfpId=");
		builder.append(accountDfpId);
		builder.append(", accountName=");
		builder.append(accountName);
		builder.append(", accountType=");
		builder.append(accountType);
		builder.append(", status=");
		builder.append(status);
		builder.append(", dfpAccountName=");
		builder.append(dfpAccountName);
		builder.append(", industry=");
		builder.append(industry);
		builder.append(", webURL=");
		builder.append(webURL);
		builder.append(", contact=");
		builder.append(contact);
		builder.append(", phone=");
		builder.append(phone);
		builder.append(", email=");
		builder.append(email);
		builder.append(", fax=");
		builder.append(fax);
		builder.append(", adServerId=");
		builder.append(adServerId);
		builder.append(", adServerUserName=");
		builder.append(adServerUserName);
		builder.append(", companyId=");
		builder.append(companyId);
		builder.append(", creationDate=");
		builder.append(creationDate);
		builder.append(", createdByUserId=");
		builder.append(createdByUserId);
		builder.append(", lastModifiedDate=");
		builder.append(lastModifiedDate);
		builder.append(", lastModifiedByUserId=");
		builder.append(lastModifiedByUserId);
		builder.append(", address=");
		builder.append(address);
		builder.append(", state=");
		builder.append(state);
		builder.append(", contactPersonName=");
		builder.append(contactPersonName);
		builder.append("]");
		return builder.toString();
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
	
	

	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getContactPersonName() {
		return contactPersonName;
	}
	public void setContactPersonName(String contactPersonName) {
		this.contactPersonName = contactPersonName;
	}
	
	@Override
	public int compareTo(AccountsEntity accountsObj) {
		return accountName.compareToIgnoreCase(accountsObj.getAccountName());
	}

	@Override
	public boolean equals(Object obj) {
		AccountsEntity accountsObj = (AccountsEntity) obj;
		if(this.companyId.equals(accountsObj.companyId) && 
				this.adServerId.equals(accountsObj.adServerId) && 
				this.adServerUserName.equals(accountsObj.adServerUserName) &&
				this.accountDfpId.equals(accountsObj.accountDfpId)) {
			return true;
		}
		return false;
	}
	@Override
	public int hashCode() {
		return super.hashCode();
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	
}
