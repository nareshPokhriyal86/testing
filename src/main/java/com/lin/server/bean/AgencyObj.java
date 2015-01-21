package com.lin.server.bean;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity

/*
 * @author Youdhveer Panwar
 * This entity is used in campaign setup to create agencies in datastore
 */
@SuppressWarnings("serial")
@Index
public class AgencyObj implements Serializable {
	@Id	private Long id; 
	private long agencyId;
	private String name;
	private String address;	
	private String state;
	private String stateShort;
	private String zipCode;
	private String phone;
	private String fax;
	private String email;
	private String contactPersonName;
	private String createdBy;
	private String createdOn;
	private String dfpNetworkCode; 
	
	public AgencyObj(){
	}
	
	public AgencyObj(long agencyId,String name,String dfpNetworkCode){	
		this.agencyId=agencyId;
		this.name=name;
		this.dfpNetworkCode=dfpNetworkCode;
	}

	public AgencyObj(String name, String address, String state,
			String phone, String fax, String email) {
		this.name = name;
		this.address = address;
		this.state = state;
		this.phone = phone;
		this.fax = fax;
		this.email = email;
	}
	
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AgencyObj [id=");
		builder.append(id);
		builder.append(", agencyId=");
		builder.append(agencyId);
		builder.append(", name=");
		builder.append(name);
		builder.append(", address=");
		builder.append(address);
		builder.append(", state=");
		builder.append(state);
		builder.append(", stateShort=");
		builder.append(stateShort);
		builder.append(", zipCode=");
		builder.append(zipCode);
		builder.append(", phone=");
		builder.append(phone);
		builder.append(", fax=");
		builder.append(fax);
		builder.append(", email=");
		builder.append(email);
		builder.append(", contactPersonName=");
		builder.append(contactPersonName);
		builder.append(", createdBy=");
		builder.append(createdBy);
		builder.append(", createdOn=");
		builder.append(createdOn);
		builder.append(", dfpNetworkCode=");
		builder.append(dfpNetworkCode);
		builder.append("]");
		return builder.toString();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(long agencyId) {
		this.agencyId = agencyId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStateShort() {
		return stateShort;
	}

	public void setStateShort(String stateShort) {
		this.stateShort = stateShort;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContactPersonName() {
		return contactPersonName;
	}

	public void setContactPersonName(String contactPersonName) {
		this.contactPersonName = contactPersonName;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getDfpNetworkCode() {
		return dfpNetworkCode;
	}

	public void setDfpNetworkCode(String dfpNetworkCode) {
		this.dfpNetworkCode = dfpNetworkCode;
	}
	
	
}
