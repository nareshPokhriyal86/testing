package com.lin.web.dto;

public class AdvertiserReallocationHeaderDTO {

	private String 	totalBudget;
	private String 	startDate;
	private String 	endDate;
	private String 	impressions;
	private String 	clicks;
	private String CTR;
	private String date;
	
	public AdvertiserReallocationHeaderDTO() {
		
	}
	
	
	public AdvertiserReallocationHeaderDTO(String totalBudget,
			String startDate, String endDate, String impressions,
			String clicks, String cTR, String date) {
		super();
		this.totalBudget = totalBudget;
		this.startDate = startDate;
		this.endDate = endDate;
		this.impressions = impressions;
		this.clicks = clicks;
		CTR = cTR;
		this.date = date;
	}


	public String getTotalBudget() {
		return totalBudget;
	}
	public void setTotalBudget(String totalBudget) {
		this.totalBudget = totalBudget;
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
	public String getImpressions() {
		return impressions;
	}
	public void setImpressions(String impressions) {
		this.impressions = impressions;
	}
	public String getClicks() {
		return clicks;
	}
	public void setClicks(String clicks) {
		this.clicks = clicks;
	}
	public String getCTR() {
		return CTR;
	}
	public void setCTR(String cTR) {
		CTR = cTR;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDate() {
		return date;
	}
}
