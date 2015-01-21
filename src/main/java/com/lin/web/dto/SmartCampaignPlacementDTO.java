package com.lin.web.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SmartCampaignPlacementDTO implements Serializable {
	
	private long id;
	private String placementName;
	private long  impressions;
	private String startDate;  
	private String endDate;
	private double rate;
	private double budget;
	private String creatives;
	private String cities;
	private String notes;
	private String dmas;
	private String partnerDFPNetworkCode;
	private String partnerName;
	private String lineItemName;
	private long lineItemId;
	private String gptTag;
	private String productId;
	private String productName;
	private String itemType="1";
	private Integer deviceCapability=0;
	public SmartCampaignPlacementDTO(){
		
	}
	
	public SmartCampaignPlacementDTO(long id, String placementName,
			long impressions, String startDate, String endDate, double rate,
			double budget, String creatives, String dmas, String notes) {
		this(id, placementName, impressions, startDate, endDate, rate, budget, creatives, dmas, notes, null, null);
	}
	public SmartCampaignPlacementDTO(long id, String placementName,
			long impressions, String startDate, String endDate, double rate,
			double budget, String creatives, String dmas, String notes, String itemType, Integer deviceCapability) {
		this.id = id;
		this.placementName = placementName;
		this.impressions = impressions;
		this.startDate = startDate;
		this.endDate = endDate;
		this.rate = rate;
		this.budget = budget;
		this.creatives = creatives;
		this.dmas = dmas;
		this.notes = notes;
		this.setItemType(itemType);
		this.setDeviceCapability(deviceCapability);
	}
	
	
	public SmartCampaignPlacementDTO(long id, String placementName,
			long impressions, String startDate, String endDate, double rate,
			double budget, String creatives, String dmas, String notes,
			String partnerDFPNetworkCode, String partnerName,
			String lineItemName, long lineItemId) {
		this.id = id;
		this.placementName = placementName;
		this.impressions = impressions;
		this.startDate = startDate;
		this.endDate = endDate;
		this.rate = rate;
		this.budget = budget;
		this.creatives = creatives;
		this.dmas = dmas;
		this.notes = notes;
		this.partnerDFPNetworkCode = partnerDFPNetworkCode;
		this.partnerName = partnerName;
		this.lineItemName = lineItemName;
		this.lineItemId = lineItemId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SmartCampaignPlacementDTO [id=");
		builder.append(id);
		builder.append(", placementName=");
		builder.append(placementName);
		builder.append(", impressions=");
		builder.append(impressions);
		builder.append(", startDate=");
		builder.append(startDate);
		builder.append(", endDate=");
		builder.append(endDate);
		builder.append(", rate=");
		builder.append(rate);
		builder.append(", budget=");
		builder.append(budget);
		builder.append(", creatives=");
		builder.append(creatives);
		builder.append(", cities=");
		builder.append(cities);
		builder.append(", dmas=");
		builder.append(dmas);
		builder.append(", notes=");
		builder.append(notes);
		builder.append(", partnerDFPNetworkCode=");
		builder.append(partnerDFPNetworkCode);
		builder.append(", partnerName=");
		builder.append(partnerName);
		builder.append(", lineItemName=");
		builder.append(lineItemName);
		builder.append(", lineItemId=");
		builder.append(lineItemId);
		builder.append(", gptTag=");
		builder.append(gptTag);
		builder.append("]");
		return builder.toString();
	}

	public String getPlacementName() {
		return placementName;
	}
	public void setPlacementName(String placementName) {
		this.placementName = placementName;
	}
	public long getImpressions() {
		return impressions;
	}
	public void setImpressions(long impressions) {
		this.impressions = impressions;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	public double getBudget() {
		return budget;
	}
	public void setBudget(double budget) {
		this.budget = budget;
	}
	public String getCreatives() {
		return creatives;
	}
	public void setCreatives(String creatives) {
		this.creatives = creatives;
	}
	public String getCities() {
		return cities;
	}
	public void setCities(String cities) {
		this.cities = cities;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public long getLineItemId() {
		return lineItemId;
	}

	public void setLineItemId(long lineItemId) {
		this.lineItemId = lineItemId;
	}

	public String getLineItemName() {
		return lineItemName;
	}

	public void setLineItemName(String lineItemName) {
		this.lineItemName = lineItemName;
	}

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	public String getPartnerDFPNetworkCode() {
		return partnerDFPNetworkCode;
	}

	public void setPartnerDFPNetworkCode(String partnerDFPNetworkCode) {
		this.partnerDFPNetworkCode = partnerDFPNetworkCode;
	}

	public String getGptTag() {
		return gptTag;
	}

	public void setGptTag(String gptTag) {
		this.gptTag = gptTag;
	}

	public String getDmas() {
		return dmas;
	}

	public void setDmas(String dmas) {
		this.dmas = dmas;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public Integer getDeviceCapability() {
		return deviceCapability;
	}

	public void setDeviceCapability(Integer deviceCapability) {
		this.deviceCapability = deviceCapability;
	}

	
	

}
