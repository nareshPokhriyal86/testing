package com.lin.web.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ProductDTO implements Serializable{
	
	private long id;
	private String name;
	private double offeredRate;
	private long availableImpressions;
	private long matchedImpressions;
	private long allocatedImpressions;
	private double allocatedBudget;
	private double revenueSharePercent;
	private double payout;
	private double netRate;
	private String placementId;
	private String partnerName;
	private String partnerAdserverId;
	private String partnerId;
	private String partnerLogo;
	
	public ProductDTO(){
		
	}
		
	public ProductDTO(long id, String name, double offeredRate,
			long availableImpressions, long allocatedImpressions,
			double allocatedBudget, double revenueSharePercent, double payout,
			double netRate, String placementId, String partnerName,
			String partnerAdserverId, String partnerId){
		this(id, name, offeredRate, availableImpressions, 0, allocatedImpressions,  allocatedBudget, revenueSharePercent, payout, netRate, placementId, partnerName, partnerAdserverId, partnerId);
		
	}
	public ProductDTO(long id, String name, double offeredRate,
			long availableImpressions, long matchedImpressions, long allocatedImpressions,
			double allocatedBudget, double revenueSharePercent, double payout,
			double netRate, String placementId, String partnerName,
			String partnerAdserverId, String partnerId) {
		this.id = id;
		this.name = name;
		this.offeredRate = offeredRate;
		this.availableImpressions = availableImpressions;
		this.matchedImpressions = matchedImpressions;
		this.allocatedImpressions = allocatedImpressions;
		this.allocatedBudget = allocatedBudget;
		this.revenueSharePercent = revenueSharePercent;
		this.payout = payout;
		this.netRate = netRate;
		this.placementId = placementId;
		this.partnerName = partnerName;
		this.partnerAdserverId = partnerAdserverId;
		this.partnerId = partnerId;
	}

	
	@Override
	public String toString() {
		return "ProductDTO [id=" + id + ", name=" + name + ", offeredRate="
				+ offeredRate + ", availableImpressions="
				+ availableImpressions + ", allocatedImpressions="
				+ allocatedImpressions + ", allocatedBudget=" + allocatedBudget
				+ ", revenueSharePercent=" + revenueSharePercent + ", payout="
				+ payout + ", netRate=" + netRate + ", placementId="
				+ placementId + ", partnerName=" + partnerName
				+ ", partnerAdserverId=" + partnerAdserverId + ", partnerId="
				+ partnerId + ", partnerId="+partnerLogo+"]";
	}


	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getOfferedRate() {
		return offeredRate;
	}
	public void setOfferedRate(double offeredRate) {
		this.offeredRate = offeredRate;
	}
	public long getAvailableImpressions() {
		return availableImpressions;
	}
	public void setAvailableImpressions(long availableImpressions) {
		this.availableImpressions = availableImpressions;
	}
	public long getAllocatedImpressions() {
		return allocatedImpressions;
	}
	public void setAllocatedImpressions(long allocatedImpressions) {
		this.allocatedImpressions = allocatedImpressions;
	}
	public double getAllocatedBudget() {
		return allocatedBudget;
	}
	public void setAllocatedBudget(double allocatedBudget) {
		this.allocatedBudget = allocatedBudget;
	}
	public double getRevenueSharePercent() {
		return revenueSharePercent;
	}
	public void setRevenueSharePercent(double revenueSharePercent) {
		this.revenueSharePercent = revenueSharePercent;
	}
	public double getPayout() {
		return payout;
	}
	public void setPayout(double payout) {
		this.payout = payout;
	}
	public double getNetRate() {
		return netRate;
	}
	public void setNetRate(double netRate) {
		this.netRate = netRate;
	}
	public String getPlacementId() {
		return placementId;
	}
	public void setPlacementId(String placementId) {
		this.placementId = placementId;
	}
	public String getPartnerName() {
		return partnerName;
	}
	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}
	public String getPartnerAdserverId() {
		return partnerAdserverId;
	}
	public void setPartnerAdserverId(String partnerAdserverId) {
		this.partnerAdserverId = partnerAdserverId;
	}


	public String getPartnerId() {
		return partnerId;
	}


	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}


	public String getPartnerLogo() {
		return partnerLogo;
	}


	public void setPartnerLogo(String partnerLogo) {
		this.partnerLogo = partnerLogo;
	}

	public long getMatchedImpressions() {
		return matchedImpressions;
	}

	public void setMatchedImpressions(long matchedImpressions) {
		this.matchedImpressions = matchedImpressions;
	}
	

}
