package com.lin.web.dto;

import java.io.Serializable;
import java.util.List;

/*
 * @author Naresh Pokhriyal
 */

public class PublisherIOReportDTO implements Serializable{

	private String companyName;
	private String contact;
	private String address;
	private String email;
	private String phone;
	private String fax;
	
	private String publisherCompanyName;
	private String checkPayableTo;
	private String taxID;
	private String publisherContact;
	private String publisherAddress;
	private String publisherEmail;
	private String publisherPhone;
	private String publisherFax;
	
	private String totalBudget;
	private String totalImpressions;
	
	private List<PlacementDTO> placementDTOList;

	

	public PublisherIOReportDTO(String companyName, String contact,
			String address, String email, String phone, String fax,
			String publisherCompanyName, String checkPayableTo, String taxID,
			String publisherContact, String publisherAddress,
			String publisherEmail, String publisherPhone, String publisherFax,
			String totalBudget, String totalImpressions,
			List<PlacementDTO> placementDTOList) {
		this.companyName = companyName;
		this.contact = contact;
		this.address = address;
		this.email = email;
		this.phone = phone;
		this.fax = fax;
		this.publisherCompanyName = publisherCompanyName;
		this.checkPayableTo = checkPayableTo;
		this.taxID = taxID;
		this.publisherContact = publisherContact;
		this.publisherAddress = publisherAddress;
		this.publisherEmail = publisherEmail;
		this.publisherPhone = publisherPhone;
		this.publisherFax = publisherFax;
		this.totalBudget = totalBudget;
		this.totalImpressions = totalImpressions;
		this.placementDTOList = placementDTOList;
	}

	public PublisherIOReportDTO() {
	}

	

	@Override
	public String toString() {
		return "PublisherIOReportDTO [companyName=" + companyName
				+ ", contact=" + contact + ", address=" + address + ", email="
				+ email + ", phone=" + phone + ", fax=" + fax
				+ ", publisherCompanyName=" + publisherCompanyName
				+ ", checkPayableTo=" + checkPayableTo + ", taxID=" + taxID
				+ ", publisherContact=" + publisherContact
				+ ", publisherAddress=" + publisherAddress
				+ ", publisherEmail=" + publisherEmail + ", publisherPhone="
				+ publisherPhone + ", publisherFax=" + publisherFax
				+ ", totalBudget=" + totalBudget + ", totalImpressions="
				+ totalImpressions + ", placementDTOList=" + placementDTOList
				+ "]";
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
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

	public String getPublisherCompanyName() {
		return publisherCompanyName;
	}

	public void setPublisherCompanyName(String publisherCompanyName) {
		this.publisherCompanyName = publisherCompanyName;
	}

	public String getCheckPayableTo() {
		return checkPayableTo;
	}

	public void setCheckPayableTo(String checkPayableTo) {
		this.checkPayableTo = checkPayableTo;
	}

	public String getTaxID() {
		return taxID;
	}

	public void setTaxID(String taxID) {
		this.taxID = taxID;
	}

	public String getPublisherContact() {
		return publisherContact;
	}

	public void setPublisherContact(String publisherContact) {
		this.publisherContact = publisherContact;
	}

	public String getPublisherAddress() {
		return publisherAddress;
	}

	public void setPublisherAddress(String publisherAddress) {
		this.publisherAddress = publisherAddress;
	}

	public String getPublisherEmail() {
		return publisherEmail;
	}

	public void setPublisherEmail(String publisherEmail) {
		this.publisherEmail = publisherEmail;
	}

	public String getPublisherPhone() {
		return publisherPhone;
	}

	public void setPublisherPhone(String publisherPhone) {
		this.publisherPhone = publisherPhone;
	}

	public String getPublisherFax() {
		return publisherFax;
	}

	public void setPublisherFax(String publisherFax) {
		this.publisherFax = publisherFax;
	}

	public List<PlacementDTO> getPlacementDTOList() {
		return placementDTOList;
	}

	public void setPlacementDTOList(List<PlacementDTO> placementDTOList) {
		this.placementDTOList = placementDTOList;
	}

	public void setTotalBudget(String totalBudget) {
		this.totalBudget = totalBudget;
	}

	public String getTotalBudget() {
		return totalBudget;
	}

	public void setTotalImpressions(String totalImpressions) {
		this.totalImpressions = totalImpressions;
	}

	public String getTotalImpressions() {
		return totalImpressions;
	}
	
	
}
