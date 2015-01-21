package com.lin.server.bean;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity
@SuppressWarnings("serial")
@Index
public class AdvertiserReportObj implements Serializable{

	@Id	private String id;
	
	private String advertiserName;
	private String orderName;
	private String lineItemName;
	private String creativeSize;
	private String date;
	private long advertiserId;	
	private long orderId;
	private long lineItemId;
	private String advertiserLabel;
	private String orderStartDate;
	private String orderEndDate;
	private String orderStartTime;
	private String orderEndTime;
	private long orderPONumber;
	private long orderLifeTimeClicks;
	private long orderLifeTimeImpressions;
	private String agencyName;
	private String lineItemStartDate;
	private String lineItemEndDate;
	private String lineItemStartTime;
	private String lineItemEndTime;
	private String costType;	
	private double rate;
	private long goalQuantity;
	private long lineItemLifeTimeClicks;
	private long lineItemLifeItemImpressions;
	private String contractedQuantity;
	private long adServerImpressions;
	private double adServerCTR;
	private long adServerClicks;
	private double deliveryIndicator;
	private double adServerECPM;
	
	public AdvertiserReportObj(){
		
	}

	
	public AdvertiserReportObj(String id, String advertiserName,
			String orderName, String lineItemName, String creativeSize,
			String date, long advertiserId, long orderId, long lineItemId,
			String advertiserLabel, String orderStartDate, String orderEndDate,
			String orderStartTime, String orderEndTime, long orderPONumber,
			long orderLifeTimeClicks, long orderLifeTimeImpressions,
			String agencyName, String lineItemStartDate,
			String lineItemEndDate, String lineItemStartTime,
			String lineItemEndTime, String costType, double rate,
			long goalQuantity, long lineItemLifeTimeClicks,
			long lineItemLifeItemImpressions, String contractedQuantity,
			long adServerImpressions, double adServerCTR, long adServerClicks,
			double deliveryIndicator, double adServerECPM) {
		this.id = id;
		this.advertiserName = advertiserName;
		this.orderName = orderName;
		this.lineItemName = lineItemName;
		this.creativeSize = creativeSize;
		this.date = date;
		this.advertiserId = advertiserId;
		this.orderId = orderId;
		this.lineItemId = lineItemId;
		this.advertiserLabel = advertiserLabel;
		this.orderStartDate = orderStartDate;
		this.orderEndDate = orderEndDate;
		this.orderStartTime = orderStartTime;
		this.orderEndTime = orderEndTime;
		this.orderPONumber = orderPONumber;
		this.orderLifeTimeClicks = orderLifeTimeClicks;
		this.orderLifeTimeImpressions = orderLifeTimeImpressions;
		this.agencyName = agencyName;
		this.lineItemStartDate = lineItemStartDate;
		this.lineItemEndDate = lineItemEndDate;
		this.lineItemStartTime = lineItemStartTime;
		this.lineItemEndTime = lineItemEndTime;
		this.costType = costType;
		this.rate = rate;
		this.goalQuantity = goalQuantity;
		this.lineItemLifeTimeClicks = lineItemLifeTimeClicks;
		this.lineItemLifeItemImpressions = lineItemLifeItemImpressions;
		this.contractedQuantity = contractedQuantity;
		this.adServerImpressions = adServerImpressions;
		this.adServerCTR = adServerCTR;
		this.adServerClicks = adServerClicks;
		this.deliveryIndicator = deliveryIndicator;
		this.adServerECPM = adServerECPM;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAdvertiserName() {
		return advertiserName;
	}

	public void setAdvertiserName(String advertiserName) {
		this.advertiserName = advertiserName;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public String getLineItemName() {
		return lineItemName;
	}

	public void setLineItemName(String lineItemName) {
		this.lineItemName = lineItemName;
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

	public long getAdvertiserId() {
		return advertiserId;
	}

	public void setAdvertiserId(long advertiserId) {
		this.advertiserId = advertiserId;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getLineItemId() {
		return lineItemId;
	}

	public void setLineItemId(long lineItemId) {
		this.lineItemId = lineItemId;
	}

	public String getAdvertiserLabel() {
		return advertiserLabel;
	}

	public void setAdvertiserLabel(String advertiserLabel) {
		this.advertiserLabel = advertiserLabel;
	}

	public String getOrderStartDate() {
		return orderStartDate;
	}

	public void setOrderStartDate(String orderStartDate) {
		this.orderStartDate = orderStartDate;
	}

	public String getOrderEndDate() {
		return orderEndDate;
	}

	public void setOrderEndDate(String orderEndDate) {
		this.orderEndDate = orderEndDate;
	}

	public String getOrderStartTime() {
		return orderStartTime;
	}

	public void setOrderStartTime(String orderStartTime) {
		this.orderStartTime = orderStartTime;
	}

	public String getOrderEndTime() {
		return orderEndTime;
	}

	public void setOrderEndTime(String orderEndTime) {
		this.orderEndTime = orderEndTime;
	}

	public long getOrderPONumber() {
		return orderPONumber;
	}

	public void setOrderPONumber(long orderPONumber) {
		this.orderPONumber = orderPONumber;
	}

	public long getOrderLifeTimeClicks() {
		return orderLifeTimeClicks;
	}

	public void setOrderLifeTimeClicks(long orderLifeTimeClicks) {
		this.orderLifeTimeClicks = orderLifeTimeClicks;
	}

	public long getOrderLifeTimeImpressions() {
		return orderLifeTimeImpressions;
	}

	public void setOrderLifeTimeImpressions(long orderLifeTimeImpressions) {
		this.orderLifeTimeImpressions = orderLifeTimeImpressions;
	}

	public String getAgencyName() {
		return agencyName;
	}

	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}

	public String getLineItemStartDate() {
		return lineItemStartDate;
	}

	public void setLineItemStartDate(String lineItemStartDate) {
		this.lineItemStartDate = lineItemStartDate;
	}

	public String getLineItemEndDate() {
		return lineItemEndDate;
	}

	public void setLineItemEndDate(String lineItemEndDate) {
		this.lineItemEndDate = lineItemEndDate;
	}

	public String getLineItemStartTime() {
		return lineItemStartTime;
	}

	public void setLineItemStartTime(String lineItemStartTime) {
		this.lineItemStartTime = lineItemStartTime;
	}

	public String getLineItemEndTime() {
		return lineItemEndTime;
	}

	public void setLineItemEndTime(String lineItemEndTime) {
		this.lineItemEndTime = lineItemEndTime;
	}

	public String getCostType() {
		return costType;
	}

	public void setCostType(String costType) {
		this.costType = costType;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public long getGoalQuantity() {
		return goalQuantity;
	}

	public void setGoalQuantity(long goalQuantity) {
		this.goalQuantity = goalQuantity;
	}

	public long getLineItemLifeTimeClicks() {
		return lineItemLifeTimeClicks;
	}

	public void setLineItemLifeTimeClicks(long lineItemLifeTimeClicks) {
		this.lineItemLifeTimeClicks = lineItemLifeTimeClicks;
	}

	public long getLineItemLifeItemImpressions() {
		return lineItemLifeItemImpressions;
	}

	public void setLineItemLifeItemImpressions(long lineItemLifeItemImpressions) {
		this.lineItemLifeItemImpressions = lineItemLifeItemImpressions;
	}

	public String getContractedQuantity() {
		return contractedQuantity;
	}

	public void setContractedQuantity(String contractedQuantity) {
		this.contractedQuantity = contractedQuantity;
	}

	public long getAdServerImpressions() {
		return adServerImpressions;
	}

	public void setAdServerImpressions(long adServerImpressions) {
		this.adServerImpressions = adServerImpressions;
	}

	public double getAdServerCTR() {
		return adServerCTR;
	}

	public void setAdServerCTR(double adServerCTR) {
		this.adServerCTR = adServerCTR;
	}

	public long getAdServerClicks() {
		return adServerClicks;
	}

	public void setAdServerClicks(long adServerClicks) {
		this.adServerClicks = adServerClicks;
	}

	public double getDeliveryIndicator() {
		return deliveryIndicator;
	}

	public void setDeliveryIndicator(double deliveryIndicator) {
		this.deliveryIndicator = deliveryIndicator;
	}

	public double getAdServerECPM() {
		return adServerECPM;
	}

	public void setAdServerECPM(double adServerECPM) {
		this.adServerECPM = adServerECPM;
	}	
	
	
	
	
	
}

