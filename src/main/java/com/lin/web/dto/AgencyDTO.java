package com.lin.web.dto;

import java.util.List;

public class AgencyDTO {

	private long agencyId;	
	private String agencyName;
	private List<AdvertiserDTO> advertiserNamePerAgency;
	private String budget;
	private String status;	
	private String targeting;	
	private long impressionsDelivered;
	private long clicksDelivered;
	private double revenueDelivered;
	private double revenueRemaining;	
	private long bookedImpressions;
	
	public AgencyDTO(){
		
	}
	public AgencyDTO(long agencyId, String agencyName,
			List<AdvertiserDTO> advertiserNamePerAgency, String budget, String status,
			String targeting, long impressionsDelivered, long clicksDelivered,
			double revenueDelivered, double revenueRemaining,
			long bookedImpressions) {
		this.agencyId = agencyId;
		this.agencyName = agencyName;
		this.advertiserNamePerAgency = advertiserNamePerAgency;
		this.budget = budget;
		this.status = status;
		this.targeting = targeting;
		this.impressionsDelivered = impressionsDelivered;
		this.clicksDelivered = clicksDelivered;
		this.revenueDelivered = revenueDelivered;
		this.revenueRemaining = revenueRemaining;
		this.bookedImpressions = bookedImpressions;
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
	public List<AdvertiserDTO> getAdvertiserNamePerAgency() {
		return advertiserNamePerAgency;
	}
	public void setAdvertiserNamePerAgency(List<AdvertiserDTO> advertiserNamePerAgency) {
		this.advertiserNamePerAgency = advertiserNamePerAgency;
	}
	public String getBudget() {
		return budget;
	}
	public void setBudget(String budget) {
		this.budget = budget;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTargeting() {
		return targeting;
	}
	public void setTargeting(String targeting) {
		this.targeting = targeting;
	}
	public long getImpressionsDelivered() {
		return impressionsDelivered;
	}
	public void setImpressionsDelivered(long impressionsDelivered) {
		this.impressionsDelivered = impressionsDelivered;
	}
	public long getClicksDelivered() {
		return clicksDelivered;
	}
	public void setClicksDelivered(long clicksDelivered) {
		this.clicksDelivered = clicksDelivered;
	}
	public double getRevenueDelivered() {
		return revenueDelivered;
	}
	public void setRevenueDelivered(double revenueDelivered) {
		this.revenueDelivered = revenueDelivered;
	}
	public double getRevenueRemaining() {
		return revenueRemaining;
	}
	public void setRevenueRemaining(double revenueRemaining) {
		this.revenueRemaining = revenueRemaining;
	}
	public long getBookedImpressions() {
		return bookedImpressions;
	}
	public void setBookedImpressions(long bookedImpressions) {
		this.bookedImpressions = bookedImpressions;
	}
	
	
}
