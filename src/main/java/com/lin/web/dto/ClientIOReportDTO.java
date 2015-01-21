package com.lin.web.dto;

import java.io.Serializable;
import java.util.List;

/*
 * @author Naresh Pokhriyal
 */

public class ClientIOReportDTO implements Serializable{

	private String agencyCompanyName;
	private String agencyContact;
	private String agencyAddress;
	private String agencyEmail;
	private String agencyPhone;
	private String agencyFax;
	
	private String agencyDBA;
	private String agencyBillingAddress;
	private String agencyFaxBillingContact;
	private String agencyFaxBillingPhone;
	private String agencyFaxBillingEmail;
	
	private String advertiseCompanyName;
	private String advertiseContact;
	private String advertiseAddress;
	private String advertiseEmail;
	private String advertisePhone;
	private String advertiseFax;
	
	private String advertiserBasisForBilling;
	private String advertiserPaymentTerms;
	/*private String advertiserRateType;
	private String advertiserBillingType;*/
	private String advertiserCollectionType;
	private String advertiserCancellationPolicy;
	
	private String insertionOrderDate;
	private String insertionOrderStation;
	private String accountExecutive;
	private String accountExecutiveEmail;
	private String accountExecutivePhone;
	private String accountExecutiveFax;
	private String accountManager;
	private String accountManagerEmail;
	private String accountManagerPhone;
	private String accountManagerFax;
	
	private String billingNotes;
	
	private String totalECPM;
	private String totalImpressions;
	
	private String campaignNote;
	private String campaignDMA;
	private String campaignGoal;
	private String frequencyCap;
	
	private List<PlacementDTO> placementDTOList;
	private List<CampaignDetailDTO> campaignDetailDTOList;
	
	

	public ClientIOReportDTO(String agencyCompanyName, String agencyContact,
			String agencyAddress, String agencyEmail, String agencyPhone,
			String agencyFax, String agencyDBA, String agencyBillingAddress,
			String agencyFaxBillingContact, String agencyFaxBillingPhone,
			String agencyFaxBillingEmail, String advertiseCompanyName,
			String advertiseContact, String advertiseAddress,
			String advertiseEmail, String advertisePhone, String advertiseFax,
			String advertiserBasisForBilling, String advertiserPaymentTerms,
			String advertiserCollectionType,
			String advertiserCancellationPolicy, String insertionOrderDate,
			String insertionOrderStation, String accountExecutive,
			String accountExecutiveEmail, String accountExecutivePhone,
			String accountExecutiveFax, String accountManager,
			String accountManagerEmail, String accountManagerPhone,
			String accountManagerFax, String billingNotes, String totalECPM,
			String totalImpressions, List<PlacementDTO> placementDTOList, List<CampaignDetailDTO> campaignDetailDTOList) {
		this.agencyCompanyName = agencyCompanyName;
		this.agencyContact = agencyContact;
		this.agencyAddress = agencyAddress;
		this.agencyEmail = agencyEmail;
		this.agencyPhone = agencyPhone;
		this.agencyFax = agencyFax;
		this.agencyDBA = agencyDBA;
		this.agencyBillingAddress = agencyBillingAddress;
		this.agencyFaxBillingContact = agencyFaxBillingContact;
		this.agencyFaxBillingPhone = agencyFaxBillingPhone;
		this.agencyFaxBillingEmail = agencyFaxBillingEmail;
		this.advertiseCompanyName = advertiseCompanyName;
		this.advertiseContact = advertiseContact;
		this.advertiseAddress = advertiseAddress;
		this.advertiseEmail = advertiseEmail;
		this.advertisePhone = advertisePhone;
		this.advertiseFax = advertiseFax;
		this.advertiserBasisForBilling = advertiserBasisForBilling;
		this.advertiserPaymentTerms = advertiserPaymentTerms;
		this.advertiserCollectionType = advertiserCollectionType;
		this.advertiserCancellationPolicy = advertiserCancellationPolicy;
		this.insertionOrderDate = insertionOrderDate;
		this.insertionOrderStation = insertionOrderStation;
		this.accountExecutive = accountExecutive;
		this.accountExecutiveEmail = accountExecutiveEmail;
		this.accountExecutivePhone = accountExecutivePhone;
		this.accountExecutiveFax = accountExecutiveFax;
		this.accountManager = accountManager;
		this.accountManagerEmail = accountManagerEmail;
		this.accountManagerPhone = accountManagerPhone;
		this.accountManagerFax = accountManagerFax;
		this.billingNotes = billingNotes;
		this.totalECPM = totalECPM;
		this.totalImpressions = totalImpressions;
		this.placementDTOList = placementDTOList;
		this.campaignDetailDTOList = campaignDetailDTOList;
	}

	public ClientIOReportDTO() {
	}

	@Override
	public String toString() {
		return "ClientIOReportDTO [agencyCompanyName=" + agencyCompanyName
				+ ", agencyContact=" + agencyContact + ", agencyAddress="
				+ agencyAddress + ", agencyEmail=" + agencyEmail
				+ ", agencyPhone=" + agencyPhone + ", agencyFax=" + agencyFax
				+ ", agencyDBA=" + agencyDBA + ", agencyBillingAddress="
				+ agencyBillingAddress + ", agencyFaxBillingContact="
				+ agencyFaxBillingContact + ", agencyFaxBillingPhone="
				+ agencyFaxBillingPhone + ", agencyFaxBillingEmail="
				+ agencyFaxBillingEmail + ", advertiseCompanyName="
				+ advertiseCompanyName + ", advertiseContact="
				+ advertiseContact + ", advertiseAddress=" + advertiseAddress
				+ ", advertiseEmail=" + advertiseEmail + ", advertisePhone="
				+ advertisePhone + ", advertiseFax=" + advertiseFax
				+ ", advertiserBasisForBilling=" + advertiserBasisForBilling
				+ ", advertiserPaymentTerms=" + advertiserPaymentTerms
				+ ", advertiserCollectionType=" + advertiserCollectionType
				+ ", advertiserCancellationPolicy="
				+ advertiserCancellationPolicy + ", insertionOrderDate="
				+ insertionOrderDate + ", insertionOrderStation="
				+ insertionOrderStation + ", accountExecutive="
				+ accountExecutive + ", accountExecutiveEmail="
				+ accountExecutiveEmail + ", accountExecutivePhone="
				+ accountExecutivePhone + ", accountExecutiveFax="
				+ accountExecutiveFax + ", accountManager=" + accountManager
				+ ", accountManagerEmail=" + accountManagerEmail
				+ ", accountManagerPhone=" + accountManagerPhone
				+ ", accountManagerFax=" + accountManagerFax
				+ ", billingNotes=" + billingNotes + ", totalECPM=" + totalECPM
				+ ", totalImpressions=" + totalImpressions + ", campaignNote="
				+ campaignNote + ", campaignDMA=" + campaignDMA
				+ ", campaignGoal=" + campaignGoal + ", frequencyCap="
				+ frequencyCap + ", placementDTOList=" + placementDTOList
				+ ", campaignDetailDTOList=" + campaignDetailDTOList + "]";
	}

	public String getAgencyCompanyName() {
		return agencyCompanyName;
	}

	public void setAgencyCompanyName(String agencyCompanyName) {
		this.agencyCompanyName = agencyCompanyName;
	}

	public String getAgencyContact() {
		return agencyContact;
	}

	public void setAgencyContact(String agencyContact) {
		this.agencyContact = agencyContact;
	}

	public String getAgencyAddress() {
		return agencyAddress;
	}

	public void setAgencyAddress(String agencyAddress) {
		this.agencyAddress = agencyAddress;
	}

	public String getAgencyEmail() {
		return agencyEmail;
	}

	public void setAgencyEmail(String agencyEmail) {
		this.agencyEmail = agencyEmail;
	}

	public String getAgencyPhone() {
		return agencyPhone;
	}

	public void setAgencyPhone(String agencyPhone) {
		this.agencyPhone = agencyPhone;
	}

	public String getAgencyFax() {
		return agencyFax;
	}

	public void setAgencyFax(String agencyFax) {
		this.agencyFax = agencyFax;
	}

	public String getAgencyDBA() {
		return agencyDBA;
	}

	public void setAgencyDBA(String agencyDBA) {
		this.agencyDBA = agencyDBA;
	}

	public String getAgencyBillingAddress() {
		return agencyBillingAddress;
	}

	public void setAgencyBillingAddress(String agencyBillingAddress) {
		this.agencyBillingAddress = agencyBillingAddress;
	}

	public String getAgencyFaxBillingContact() {
		return agencyFaxBillingContact;
	}

	public void setAgencyFaxBillingContact(String agencyFaxBillingContact) {
		this.agencyFaxBillingContact = agencyFaxBillingContact;
	}

	public String getAgencyFaxBillingPhone() {
		return agencyFaxBillingPhone;
	}

	public void setAgencyFaxBillingPhone(String agencyFaxBillingPhone) {
		this.agencyFaxBillingPhone = agencyFaxBillingPhone;
	}

	public String getAgencyFaxBillingEmail() {
		return agencyFaxBillingEmail;
	}

	public void setAgencyFaxBillingEmail(String agencyFaxBillingEmail) {
		this.agencyFaxBillingEmail = agencyFaxBillingEmail;
	}

	public String getAdvertiseCompanyName() {
		return advertiseCompanyName;
	}

	public void setAdvertiseCompanyName(String advertiseCompanyName) {
		this.advertiseCompanyName = advertiseCompanyName;
	}

	public String getAdvertiseContact() {
		return advertiseContact;
	}

	public void setAdvertiseContact(String advertiseContact) {
		this.advertiseContact = advertiseContact;
	}

	public String getAdvertiseAddress() {
		return advertiseAddress;
	}

	public void setAdvertiseAddress(String advertiseAddress) {
		this.advertiseAddress = advertiseAddress;
	}

	public String getAdvertiseEmail() {
		return advertiseEmail;
	}

	public void setAdvertiseEmail(String advertiseEmail) {
		this.advertiseEmail = advertiseEmail;
	}

	public String getAdvertisePhone() {
		return advertisePhone;
	}

	public void setAdvertisePhone(String advertisePhone) {
		this.advertisePhone = advertisePhone;
	}

	public String getAdvertiseFax() {
		return advertiseFax;
	}

	public void setAdvertiseFax(String advertiseFax) {
		this.advertiseFax = advertiseFax;
	}

	public String getAdvertiserBasisForBilling() {
		return advertiserBasisForBilling;
	}

	public void setAdvertiserBasisForBilling(String advertiserBasisForBilling) {
		this.advertiserBasisForBilling = advertiserBasisForBilling;
	}

	public String getAdvertiserPaymentTerms() {
		return advertiserPaymentTerms;
	}

	public void setAdvertiserPaymentTerms(String advertiserPaymentTerms) {
		this.advertiserPaymentTerms = advertiserPaymentTerms;
	}

	public String getAdvertiserCollectionType() {
		return advertiserCollectionType;
	}

	public void setAdvertiserCollectionType(String advertiserCollectionType) {
		this.advertiserCollectionType = advertiserCollectionType;
	}

	public String getAdvertiserCancellationPolicy() {
		return advertiserCancellationPolicy;
	}

	public void setAdvertiserCancellationPolicy(String advertiserCancellationPolicy) {
		this.advertiserCancellationPolicy = advertiserCancellationPolicy;
	}

	public String getInsertionOrderDate() {
		return insertionOrderDate;
	}

	public void setInsertionOrderDate(String insertionOrderDate) {
		this.insertionOrderDate = insertionOrderDate;
	}

	public String getInsertionOrderStation() {
		return insertionOrderStation;
	}

	public void setInsertionOrderStation(String insertionOrderStation) {
		this.insertionOrderStation = insertionOrderStation;
	}

	public String getAccountExecutive() {
		return accountExecutive;
	}

	public void setAccountExecutive(String accountExecutive) {
		this.accountExecutive = accountExecutive;
	}

	public String getAccountExecutiveEmail() {
		return accountExecutiveEmail;
	}

	public void setAccountExecutiveEmail(String accountExecutiveEmail) {
		this.accountExecutiveEmail = accountExecutiveEmail;
	}

	public String getAccountExecutivePhone() {
		return accountExecutivePhone;
	}

	public void setAccountExecutivePhone(String accountExecutivePhone) {
		this.accountExecutivePhone = accountExecutivePhone;
	}

	public String getAccountExecutiveFax() {
		return accountExecutiveFax;
	}

	public void setAccountExecutiveFax(String accountExecutiveFax) {
		this.accountExecutiveFax = accountExecutiveFax;
	}

	public String getAccountManager() {
		return accountManager;
	}

	public void setAccountManager(String accountManager) {
		this.accountManager = accountManager;
	}

	public String getAccountManagerEmail() {
		return accountManagerEmail;
	}

	public void setAccountManagerEmail(String accountManagerEmail) {
		this.accountManagerEmail = accountManagerEmail;
	}

	public String getAccountManagerPhone() {
		return accountManagerPhone;
	}

	public void setAccountManagerPhone(String accountManagerPhone) {
		this.accountManagerPhone = accountManagerPhone;
	}

	public String getAccountManagerFax() {
		return accountManagerFax;
	}

	public void setAccountManagerFax(String accountManagerFax) {
		this.accountManagerFax = accountManagerFax;
	}

	public String getBillingNotes() {
		return billingNotes;
	}

	public void setBillingNotes(String billingNotes) {
		this.billingNotes = billingNotes;
	}

	public String getTotalECPM() {
		return totalECPM;
	}

	public void setTotalECPM(String totalECPM) {
		this.totalECPM = totalECPM;
	}

	public String getTotalImpressions() {
		return totalImpressions;
	}

	public void setTotalImpressions(String totalImpressions) {
		this.totalImpressions = totalImpressions;
	}

	public List<PlacementDTO> getPlacementDTOList() {
		return placementDTOList;
	}

	public void setPlacementDTOList(List<PlacementDTO> placementDTOList) {
		this.placementDTOList = placementDTOList;
	}

	public List<CampaignDetailDTO> getCampaignDetailDTOList() {
		return campaignDetailDTOList;
	}

	public void setCampaignDetailDTOList(List<CampaignDetailDTO> campaignDetailDTOList) {
		this.campaignDetailDTOList = campaignDetailDTOList;
	}

	public String getCampaignNote() {
		return campaignNote;
	}

	public void setCampaignNote(String campaignNote) {
		this.campaignNote = campaignNote;
	}

	public String getCampaignDMA() {
		return campaignDMA;
	}

	public void setCampaignDMA(String campaignDMA) {
		this.campaignDMA = campaignDMA;
	}

	public String getCampaignGoal() {
		return campaignGoal;
	}

	public void setCampaignGoal(String campaignGoal) {
		this.campaignGoal = campaignGoal;
	}

	public String getFrequencyCap() {
		return frequencyCap;
	}

	public void setFrequencyCap(String frequencyCap) {
		this.frequencyCap = frequencyCap;
	}
	
}
