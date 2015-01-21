package com.lin.server.bean;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity
/*
 * It is a InsertionOrder entity bean 
 * referenced by ProposalObj
 *  
 * @author Youdhveer Panwar
 */
@Index
public class InsertionOrderObj implements Serializable {
	@Id	private String id;
	
	private String uid;
	private long proposalId;	
	
	private String date;
	private String station;
	private String accExecName;
	private String accExecEmail;
	private String accExecPhone;
	private String accExecFax;
	private String accMgrName;
	private String accMgrEmail;
	private String accMgrPhone;
	private String accMgrFax;
	
	private String agencyName;
	private String agencyAddress;
	private String agencyContact;
	private String agencyPhone;
	private String agencyFax;
	private String agencyEmail;
	
	private String advertiserName;
	private String advertiserAddress;
	private String advertiserContact;
	private String advertiserPhone;
	private String advertiserFax;
	private String advertiserEmail;
	
	private String billingCompName;
	private String billingAddress;
	private String billingContact;
	private String billingPhone;
	private String billingEmail;
	private String billingBasis;
	private String paymentTerms;
	private String rateType;
	private String billingType;
	private String collectionType;
	private String cancellationPolicy;
	private String billingNote;
	private String pixelConversionWindow;
	private String dayParting;
	private String freqCap;
	private String reportingInstructions;
	private String goals;
	private String geoTargets;	
	
	private String createdOn;	
	private String createdBy;
	private String updatedOn;
	private String updatedBy;

	
	public InsertionOrderObj(){
		
	}
	
	public InsertionOrderObj(String id, String uid, long proposalId,
			String date, String station, String accExecName,
			String accExecEmail, String accExecPhone, String accExecFax,
			String accMgrName, String accMgrEmail, String accMgrPhone,
			String accMgrFax, String agencyName,
			String agencyAddress, String agencyContact, String agencyPhone,
			String agencyFax, String agencyEmail,
			String advertiserName, String advertiserAddress,
			String advertiserContact, String advertiserPhone,
			String advertiserFax, String advertiserEmail,
			String billingCompName, String billingAddress,
			String billingContact, String billingPhone, String billingEmail,
			String billingBasis, String paymentTerms, String rateType,
			String billingType, String collectionType,
			String cancellationPolicy, String billingNote,
			String pixelConversionWindow, String dayParting, String freqCap,
			String reportingInstructions, String goals, String geoTargets,
			String createdOn, String createdBy, String updatedOn,
			String updatedBy) {
		this.id = id;
		this.uid = uid;
		this.proposalId = proposalId;
		this.date = date;
		this.station = station;
		this.accExecName = accExecName;
		this.accExecEmail = accExecEmail;
		this.accExecPhone = accExecPhone;
		this.accExecFax = accExecFax;
		this.accMgrName = accMgrName;
		this.accMgrEmail = accMgrEmail;
		this.accMgrPhone = accMgrPhone;
		this.accMgrFax = accMgrFax;
		
		this.agencyName = agencyName;
		this.agencyAddress = agencyAddress;
		this.agencyContact = agencyContact;
		this.agencyPhone = agencyPhone;
		this.agencyFax = agencyFax;
		this.agencyEmail = agencyEmail;
		
		this.advertiserName = advertiserName;
		this.advertiserAddress = advertiserAddress;
		this.advertiserContact = advertiserContact;
		this.advertiserPhone = advertiserPhone;
		this.advertiserFax = advertiserFax;
		this.advertiserEmail = advertiserEmail;
		this.billingCompName = billingCompName;
		this.billingAddress = billingAddress;
		this.billingContact = billingContact;
		this.billingPhone = billingPhone;
		this.billingEmail = billingEmail;
		this.billingBasis = billingBasis;
		this.paymentTerms = paymentTerms;
		this.rateType = rateType;
		this.billingType = billingType;
		this.collectionType = collectionType;
		this.cancellationPolicy = cancellationPolicy;
		this.billingNote = billingNote;
		this.pixelConversionWindow = pixelConversionWindow;
		this.dayParting = dayParting;
		this.freqCap = freqCap;
		this.reportingInstructions = reportingInstructions;
		this.goals = goals;
		this.geoTargets = geoTargets;
		this.createdOn = createdOn;
		this.createdBy = createdBy;
		this.updatedOn = updatedOn;
		this.updatedBy = updatedBy;
	}




	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InsertionOrderObj [id=").append(id).append(", uid=")
				.append(uid).append(", proposalId=").append(proposalId)
				.append(", date=").append(date).append(", station=")
				.append(station).append(", accExecName=").append(accExecName)
				.append(", accExecEmail=").append(accExecEmail)
				.append(", accExecPhone=").append(accExecPhone)
				.append(", accExecFax=").append(accExecFax)
				.append(", accMgrName=").append(accMgrName)
				.append(", accMgrEmail=").append(accMgrEmail)
				.append(", accMgrPhone=").append(accMgrPhone)
				.append(", accMgrFax=").append(accMgrFax)
				.append(", agencyName=").append(agencyName)
				.append(", agencyAddress=").append(agencyAddress)
				.append(", agencyContact=").append(agencyContact)
				.append(", agencyPhone=").append(agencyPhone)
				.append(", agencyFax=").append(agencyFax)
				.append(", agencyEmail=").append(agencyEmail)				
				.append(", advertiserName=").append(advertiserName)
				.append(", advertiserAddress=").append(advertiserAddress)
				.append(", advertiserContact=").append(advertiserContact)
				.append(", advertiserPhone=").append(advertiserPhone)
				.append(", advertiserFax=").append(advertiserFax)
				.append(", advertiserEmail=").append(advertiserEmail)
				.append(", billingCompName=").append(billingCompName)
				.append(", billingAddress=").append(billingAddress)
				.append(", billingContact=").append(billingContact)
				.append(", billingPhone=").append(billingPhone)
				.append(", billingEmail=").append(billingEmail)
				.append(", billingBasis=").append(billingBasis)
				.append(", paymentTerms=").append(paymentTerms)
				.append(", rateType=").append(rateType)
				.append(", billingType=").append(billingType)
				.append(", collectionType=").append(collectionType)
				.append(", cancellationPolicy=").append(cancellationPolicy)
				.append(", billingNote=").append(billingNote)
				.append(", pixelConversionWindow=")
				.append(pixelConversionWindow).append(", dayParting=")
				.append(dayParting).append(", freqCap=").append(freqCap)
				.append(", reportingInstructions=")
				.append(reportingInstructions).append(", goals=").append(goals)
				.append(", geoTargetDMA=").append(geoTargets)
				.append(", createdOn=").append(createdOn)
				.append(", createdBy=").append(createdBy)
				.append(", updatedOn=").append(updatedOn)
				.append(", updatedBy=").append(updatedBy).append("]");
		return builder.toString();
	}




	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getUid() {
		return uid;
	}


	public void setUid(String uid) {
		this.uid = uid;
	}


	public long getProposalId() {
		return proposalId;
	}


	public void setProposalId(long proposalId) {
		this.proposalId = proposalId;
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public String getStation() {
		return station;
	}


	public void setStation(String station) {
		this.station = station;
	}


	public String getAccExecName() {
		return accExecName;
	}


	public void setAccExecName(String accExecName) {
		this.accExecName = accExecName;
	}


	public String getAccExecEmail() {
		return accExecEmail;
	}


	public void setAccExecEmail(String accExecEmail) {
		this.accExecEmail = accExecEmail;
	}


	public String getAccMgrName() {
		return accMgrName;
	}


	public void setAccMgrName(String accMgrName) {
		this.accMgrName = accMgrName;
	}


	public String getAccMgrEmail() {
		return accMgrEmail;
	}


	public void setAccMgrEmail(String accMgrEmail) {
		this.accMgrEmail = accMgrEmail;
	}

	public String getAgencyName() {
		return agencyName;
	}


	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}


	public String getAgencyAddress() {
		return agencyAddress;
	}


	public void setAgencyAddress(String agencyAddress) {
		this.agencyAddress = agencyAddress;
	}


	public String getAgencyContact() {
		return agencyContact;
	}


	public void setAgencyContact(String agencyContact) {
		this.agencyContact = agencyContact;
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


	public String getAgencyEmail() {
		return agencyEmail;
	}


	public void setAgencyEmail(String agencyEmail) {
		this.agencyEmail = agencyEmail;
	}

	public String getAdvertiserName() {
		return advertiserName;
	}


	public void setAdvertiserName(String advertiserName) {
		this.advertiserName = advertiserName;
	}


	public String getAdvertiserAddress() {
		return advertiserAddress;
	}


	public void setAdvertiserAddress(String advertiserAddress) {
		this.advertiserAddress = advertiserAddress;
	}


	public String getAdvertiserContact() {
		return advertiserContact;
	}


	public void setAdvertiserContact(String advertiserContact) {
		this.advertiserContact = advertiserContact;
	}


	public String getAdvertiserPhone() {
		return advertiserPhone;
	}


	public void setAdvertiserPhone(String advertiserPhone) {
		this.advertiserPhone = advertiserPhone;
	}


	public String getAdvertiserFax() {
		return advertiserFax;
	}


	public void setAdvertiserFax(String advertiserFax) {
		this.advertiserFax = advertiserFax;
	}


	public String getAdvertiserEmail() {
		return advertiserEmail;
	}


	public void setAdvertiserEmail(String advertiserEmail) {
		this.advertiserEmail = advertiserEmail;
	}


	public String getBillingCompName() {
		return billingCompName;
	}


	public void setBillingCompName(String billingCompName) {
		this.billingCompName = billingCompName;
	}


	public String getBillingAddress() {
		return billingAddress;
	}


	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}


	public String getBillingContact() {
		return billingContact;
	}


	public void setBillingContact(String billingContact) {
		this.billingContact = billingContact;
	}


	public String getBillingPhone() {
		return billingPhone;
	}


	public void setBillingPhone(String billingPhone) {
		this.billingPhone = billingPhone;
	}


	public String getBillingEmail() {
		return billingEmail;
	}


	public void setBillingEmail(String billingEmail) {
		this.billingEmail = billingEmail;
	}


	public String getBillingBasis() {
		return billingBasis;
	}


	public void setBillingBasis(String billingBasis) {
		this.billingBasis = billingBasis;
	}


	public String getPaymentTerms() {
		return paymentTerms;
	}


	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = paymentTerms;
	}


	public String getRateType() {
		return rateType;
	}


	public void setRateType(String rateType) {
		this.rateType = rateType;
	}


	public String getBillingType() {
		return billingType;
	}


	public void setBillingType(String billingType) {
		this.billingType = billingType;
	}


	public String getCollectionType() {
		return collectionType;
	}


	public void setCollectionType(String collectionType) {
		this.collectionType = collectionType;
	}


	public String getCancellationPolicy() {
		return cancellationPolicy;
	}


	public void setCancellationPolicy(String cancellationPolicy) {
		this.cancellationPolicy = cancellationPolicy;
	}


	public String getBillingNote() {
		return billingNote;
	}


	public void setBillingNote(String billingNote) {
		this.billingNote = billingNote;
	}


	public String getPixelConversionWindow() {
		return pixelConversionWindow;
	}


	public void setPixelConversionWindow(String pixelConversionWindow) {
		this.pixelConversionWindow = pixelConversionWindow;
	}


	public String getDayParting() {
		return dayParting;
	}


	public void setDayParting(String dayParting) {
		this.dayParting = dayParting;
	}


	public String getFreqCap() {
		return freqCap;
	}


	public void setFreqCap(String freqCap) {
		this.freqCap = freqCap;
	}


	public String getReportingInstructions() {
		return reportingInstructions;
	}


	public void setReportingInstructions(String reportingInstructions) {
		this.reportingInstructions = reportingInstructions;
	}


	public String getGoals() {
		return goals;
	}


	public void setGoals(String goals) {
		this.goals = goals;
	}


	
	public String getCreatedOn() {
		return createdOn;
	}


	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}


	public String getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}


	public String getUpdatedOn() {
		return updatedOn;
	}


	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}


	public String getUpdatedBy() {
		return updatedBy;
	}


	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}


	public void setAccExecPhone(String accExecPhone) {
		this.accExecPhone = accExecPhone;
	}


	public String getAccExecPhone() {
		return accExecPhone;
	}


	public void setAccExecFax(String accExecFax) {
		this.accExecFax = accExecFax;
	}


	public String getAccExecFax() {
		return accExecFax;
	}


	public void setAccMgrPhone(String accMgrPhone) {
		this.accMgrPhone = accMgrPhone;
	}


	public String getAccMgrPhone() {
		return accMgrPhone;
	}


	public void setAccMgrFax(String accMgrFax) {
		this.accMgrFax = accMgrFax;
	}


	public String getAccMgrFax() {
		return accMgrFax;
	}

	public void setGeoTargets(String geoTargets) {
		this.geoTargets = geoTargets;
	}

	public String getGeoTargets() {
		return geoTargets;
	}
	
	
	
}
