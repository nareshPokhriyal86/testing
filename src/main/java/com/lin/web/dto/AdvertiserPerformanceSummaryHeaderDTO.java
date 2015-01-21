package com.lin.web.dto;

import java.io.Serializable;

import javax.persistence.Id;

@SuppressWarnings("serial")
public class AdvertiserPerformanceSummaryHeaderDTO implements Serializable {
	@Id	private Long id;
	private String bookedImpressions;
	private String budget;
	private String clicks;
	private String totalClicks;
	private String ctr;
	private String totalCtr;
	private String revenueDelivered;
	private String totalRevenueDelivered;
	private String impressionDelivered;
	private String totalImpressionDelivered;
	private String revenueRemaining;
	
	public AdvertiserPerformanceSummaryHeaderDTO(){
		
	}

	public AdvertiserPerformanceSummaryHeaderDTO(String bookedImpressions,
			String budget, String clicks, String totalClicks, String ctr,
			String totalCtr, String revenueDelivered,
			String totalRevenueDelivered, String impressionDelivered,
			String totalImpressionDelivered, String revenueRemaining) {
		this.bookedImpressions = bookedImpressions;
		this.budget = budget;
		this.clicks = clicks;
		this.totalClicks = totalClicks;
		this.ctr = ctr;
		this.totalCtr = totalCtr;
		this.revenueDelivered = revenueDelivered;
		this.totalRevenueDelivered = totalRevenueDelivered;
		this.impressionDelivered = impressionDelivered;
		this.totalImpressionDelivered = totalImpressionDelivered;
		this.revenueRemaining = revenueRemaining;
	}

	public String getBookedImpressions() {
		return bookedImpressions;
	}

	public void setBookedImpressions(String bookedImpressions) {
		this.bookedImpressions = bookedImpressions;
	}

	public String getBudget() {
		return budget;
	}

	public void setBudget(String budget) {
		this.budget = budget;
	}

	public String getClicks() {
		return clicks;
	}

	public void setClicks(String clicks) {
		this.clicks = clicks;
	}

	public String getTotalClicks() {
		return totalClicks;
	}

	public void setTotalClicks(String totalClicks) {
		this.totalClicks = totalClicks;
	}

	public String getCtr() {
		return ctr;
	}

	public void setCtr(String ctr) {
		this.ctr = ctr;
	}

	public String getTotalCtr() {
		return totalCtr;
	}

	public void setTotalCtr(String totalCtr) {
		this.totalCtr = totalCtr;
	}

	public String getRevenueDelivered() {
		return revenueDelivered;
	}

	public void setRevenueDelivered(String revenueDelivered) {
		this.revenueDelivered = revenueDelivered;
	}

	public String getTotalRevenueDelivered() {
		return totalRevenueDelivered;
	}

	public void setTotalRevenueDelivered(String totalRevenueDelivered) {
		this.totalRevenueDelivered = totalRevenueDelivered;
	}

	public String getImpressionDelivered() {
		return impressionDelivered;
	}

	public void setImpressionDelivered(String impressionDelivered) {
		this.impressionDelivered = impressionDelivered;
	}

	public String getTotalImpressionDelivered() {
		return totalImpressionDelivered;
	}

	public void setTotalImpressionDelivered(String totalImpressionDelivered) {
		this.totalImpressionDelivered = totalImpressionDelivered;
	}

	public String getRevenueRemaining() {
		return revenueRemaining;
	}

	public void setRevenueRemaining(String revenueRemaining) {
		this.revenueRemaining = revenueRemaining;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	
}
