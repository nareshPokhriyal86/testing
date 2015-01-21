package com.lin.web.dto;


public class SmartCampaignAdvertiserDTO {

	private String campaignID;
	private String name;
	private String address;
	private String email;
	private String phone;
	private String fax;
	private String state;
	private String zipCode;
	
	public SmartCampaignAdvertiserDTO() {
		
	}

	public SmartCampaignAdvertiserDTO(String campaignID, String name,
			String address, String email, String phone, String fax,
			String state, String zipCode) {
		super();
		this.campaignID = campaignID;
		this.name = name;
		this.address = address;
		this.email = email;
		this.phone = phone;
		this.fax = fax;
		this.state = state;
		this.zipCode = zipCode;
	}

	@Override
	public String toString() {
		return "SmartCampaignPublisherDTO [campaignID=" + campaignID
				+ ", name=" + name + ", address=" + address + ", email="
				+ email + ", phone=" + phone + ", fax=" + fax + ", state="
				+ state + ", zipCode=" + zipCode + "]";
	}

	public String getCampaignID() {
		return campaignID;
	}

	public void setCampaignID(String campaignID) {
		this.campaignID = campaignID;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
}
