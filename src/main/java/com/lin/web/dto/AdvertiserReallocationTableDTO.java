package com.lin.web.dto;

public class AdvertiserReallocationTableDTO {

	private String lineItem;
	private String lineItemId;
	private String ECPM;
	private String budget;
	private String bookedImpressions;
	private String deliveredImpressions;
	private String clicks;
	private String overUnder;
	private String CTR;
	private String revenueDelivered;
	private String revenueRemaining;
	private String revisedBudget;
	private String revisedBookedImpressions;
	private String revenueToBeDelivered;
	private String totalECPM;
	private String totalBudget;
	private String totalBookedImpressions;
	private String totalDeliveredImpressions;
	private String totalClicks;
	private String totalOverUnder;
	private String totalCTR;
	private String totalRevenueDelivered;
	private String totalRevenueRemaining;
	private String totalRevisedBudget;
	private String totalRevisedBookedImpressions;
	private String totalRevenueToBeDelivered;
	
	public AdvertiserReallocationTableDTO() {
		
	}
	public AdvertiserReallocationTableDTO(String lineItem, String eCPM,
			String budget, String bookedImpressions,
			String deliveredImpressions, String clicks, String overUnder,
			String cTR, String revenueDelivered, String revenueRemaining,
			String revisedBudget, String revisedBookedImpressions,
			String revenueToBeDelivered) {
		
		this.lineItem = lineItem;
		ECPM = eCPM;
		this.budget = budget;
		this.bookedImpressions = bookedImpressions;
		this.deliveredImpressions = deliveredImpressions;
		this.clicks = clicks;
		this.overUnder = overUnder;
		CTR = cTR;
		this.revenueDelivered = revenueDelivered;
		this.revenueRemaining = revenueRemaining;
		this.revisedBudget = revisedBudget;
		this.revisedBookedImpressions = revisedBookedImpressions;
		this.revenueToBeDelivered = revenueToBeDelivered;
	}
	
	public AdvertiserReallocationTableDTO(String lineItem, String lineItemId,
			String eCPM, String budget, String bookedImpressions,
			String deliveredImpressions, String clicks, String overUnder,
			String cTR, String revenueDelivered, String revenueRemaining,
			String revisedBudget, String revisedBookedImpressions,
			String revenueToBeDelivered) {
		super();
		this.lineItem = lineItem;
		this.lineItemId = lineItemId;
		ECPM = eCPM;
		this.budget = budget;
		this.bookedImpressions = bookedImpressions;
		this.deliveredImpressions = deliveredImpressions;
		this.clicks = clicks;
		this.overUnder = overUnder;
		CTR = cTR;
		this.revenueDelivered = revenueDelivered;
		this.revenueRemaining = revenueRemaining;
		this.revisedBudget = revisedBudget;
		this.revisedBookedImpressions = revisedBookedImpressions;
		this.revenueToBeDelivered = revenueToBeDelivered;
	}
	
	
	public AdvertiserReallocationTableDTO(String lineItem, String lineItemId,
			String eCPM, String budget, String bookedImpressions,
			String deliveredImpressions, String clicks, String overUnder,
			String cTR, String revenueDelivered, String revenueRemaining,
			String revisedBudget, String revisedBookedImpressions,
			String revenueToBeDelivered, String totalECPM, String totalBudget,
			String totalBookedImpressions, String totalDeliveredImpressions,
			String totalClicks, String totalOverUnder, String totalCTR,
			String totalRevenueDelivered, String totalRevenueRemaining,
			String totalRevisedBudget, String totalRevisedBookedImpressions,
			String totalRevenueToBeDelivered) {
		super();
		this.lineItem = lineItem;
		this.lineItemId = lineItemId;
		ECPM = eCPM;
		this.budget = budget;
		this.bookedImpressions = bookedImpressions;
		this.deliveredImpressions = deliveredImpressions;
		this.clicks = clicks;
		this.overUnder = overUnder;
		CTR = cTR;
		this.revenueDelivered = revenueDelivered;
		this.revenueRemaining = revenueRemaining;
		this.revisedBudget = revisedBudget;
		this.revisedBookedImpressions = revisedBookedImpressions;
		this.revenueToBeDelivered = revenueToBeDelivered;
		this.totalECPM = totalECPM;
		this.totalBudget = totalBudget;
		this.totalBookedImpressions = totalBookedImpressions;
		this.totalDeliveredImpressions = totalDeliveredImpressions;
		this.totalClicks = totalClicks;
		this.totalOverUnder = totalOverUnder;
		this.totalCTR = totalCTR;
		this.totalRevenueDelivered = totalRevenueDelivered;
		this.totalRevenueRemaining = totalRevenueRemaining;
		this.totalRevisedBudget = totalRevisedBudget;
		this.totalRevisedBookedImpressions = totalRevisedBookedImpressions;
		this.totalRevenueToBeDelivered = totalRevenueToBeDelivered;
	}
	public String getLineItem() {
		return lineItem;
	}
	public void setLineItem(String lineItem) {
		this.lineItem = lineItem;
	}
	public String getECPM() {
		return ECPM;
	}
	public void setECPM(String eCPM) {
		ECPM = eCPM;
	}
	public String getBudget() {
		return budget;
	}
	public void setBudget(String budget) {
		this.budget = budget;
	}
	public String getBookedImpressions() {
		return bookedImpressions;
	}
	public void setBookedImpressions(String bookedImpressions) {
		this.bookedImpressions = bookedImpressions;
	}
	public String getDeliveredImpressions() {
		return deliveredImpressions;
	}
	public void setDeliveredImpressions(String deliveredImpressions) {
		this.deliveredImpressions = deliveredImpressions;
	}
	public String getClicks() {
		return clicks;
	}
	public void setClicks(String clicks) {
		this.clicks = clicks;
	}
	public String getOverUnder() {
		return overUnder;
	}
	public void setOverUnder(String overUnder) {
		this.overUnder = overUnder;
	}
	public String getCTR() {
		return CTR;
	}
	public void setCTR(String cTR) {
		CTR = cTR;
	}
	public String getRevenueDelivered() {
		return revenueDelivered;
	}
	public void setRevenueDelivered(String revenueDelivered) {
		this.revenueDelivered = revenueDelivered;
	}
	public String getRevenueRemaining() {
		return revenueRemaining;
	}
	public void setRevenueRemaining(String revenueRemaining) {
		this.revenueRemaining = revenueRemaining;
	}
	public String getRevisedBudget() {
		return revisedBudget;
	}
	public void setRevisedBudget(String revisedBudget) {
		this.revisedBudget = revisedBudget;
	}
	public String getRevisedBookedImpressions() {
		return revisedBookedImpressions;
	}
	public void setRevisedBookedImpressions(String revisedBookedImpressions) {
		this.revisedBookedImpressions = revisedBookedImpressions;
	}
	public String getRevenueToBeDelivered() {
		return revenueToBeDelivered;
	}
	public void setRevenueToBeDelivered(String revenueToBeDelivered) {
		this.revenueToBeDelivered = revenueToBeDelivered;
	}
	public void setLineItemId(String lineItemId) {
		this.lineItemId = lineItemId;
	}
	public String getLineItemId() {
		return lineItemId;
	}
	public String getTotalECPM() {
		return totalECPM;
	}
	public void setTotalECPM(String totalECPM) {
		this.totalECPM = totalECPM;
	}
	public String getTotalBudget() {
		return totalBudget;
	}
	public void setTotalBudget(String totalBudget) {
		this.totalBudget = totalBudget;
	}
	public String getTotalBookedImpressions() {
		return totalBookedImpressions;
	}
	public void setTotalBookedImpressions(String totalBookedImpressions) {
		this.totalBookedImpressions = totalBookedImpressions;
	}
	public String getTotalDeliveredImpressions() {
		return totalDeliveredImpressions;
	}
	public void setTotalDeliveredImpressions(String totalDeliveredImpressions) {
		this.totalDeliveredImpressions = totalDeliveredImpressions;
	}
	public String getTotalClicks() {
		return totalClicks;
	}
	public void setTotalClicks(String totalClicks) {
		this.totalClicks = totalClicks;
	}
	public String getTotalOverUnder() {
		return totalOverUnder;
	}
	public void setTotalOverUnder(String totalOverUnder) {
		this.totalOverUnder = totalOverUnder;
	}
	public String getTotalCTR() {
		return totalCTR;
	}
	public void setTotalCTR(String totalCTR) {
		this.totalCTR = totalCTR;
	}
	public String getTotalRevenueDelivered() {
		return totalRevenueDelivered;
	}
	public void setTotalRevenueDelivered(String totalRevenueDelivered) {
		this.totalRevenueDelivered = totalRevenueDelivered;
	}
	public String getTotalRevenueRemaining() {
		return totalRevenueRemaining;
	}
	public void setTotalRevenueRemaining(String totalRevenueRemaining) {
		this.totalRevenueRemaining = totalRevenueRemaining;
	}
	public String getTotalRevisedBudget() {
		return totalRevisedBudget;
	}
	public void setTotalRevisedBudget(String totalRevisedBudget) {
		this.totalRevisedBudget = totalRevisedBudget;
	}
	public String getTotalRevisedBookedImpressions() {
		return totalRevisedBookedImpressions;
	}
	public void setTotalRevisedBookedImpressions(
			String totalRevisedBookedImpressions) {
		this.totalRevisedBookedImpressions = totalRevisedBookedImpressions;
	}
	public String getTotalRevenueToBeDelivered() {
		return totalRevenueToBeDelivered;
	}
	public void setTotalRevenueToBeDelivered(String totalRevenueToBeDelivered) {
		this.totalRevenueToBeDelivered = totalRevenueToBeDelivered;
	}
	
	
	

}
