package com.lin.web.dto;

import java.io.Serializable;

public class LineItemDTO implements Serializable{

	private long lineItemId;
	private long orderId;
	private String name;
	private String orderName;
	private String startDateTime;
	private String endDateTime;
	private String lineItemType;	
	private String costPerUnit;
	private String valueCostPerUnit;	
	private String targetPlatform;	
	private String budget;
	private String status;	
	private String targeting;	
	private long impressionsDelivered;
	private long clicksDelivered;
	private double actualDeliveryPercentage;
	private double expectedDeliveryPercentage;	
	private long last7DaysImpressionsDelivered;
	private long last7DaysClicksDelivered;
	private double cpm;
	private long goalQuantity;
	
	private String accountId;
	private String accountName;
	private String accountType;
	private String agencyId;
	private String agencyName;
	private String advertiserId;
	private String advertiserName;
	private String adServerOrderId;
	private String adServerOrderName;
	private String siteId;
	private String siteName;
	private String parentId;
	private boolean hasChildren;
	private String parentName;
	
	public long getLineItemId() {
		return lineItemId;
	}
	public void setLineItemId(long lineItemId) {
		this.lineItemId = lineItemId;
	}
	public long getOrderId() {
		return orderId;
	}
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getLineItemType() {
		return lineItemType;
	}
	public void setLineItemType(String lineItemType) {
		this.lineItemType = lineItemType;
	}
	public String getCostPerUnit() {
		return costPerUnit;
	}
	public void setCostPerUnit(String costPerUnit) {
		this.costPerUnit = costPerUnit;
	}
	public String getValueCostPerUnit() {
		return valueCostPerUnit;
	}
	public void setValueCostPerUnit(String valueCostPerUnit) {
		this.valueCostPerUnit = valueCostPerUnit;
	}
	public String getTargetPlatform() {
		return targetPlatform;
	}
	public void setTargetPlatform(String targetPlatform) {
		this.targetPlatform = targetPlatform;
	}
	public String getBudget() {
		return budget;
	}
	public void setBudget(String budget) {
		this.budget = budget;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTargeting() {
		return targeting;
	}
	public void setTargeting(String targeting) {
		this.targeting = targeting;
	}
	public long getImpressionsDelivered() {
		return impressionsDelivered;
	}
	public void setImpressionsDelivered(long impressionsDelivered) {
		this.impressionsDelivered = impressionsDelivered;
	}
	public long getClicksDelivered() {
		return clicksDelivered;
	}
	public void setClicksDelivered(long clicksDelivered) {
		this.clicksDelivered = clicksDelivered;
	}
	public double getActualDeliveryPercentage() {
		return actualDeliveryPercentage;
	}
	public void setActualDeliveryPercentage(double actualDeliveryPercentage) {
		this.actualDeliveryPercentage = actualDeliveryPercentage;
	}
	public double getExpectedDeliveryPercentage() {
		return expectedDeliveryPercentage;
	}
	public void setExpectedDeliveryPercentage(double expectedDeliveryPercentage) {
		this.expectedDeliveryPercentage = expectedDeliveryPercentage;
	}
	public long getLast7DaysImpressionsDelivered() {
		return last7DaysImpressionsDelivered;
	}
	public void setLast7DaysImpressionsDelivered(long last7DaysImpressionsDelivered) {
		this.last7DaysImpressionsDelivered = last7DaysImpressionsDelivered;
	}
	public long getLast7DaysClicksDelivered() {
		return last7DaysClicksDelivered;
	}
	public void setLast7DaysClicksDelivered(long last7DaysClicksDelivered) {
		this.last7DaysClicksDelivered = last7DaysClicksDelivered;
	}
	public double getCpm() {
		return cpm;
	}
	public void setCpm(double cpm) {
		this.cpm = cpm;
	}
	public long getGoalQuantity() {
		return goalQuantity;
	}
	public void setGoalQuantity(long goalQuantity) {
		this.goalQuantity = goalQuantity;
	}
	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}
	public String getOrderName() {
		return orderName;
	}
	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}
	public String getAgencyId() {
		return agencyId;
	}
	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}
	public String getAgencyName() {
		return agencyName;
	}
	public void setAdvertiserId(String advertiserId) {
		this.advertiserId = advertiserId;
	}
	public String getAdvertiserId() {
		return advertiserId;
	}
	public void setAdvertiserName(String advertiserName) {
		this.advertiserName = advertiserName;
	}
	public String getAdvertiserName() {
		return advertiserName;
	}
	public void setAdServerOrderId(String adServerOrderId) {
		this.adServerOrderId = adServerOrderId;
	}
	public String getAdServerOrderId() {
		return adServerOrderId;
	}
	public void setAdServerOrderName(String adServerOrderName) {
		this.adServerOrderName = adServerOrderName;
	}
	public String getAdServerOrderName() {
		return adServerOrderName;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	public String getSiteId() {
		return siteId;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getAccountType() {
		return accountType;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public boolean isHasChildren() {
		return hasChildren;
	}
	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	
}
