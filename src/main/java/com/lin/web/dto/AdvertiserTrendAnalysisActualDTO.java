package com.lin.web.dto;

import java.io.Serializable;

public class AdvertiserTrendAnalysisActualDTO implements Serializable{
	
	private String date;
	private String order;
	private String lineItem;
	private String deliveredImpression;
	private String clicks;
	private String CTR;
	private String revenueDelivered;
	private String revenueRemaining;
	
	public AdvertiserTrendAnalysisActualDTO(){
		
	}

	
	public AdvertiserTrendAnalysisActualDTO(String date, String order,
			String lineItem, String deliveredImpression, String clicks,
			String cTR, String revenueDelivered, String revenueRemaining) {
		super();
		this.date = date;
		this.order = order;
		this.lineItem = lineItem;
		this.deliveredImpression = deliveredImpression;
		this.clicks = clicks;
		CTR = cTR;
		this.revenueDelivered = revenueDelivered;
		this.revenueRemaining = revenueRemaining;
	}


	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getLineItem() {
		return lineItem;
	}

	public void setLineItem(String lineItem) {
		this.lineItem = lineItem;
	}

	public String getDeliveredImpression() {
		return deliveredImpression;
	}

	public void setDeliveredImpression(String deliveredImpression) {
		this.deliveredImpression = deliveredImpression;
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
	
}