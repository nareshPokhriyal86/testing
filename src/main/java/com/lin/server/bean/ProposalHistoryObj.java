package com.lin.server.bean;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity

/*
 * It is a ProposalHistory entity bean for datastore
 *  
 * @author Youdhveer Panwar
 */
@Index
public class ProposalHistoryObj implements Serializable {
	@Id	private Long id;
	private long proposalId;
	private String companyId;
	private String name;
	private String advertiser;
	private String agency;
	private String salesRep;
	private String industry;
	private String type;
	private String status;
	private double budget;
	private String flightStartDate;
	private String flightEndDate;
	private String kpi;
	private String createdOn;
	private String updatedOn;
	private String createdBy;
	private String updatedBy;

	private String salesContact;
	private String salesEmail;
	private String salesPhone;
	private String trafficContact;
	private String trafficEmail;
	private String trafficPhone;	
	private String geoTargets;
	
	private String activeStatus;
	
	public ProposalHistoryObj(){
		
	}

	public ProposalHistoryObj(long proposalId, String companyId,
			String name, String advertiser, String agency, String salesRep,
			String industry, String type, String status, double budget,
			String flightStartDate, String flightEndDate, String kpi,
			String createdOn, String updatedOn, String createdBy,
			String updatedBy, String salesContact, String salesEmail,
			String salesPhone, String trafficContact, String trafficEmail,
			String trafficPhone, String geoTargets) {
		
		this.proposalId = proposalId;
		this.companyId = companyId;
		this.name = name;
		this.advertiser = advertiser;
		this.agency = agency;
		this.salesRep = salesRep;
		this.industry = industry;
		this.type = type;
		this.status = status;
		this.budget = budget;
		this.flightStartDate = flightStartDate;
		this.flightEndDate = flightEndDate;
		this.kpi = kpi;
		this.createdOn = createdOn;
		this.updatedOn = updatedOn;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
		this.salesContact = salesContact;
		this.salesEmail = salesEmail;
		this.salesPhone = salesPhone;
		this.trafficContact = trafficContact;
		this.trafficEmail = trafficEmail;
		this.trafficPhone = trafficPhone;
		this.geoTargets = geoTargets;
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ProposalObj [id=").append(id).append(", proposalId=")
				.append(proposalId).append(", companyId=").append(companyId)
				.append(", name=").append(name).append(", advertiser=")
				.append(advertiser).append(", agency=").append(agency)
				.append(", salesRep=").append(salesRep).append(", industry=")
				.append(industry).append(", type=").append(type)
				.append(", status=").append(status).append(", budget=")
				.append(budget).append(", flightStartDate=")
				.append(flightStartDate).append(", flightEndDate=")
				.append(flightEndDate).append(", kpi=").append(kpi)
				.append(", createdOn=").append(createdOn)
				.append(", updatedOn=").append(updatedOn)
				.append(", createdBy=").append(createdBy)
				.append(", updatedBy=").append(updatedBy)
				.append(", salesContact=").append(salesContact)
				.append(", salesEmail=").append(salesEmail)
				.append(", salesPhone=").append(salesPhone)
				.append(", trafficContact=").append(trafficContact)
				.append(", trafficEmail=").append(trafficEmail)
				.append(", trafficPhone=").append(trafficPhone)
				.append(", geoTargets=").append(geoTargets).append("]");
		return builder.toString();
	}

	 @Override
     public boolean equals(Object obj) {
		 if(obj instanceof ProposalHistoryObj 
    	   		&& ( (((ProposalHistoryObj)obj).getProposalId())== ((this.proposalId) ) )){
    	   	return true;
    	 }else{
    	  	return false;
    	 }    
     }     
    
     
     @Override
     public int hashCode() {
         final int prime = 31;
         int result = 3;
         result = (int) (prime * (this.proposalId)+result);
         return result;
     }
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public long getProposalId() {
		return proposalId;
	}


	public void setProposalId(long proposalId) {
		this.proposalId = proposalId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getAdvertiser() {
		return advertiser;
	}


	public void setAdvertiser(String advertiser) {
		this.advertiser = advertiser;
	}


	public String getAgency() {
		return agency;
	}


	public void setAgency(String agency) {
		this.agency = agency;
	}


	public String getSalesRep() {
		return salesRep;
	}


	public void setSalesRep(String salesRep) {
		this.salesRep = salesRep;
	}


	public String getIndustry() {
		return industry;
	}


	public void setIndustry(String industry) {
		this.industry = industry;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public double getBudget() {
		return budget;
	}


	public void setBudget(double budget) {
		this.budget = budget;
	}


	public String getFlightStartDate() {
		return flightStartDate;
	}


	public void setFlightStartDate(String flightStartDate) {
		this.flightStartDate = flightStartDate;
	}


	public String getFlightEndDate() {
		return flightEndDate;
	}


	public void setFlightEndDate(String flightEndDate) {
		this.flightEndDate = flightEndDate;
	}


	public String getKpi() {
		return kpi;
	}


	public void setKpi(String kpi) {
		this.kpi = kpi;
	}


	public String getCreatedOn() {
		return createdOn;
	}


	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}


	public String getUpdatedOn() {
		return updatedOn;
	}


	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}


	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}


	public String getCompanyId() {
		return companyId;
	}


	public void setSalesContact(String salesContact) {
		this.salesContact = salesContact;
	}


	public String getSalesContact() {
		return salesContact;
	}


	public void setSalesEmail(String salesEmail) {
		this.salesEmail = salesEmail;
	}


	public String getSalesEmail() {
		return salesEmail;
	}


	public void setSalesPhone(String salesPhone) {
		this.salesPhone = salesPhone;
	}


	public String getSalesPhone() {
		return salesPhone;
	}


	public void setTrafficContact(String trafficContact) {
		this.trafficContact = trafficContact;
	}


	public String getTrafficContact() {
		return trafficContact;
	}


	public void setTrafficEmail(String trafficEmail) {
		this.trafficEmail = trafficEmail;
	}


	public String getTrafficEmail() {
		return trafficEmail;
	}


	public void setTrafficPhone(String trafficPhone) {
		this.trafficPhone = trafficPhone;
	}


	public String getTrafficPhone() {
		return trafficPhone;
	}


	public void setGeoTargets(String geoTargets) {
		this.geoTargets = geoTargets;
	}


	public String getGeoTargets() {
		return geoTargets;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getActiveStatus() {
		return activeStatus;
	}
	

}
