package com.lin.web.dto;

import java.io.Serializable;
import java.util.Date;

public class PerformanceMonitoringAlertDTO implements Serializable{

	private String id;
	private String status;
	private String campaignId;
	private String partner;
	private String totalDays;
	private String daysTillDate;
	private String rateType;
	private String barStatus;
	private String dateProgress;
	private String goalProgress;
	private String goal;
	private String name;
	private String delivered;
	private String clicks;
	private String ctr;
	private String date;
	private String dfpNetworkCode;
	private String dfpStatus;
	private String progressMessage;
	
	public PerformanceMonitoringAlertDTO() {
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getTotalDays() {
		return totalDays;
	}

	public void setTotalDays(String totalDays) {
		this.totalDays = totalDays;
	}

	public String getDaysTillDate() {
		return daysTillDate;
	}

	public void setDaysTillDate(String daysTillDate) {
		this.daysTillDate = daysTillDate;
	}

	public String getRateType() {
		return rateType;
	}

	public void setRateType(String rateType) {
		this.rateType = rateType;
	}

	public String getBarStatus() {
		return barStatus;
	}

	public void setBarStatus(String barStatus) {
		this.barStatus = barStatus;
	}

	public String getDateProgress() {
		return dateProgress;
	}

	public void setDateProgress(String dateProgress) {
		this.dateProgress = dateProgress;
	}

	public String getGoalProgress() {
		return goalProgress;
	}

	public void setGoalProgress(String goalProgress) {
		this.goalProgress = goalProgress;
	}

	public String getGoal() {
		return goal;
	}

	public void setGoal(String goal) {
		this.goal = goal;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDelivered() {
		return delivered;
	}

	public void setDelivered(String delivered) {
		this.delivered = delivered;
	}

	public String getClicks() {
		return clicks;
	}

	public void setClicks(String clicks) {
		this.clicks = clicks;
	}

	public String getCtr() {
		return ctr;
	}

	public void setCtr(String ctr) {
		this.ctr = ctr;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDfpNetworkCode() {
		return dfpNetworkCode;
	}

	public void setDfpNetworkCode(String dfpNetworkCode) {
		this.dfpNetworkCode = dfpNetworkCode;
	}

	public String getDfpStatus() {
		return dfpStatus;
	}

	public void setDfpStatus(String dfpStatus) {
		this.dfpStatus = dfpStatus;
	}

	public String getProgressMessage() {
		return progressMessage;
	}

	public void setProgressMessage(String progressMessage) {
		this.progressMessage = progressMessage;
	}
	
	
}
