package com.lin.web.dto;

import java.io.Serializable;
import java.util.Date;

public class PerformanceMonitoringDTO implements Serializable{
	private String orderId;
	private String order;
	private String impressionDelivered;
	private String clicks;
	private String CTR;
	private String startDateTime;
	private String endDateTime;
	private String advertiser;
	private String lineItemId;
	private String lineItemName;
	private String publisherName;
	private String creativeSize;
	private String date;
	private String impAverage;
	private String ctrAverage;
	private String clickAverage;
	private int averageCount;

	//Added By Anup Dutta
	private String operatingSystem;
	//Added By Dheeraj
	private String device;
	
	public PerformanceMonitoringDTO() {
		
	}
	


	public PerformanceMonitoringDTO(String orderId, String order,
			String impressionDelivered, String clicks, String cTR,
			String startDateTime, String endDateTime, String advertiser) {
		super();
		this.orderId = orderId;
		this.order = order;
		this.impressionDelivered = impressionDelivered;
		this.clicks = clicks;
		CTR = cTR;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.advertiser = advertiser;
	}






	public PerformanceMonitoringDTO(String lineItemId, String lineItemName,String impressionDelivered, String clicks,
			String cTR, String startDateTime, String endDateTime) {
		super();
		this.lineItemId = lineItemId;
		this.lineItemName = lineItemName;
		this.impressionDelivered = impressionDelivered;
		this.clicks = clicks;
		CTR = cTR;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		
	}



	public PerformanceMonitoringDTO( String impressionDelivered,
			String clicks, String cTR, String lineItemId, 
			 String creativeSize) {
		super();
		this.impressionDelivered = impressionDelivered;
		this.clicks = clicks;
		CTR = cTR;
		this.lineItemId = lineItemId;
		this.creativeSize = creativeSize;
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PerformanceMonitoringDTO [orderId=");
		builder.append(orderId);
		builder.append(", order=");
		builder.append(order);
		builder.append(", impressionDelivered=");
		builder.append(impressionDelivered);
		builder.append(", clicks=");
		builder.append(clicks);
		builder.append(", CTR=");
		builder.append(CTR);
		builder.append(", startDateTime=");
		builder.append(startDateTime);
		builder.append(", endDateTime=");
		builder.append(endDateTime);
		builder.append(", advertiser=");
		builder.append(advertiser);
		builder.append(", lineItemId=");
		builder.append(lineItemId);
		builder.append(", lineItemName=");
		builder.append(lineItemName);
		builder.append(", publisherName=");
		builder.append(publisherName);
		builder.append(", creativeSize=");
		builder.append(creativeSize);
		builder.append(", date=");
		builder.append(date);
		builder.append(", impAverage=");
		builder.append(impAverage);
		builder.append(", ctrAverage=");
		builder.append(ctrAverage);
		builder.append(", clickAverage=");
		builder.append(clickAverage);
		builder.append(", averageCount=");
		builder.append(averageCount);
		builder.append(", operatingSystem=");
		builder.append(operatingSystem);
		builder.append(", device=");
		builder.append(device);
		builder.append("]");
		return builder.toString();
	}



	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getImpressionDelivered() {
		return impressionDelivered;
	}
	public void setImpressionDelivered(String impressionDelivered) {
		this.impressionDelivered = impressionDelivered;
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
	public String getStartDateTime() {
		return startDateTime;
	}
	public void setStartDateTime(String startDateTime) {
		this.startDateTime = startDateTime;
	}
	public String getEndDateTime() {
		return endDateTime;
	}
	public void setEndDateTime(String endDateTime) {
		this.endDateTime = endDateTime;
	}



	public String getOrderId() {
		return orderId;
	}



	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}



	public String getAdvertiser() {
		return advertiser;
	}



	public void setAdvertiser(String advertiser) {
		this.advertiser = advertiser;
	}



	public String getLineItemId() {
		return lineItemId;
	}



	public void setLineItemId(String lineItemId) {
		this.lineItemId = lineItemId;
	}



	public String getLineItemName() {
		return lineItemName;
	}



	public void setLineItemName(String lineItemName) {
		this.lineItemName = lineItemName;
	}



	public String getPublisherName() {
		return publisherName;
	}



	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}



	public String getCreativeSize() {
		return creativeSize;
	}



	public void setCreativeSize(String creativeSize) {
		this.creativeSize = creativeSize;
	}



	public String getDate() {
		return date;
	}



	public void setDate(String date) {
		this.date = date;
	}



	public String getImpAverage() {
		return impAverage;
	}



	public void setImpAverage(String impAverage) {
		this.impAverage = impAverage;
	}



	public String getCtrAverage() {
		return ctrAverage;
	}



	public void setCtrAverage(String ctrAverage) {
		this.ctrAverage = ctrAverage;
	}



	public String getClickAverage() {
		return clickAverage;
	}



	public void setClickAverage(String clickAverage) {
		this.clickAverage = clickAverage;
	}



	public int getAverageCount() {
		return averageCount;
	}



	public void setAverageCount(int averageCount) {
		this.averageCount = averageCount;
	}



	public String getOperatingSystem() {
		return operatingSystem;
	}



	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}



	public String getDevice() {
		return device;
	}



	public void setDevice(String device) {
		this.device = device;
	}
	
	
}
