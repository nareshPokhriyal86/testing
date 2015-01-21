package com.lin.web.dto;

import java.io.Serializable;

import com.lin.web.util.LinMobileConstants;


/*
 * @author Youdhveer Panwar
 * ProposalDTO
 */
public class ProposalDTO implements Serializable{

	private String proposalId;
	private String company;
	private String proposalName;
	private String advertiser;
	private String advertiserId;
	private String agency;	
	private String agencyId;
	private String salesRep;
	
	private String industry;
	private String customIndustry;
	
	private String proposalType;
	private String proposalStatus;
	private String budget;
	private String flightStartDate;
	private String flightEndDate;
	
	private String kpi;
	private String customKpi;
	
	private String updatedBy;
	private String updatedOn;
	
	private String customAgency;
	private String customAdvertiser;
	private String nextPageControl;
	
	private String salesContact;
	private String salesEmail;
	private String salesPhone;
	private String trafficContact;
	private String trafficEmail;
	private String trafficPhone;	
	private String geoTargets;
	private String customGeoTargets;
	
	private String placementData;
	
	private String showTabs;
	private String fromPool;
	

	public String publisherViewPageName = (LinMobileConstants.APP_VIEWS[0])
			.split(LinMobileConstants.ARRAY_SPLITTER)[1];
	public String advertiserViewPageName = (LinMobileConstants.APP_VIEWS[1])
			.split(LinMobileConstants.ARRAY_SPLITTER)[1];
	public String unifiedCampaign = (LinMobileConstants.APP_VIEWS[2])
			.split(LinMobileConstants.ARRAY_SPLITTER)[1];
	public String newsAndResearchPageName = (LinMobileConstants.APP_VIEWS[3])
			.split(LinMobileConstants.ARRAY_SPLITTER)[1];
	public String adminPageName = (LinMobileConstants.APP_VIEWS[4])
			.split(LinMobileConstants.ARRAY_SPLITTER)[1];
	public String mySettingPageName = LinMobileConstants.APP_VIEWS[5]
			.split(LinMobileConstants.ARRAY_SPLITTER)[1].trim();
	public String poolPageName = (LinMobileConstants.APP_VIEWS[6])
			.split(LinMobileConstants.ARRAY_SPLITTER)[1];
	public String campaignPerformancePageName = (LinMobileConstants.APP_VIEWS[7])
			.split(LinMobileConstants.ARRAY_SPLITTER)[1];
	public String mapEngineName = (LinMobileConstants.APP_VIEWS[8])
			.split(LinMobileConstants.ARRAY_SPLITTER)[1];
	public String report = (LinMobileConstants.APP_VIEWS[9])
			.split(LinMobileConstants.ARRAY_SPLITTER)[1];
	
	public ProposalDTO(){
		
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ProposalDTO [proposalId=").append(proposalId)
				.append(", company=").append(company).append(", proposalName=")
				.append(proposalName).append(", advertiser=")
				.append(advertiser).append(", advertiserId=")
				.append(advertiserId).append(", agency=").append(agency)
				.append(", agencyId=").append(agencyId).append(", salesRep=")
				.append(salesRep).append(", industry=").append(industry)
				.append(", customIndustry=").append(customIndustry)
				.append(", proposalType=").append(proposalType)
				.append(", proposalStatus=").append(proposalStatus)
				.append(", budget=").append(budget)
				.append(", flightStartDate=").append(flightStartDate)
				.append(", flightEndDate=").append(flightEndDate)
				.append(", kpi=").append(kpi).append(", customKpi=")
				.append(customKpi).append(", updatedBy=").append(updatedBy)
				.append(", updatedOn=").append(updatedOn)
				.append(", customAgency=").append(customAgency)
				.append(", customAdvertiser=").append(customAdvertiser)
				.append(", nextPageControl=").append(nextPageControl)
				.append(", salesContact=").append(salesContact)
				.append(", salesEmail=").append(salesEmail)
				.append(", salesPhone=").append(salesPhone)
				.append(", trafficContact=").append(trafficContact)
				.append(", trafficEmail=").append(trafficEmail)
				.append(", trafficPhone=").append(trafficPhone)
				.append(", geoTargets=").append(geoTargets)
				.append(", customGeoTargets=").append(customGeoTargets)
				.append("]");
		return builder.toString();
	}

	 @Override
     public boolean equals(Object obj) {
		 if(obj instanceof ProposalDTO 
    	   		&& ( (Integer.parseInt(((ProposalDTO)obj).getProposalId()))== (Integer.parseInt((this.proposalId) ) ) )){
    	   	return true;
    	 }else{
    	  	return false;
    	 }    
     }     
    
     
     @Override
     public int hashCode() {
         final int prime = 31;
         int result = 1;
         result = prime * (Integer.parseInt(this.proposalId))+result;
         return result;
     }

	public String getSalesContact() {
		return salesContact;
	}




	public void setSalesContact(String salesContact) {
		this.salesContact = salesContact;
	}




	public String getSalesEmail() {
		return salesEmail;
	}




	public void setSalesEmail(String salesEmail) {
		this.salesEmail = salesEmail;
	}




	public String getSalesPhone() {
		return salesPhone;
	}




	public void setSalesPhone(String salesPhone) {
		this.salesPhone = salesPhone;
	}




	public String getTrafficContact() {
		return trafficContact;
	}




	public void setTrafficContact(String trafficContact) {
		this.trafficContact = trafficContact;
	}




	public String getTrafficEmail() {
		return trafficEmail;
	}




	public void setTrafficEmail(String trafficEmail) {
		this.trafficEmail = trafficEmail;
	}




	public String getTrafficPhone() {
		return trafficPhone;
	}




	public void setTrafficPhone(String trafficPhone) {
		this.trafficPhone = trafficPhone;
	}




	public String getGeoTargets() {
		return geoTargets;
	}




	public void setGeoTargets(String geoTargets) {
		this.geoTargets = geoTargets;
	}




	public String getCustomGeoTargets() {
		return customGeoTargets;
	}




	public void setCustomGeoTargets(String customGeoTargets) {
		this.customGeoTargets = customGeoTargets;
	}




	public String getProposalId() {
		return proposalId;
	}


	public void setProposalId(String proposalId) {
		this.proposalId = proposalId;
	}


	public String getCompany() {
		return company;
	}


	public void setCompany(String company) {
		this.company = company;
	}


	public String getProposalName() {
		return proposalName;
	}


	public void setProposalName(String proposalName) {
		this.proposalName = proposalName;
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


	public String getProposalType() {
		return proposalType;
	}


	public void setProposalType(String proposalType) {
		this.proposalType = proposalType;
	}


	public String getProposalStatus() {
		return proposalStatus;
	}


	public void setProposalStatus(String proposalStatus) {
		this.proposalStatus = proposalStatus;
	}


	public String getBudget() {
		return budget;
	}


	public void setBudget(String budget) {
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


	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}


	public String getUpdatedBy() {
		return updatedBy;
	}


	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}


	public String getUpdatedOn() {
		return updatedOn;
	}


	public void setCustomAgency(String customAgency) {
		this.customAgency = customAgency;
	}


	public String getCustomAgency() {
		return customAgency;
	}


	public void setCustomAdvertiser(String customAdvertiser) {
		this.customAdvertiser = customAdvertiser;
	}


	public String getCustomAdvertiser() {
		return customAdvertiser;
	}


	public void setAdvertiserId(String advertiserId) {
		this.advertiserId = advertiserId;
	}


	public String getAdvertiserId() {
		return advertiserId;
	}


	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}


	public String getAgencyId() {
		return agencyId;
	}


	public void setNextPageControl(String nextPageControl) {
		this.nextPageControl = nextPageControl;
	}


	public String getNextPageControl() {
		return nextPageControl;
	}




	public void setCustomIndustry(String customIndustry) {
		this.customIndustry = customIndustry;
	}




	public String getCustomIndustry() {
		return customIndustry;
	}




	public void setCustomKpi(String customKpi) {
		this.customKpi = customKpi;
	}




	public String getCustomKpi() {
		return customKpi;
	}


	public void setPlacementData(String placementData) {
		this.placementData = placementData;
	}


	public String getPlacementData() {
		return placementData;
	}


	public void setShowTabs(String showTabs) {
		this.showTabs = showTabs;
	}


	public String getShowTabs() {
		return showTabs;
	}


	public String getFromPool() {
		return fromPool;
	}


	public void setFromPool(String fromPool) {
		this.fromPool = fromPool;
	}
	
	
	
}
