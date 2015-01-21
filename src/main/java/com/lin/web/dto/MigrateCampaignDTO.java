package com.lin.web.dto;

import java.util.List;

/*
 * This DTO is used to migrate campaigns from their DFP to datastore for monitoring performance
 * 
 * @author Youdhveer Panwar
 */
public class MigrateCampaignDTO{
	
	private long orderId;
	private String orderName;
	private String orderStartDate;
	private String orderEndDate;
	private String dfpNetworkCode;
	private long advertiserId;
	private String advertiserName;
	private long agencyId;
	private String agencyName;
	private String campaignType;
	private String companyId;
	private String companyName;
	private  List<MigratePlacementDTO> placementList;
	
	public MigrateCampaignDTO(){
		
	}

	public MigrateCampaignDTO(long orderId, String orderName,
			String orderStartDate, String orderEndDate, String dfpNetworkCode,
			long advertiserId, String advertiserName, long agencyId,
			String agencyName, String campaignType, String companyId,
			String companyName, List<MigratePlacementDTO> placementList) {
		super();
		this.orderId = orderId;
		this.orderName = orderName;
		this.orderStartDate = orderStartDate;
		this.orderEndDate = orderEndDate;
		this.dfpNetworkCode = dfpNetworkCode;
		this.advertiserId = advertiserId;
		this.advertiserName = advertiserName;
		this.agencyId = agencyId;
		this.agencyName = agencyName;
		this.campaignType = campaignType;
		this.companyId = companyId;
		this.companyName = companyName;
		this.placementList = placementList;
	}

	@Override
	public String toString() {
		return "MigrateCampaignDTO [orderId=" + orderId + ", orderName="
				+ orderName + ", orderStartDate=" + orderStartDate
				+ ", orderEndDate=" + orderEndDate + ", dfpNetworkCode="
				+ dfpNetworkCode + ", advertiserId=" + advertiserId
				+ ", advertiserName=" + advertiserName + ", agencyId="
				+ agencyId + ", agencyName=" + agencyName + ", campaignType="
				+ campaignType + ", companyId=" + companyId
				+ ", companyName=" + companyName + ", placementList="
				+ placementList + "]";
	}


	public long getOrderId() {
		return orderId;
	}


	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}


	public String getOrderName() {
		return orderName;
	}


	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}


	public String getOrderStartDate() {
		return orderStartDate;
	}


	public void setOrderStartDate(String orderStartDate) {
		this.orderStartDate = orderStartDate;
	}


	public String getOrderEndDate() {
		return orderEndDate;
	}


	public void setOrderEndDate(String orderEndDate) {
		this.orderEndDate = orderEndDate;
	}


	public String getDfpNetworkCode() {
		return dfpNetworkCode;
	}


	public void setDfpNetworkCode(String dfpNetworkCode) {
		this.dfpNetworkCode = dfpNetworkCode;
	}


	public long getAdvertiserId() {
		return advertiserId;
	}


	public void setAdvertiserId(long advertiserId) {
		this.advertiserId = advertiserId;
	}


	public String getAdvertiserName() {
		return advertiserName;
	}


	public void setAdvertiserName(String advertiserName) {
		this.advertiserName = advertiserName;
	}


	public long getAgencyId() {
		return agencyId;
	}


	public void setAgencyId(long agencyId) {
		this.agencyId = agencyId;
	}


	public String getAgencyName() {
		return agencyName;
	}


	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}

	public List<MigratePlacementDTO> getPlacementList() {
		return placementList;
	}

	public void setPlacementList(List<MigratePlacementDTO> placementList) {
		this.placementList = placementList;
	}

	public String getCampaignType() {
		return campaignType;
	}

	public void setCampaignType(String campaignType) {
		this.campaignType = campaignType;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	
	
}
