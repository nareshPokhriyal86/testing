package com.lin.web.dto;

import java.io.Serializable;

public class AdvertiserTrendAnalysisHeaderDTO implements Serializable{
	
	private String bookedImpressions;
	private String impressions;
	private String lifeTimeImpresions;
	private String clicks;
	private String lifeTimeClicks;
	private String CPM;
	private String CTR;
	private String totalCTR;
	private String revenue;
	private String revenueDelivered;
	private String revenueRemaining;
	private String budget;
	
	
	public AdvertiserTrendAnalysisHeaderDTO(){
		
	}
	
	
	
	public AdvertiserTrendAnalysisHeaderDTO(String bookedImpressions,
			String impressions, String lifeTimeImpresions, String clicks,
			String lifeTimeClicks, String cPM, String cTR, String totalCTR,
			String revenue, String revenueDelivered, String revenueRemaining) {
		super();
		this.bookedImpressions = bookedImpressions;
		this.impressions = impressions;
		this.lifeTimeImpresions = lifeTimeImpresions;
		this.clicks = clicks;
		this.lifeTimeClicks = lifeTimeClicks;
		CPM = cPM;
		CTR = cTR;
		this.totalCTR = totalCTR;
		this.revenue = revenue;
		this.revenueDelivered = revenueDelivered;
		this.revenueRemaining = revenueRemaining;
	}



	public void setBookedImpressions(String bookedImpressions) {
		this.bookedImpressions = bookedImpressions;
	}
	public String getBookedImpressions() {
		return bookedImpressions;
	}
	public void setLifeTimeImpresions(String lifeTimeImpresions) {
		this.lifeTimeImpresions = lifeTimeImpresions;
	}
	public String getLifeTimeImpresions() {
		return lifeTimeImpresions;
	}
	public void setLifeTimeClicks(String lifeTimeClicks) {
		this.lifeTimeClicks = lifeTimeClicks;
	}
	public String getLifeTimeClicks() {
		return lifeTimeClicks;
	}
	public void setCPM(String cPM) {
		CPM = cPM;
	}
	public String getCPM() {
		return CPM;
	}
	public void setCTR(String cTR) {
		CTR = cTR;
	}
	public String getCTR() {
		return CTR;
	}
	public void setRevenueDelivered(String revenueDelivered) {
		this.revenueDelivered = revenueDelivered;
	}
	public String getRevenueDelivered() {
		return revenueDelivered;
	}
	public void setRevenueRemaining(String revenueRemaining) {
		this.revenueRemaining = revenueRemaining;
	}
	public String getRevenueRemaining() {
		return revenueRemaining;
	}

	public void setClicks(String clicks) {
		this.clicks = clicks;
	}

	public String getClicks() {
		return clicks;
	}

	public void setTotalCTR(String totalCTR) {
		this.totalCTR = totalCTR;
	}

	public String getTotalCTR() {
		return totalCTR;
	}

	public void setImpressions(String impressions) {
		this.impressions = impressions;
	}

	public String getImpressions() {
		return impressions;
	}

	public void setRevenue(String revenue) {
		this.revenue = revenue;
	}

	public String getRevenue() {
		return revenue;
	}



	public void setBudget(String budget) {
		this.budget = budget;
	}



	public String getBudget() {
		return budget;
	}
	
}
