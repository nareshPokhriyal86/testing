package com.lin.server.bean;

public class RootReportObj {

	private String loadTimestamp;
	private long channelId;
	private String channelName;
	private String channelType;
	private String salesType;
	private long publisherId;
	private String publisherName;
	private String advertiser;
	private String order;
	private String lineItemType;
	private String lineItem;
	private String creative;
	private String creativeSize;
	private String creativeType;
	private String siteId;
	private String siteName;
	private String siteTypeId;
	private String siteType;
	private long zoneId;
	private String zone;
	private long advertiserId;
	private long orderId;
	private long lineitemId;
	private long creativeId;
	private String orderStartDate;
	private String lineItemStartDate;
	private String orderEndDate;
	private String lineItemEndDate;
	private String orderStartTime;
	private String lineItemStartTime;
	private String orderEndTime;
	private String lineitemEndTime;
	private String orderPONumber;
	private String agency;
	private long agencyId;
	private String trafficker;
	private String salesPerson;
	private String date;
	private String costType;
	private double rate;
	private long goalQty;
	private int lineItemPriority;
	private long orderLifetimeImpressions;
	private long lineitemLifetimeImpressions;
	private long ordeLifetimeClicks;
	private long lineitemLifetimeClicks;
	private long requests;
	private long served;
	private long totalImpressions;
	private long impressionsCPM;
	private long impressionsCPC;
	private long impressionsCPD;
	private long totalClicks;
	private long clicksCPM;
	private long clicksCPC;
	private long clicksCPD;
	private double totalRevenue;
	private double revenueCPM;
	private double revenueCPC;
	private double adserverCPMAndCPCRevenue;
	private double revenueCPD;
	private double ECPM;
	private double RPM;
	private double fillRate;
	private double CTR;
	private double contractedQty;
	private double deliveryIndicator;	
	private double budget;
	private double spentLifetime;
	private double balanceLifetime;
	private String adSource;
	
	public RootReportObj(){
		
	}
	
	
	public String getLoadTimestamp() {
		return loadTimestamp;
	}
	public void setLoadTimestamp(String loadTimestamp) {
		this.loadTimestamp = loadTimestamp;
	}
	public long getChannelId() {
		return channelId;
	}
	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public String getChannelType() {
		return channelType;
	}
	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}
	public String getSalesType() {
		return salesType;
	}
	public void setSalesType(String salesType) {
		this.salesType = salesType;
	}
	
	public String getPublisherName() {
		return publisherName;
	}
	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}
	public String getAdvertiser() {
		return advertiser;
	}
	public void setAdvertiser(String advertiser) {
		this.advertiser = advertiser;
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
	public String getCreative() {
		return creative;
	}
	public void setCreative(String creative) {
		this.creative = creative;
	}
	public String getCreativeSize() {
		return creativeSize;
	}
	public void setCreativeSize(String creativeSize) {
		this.creativeSize = creativeSize;
	}
	public String getCreativeType() {
		return creativeType;
	}
	public void setCreativeType(String creativeType) {
		this.creativeType = creativeType;
	}
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getSiteTypeId() {
		return siteTypeId;
	}
	public void setSiteTypeId(String siteTypeId) {
		this.siteTypeId = siteTypeId;
	}
	public String getSiteType() {
		return siteType;
	}
	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}
	public long getZoneId() {
		return zoneId;
	}
	public void setZoneId(long zoneId) {
		this.zoneId = zoneId;
	}
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
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
	public long getLineitemId() {
		return lineitemId;
	}
	public void setLineitemId(long lineitemId) {
		this.lineitemId = lineitemId;
	}
	public long getCreativeId() {
		return creativeId;
	}
	public void setCreativeId(long creativeId) {
		this.creativeId = creativeId;
	}
	public String getOrderStartDate() {
		return orderStartDate;
	}
	public void setOrderStartDate(String orderStartDate) {
		this.orderStartDate = orderStartDate;
	}
	public String getLineItemStartDate() {
		return lineItemStartDate;
	}
	public void setLineItemStartDate(String lineItemStartDate) {
		this.lineItemStartDate = lineItemStartDate;
	}
	public String getOrderEndDate() {
		return orderEndDate;
	}
	public void setOrderEndDate(String orderEndDate) {
		this.orderEndDate = orderEndDate;
	}
	public String getLineItemEndDate() {
		return lineItemEndDate;
	}
	public void setLineItemEndDate(String lineItemEndDate) {
		this.lineItemEndDate = lineItemEndDate;
	}
	public String getOrderStartTime() {
		return orderStartTime;
	}
	public void setOrderStartTime(String orderStartTime) {
		this.orderStartTime = orderStartTime;
	}
	public String getLineItemStartTime() {
		return lineItemStartTime;
	}
	public void setLineItemStartTime(String lineItemStartTime) {
		this.lineItemStartTime = lineItemStartTime;
	}
	public String getOrderEndTime() {
		return orderEndTime;
	}
	public void setOrderEndTime(String orderEndTime) {
		this.orderEndTime = orderEndTime;
	}
	public String getLineitemEndTime() {
		return lineitemEndTime;
	}
	public void setLineitemEndTime(String lineitemEndTime) {
		this.lineitemEndTime = lineitemEndTime;
	}
	public String getOrderPONumber() {
		return orderPONumber;
	}
	public void setOrderPONumber(String orderPONumber) {
		this.orderPONumber = orderPONumber;
	}
	public String getAgency() {
		return agency;
	}
	public void setAgency(String agency) {
		this.agency = agency;
	}
	public long getAgencyId() {
		return agencyId;
	}
	public void setAgencyId(long agencyId) {
		this.agencyId = agencyId;
	}
	public String getTrafficker() {
		return trafficker;
	}
	public void setTrafficker(String trafficker) {
		this.trafficker = trafficker;
	}
	public String getSalesPerson() {
		return salesPerson;
	}
	public void setSalesPerson(String salesPerson) {
		this.salesPerson = salesPerson;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
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

	public int getLineItemPriority() {
		return lineItemPriority;
	}
	public void setLineItemPriority(int lineItemPriority) {
		this.lineItemPriority = lineItemPriority;
	}
	public long getOrderLifetimeImpressions() {
		return orderLifetimeImpressions;
	}
	public void setOrderLifetimeImpressions(long orderLifetimeImpressions) {
		this.orderLifetimeImpressions = orderLifetimeImpressions;
	}
	public long getLineitemLifetimeImpressions() {
		return lineitemLifetimeImpressions;
	}
	public void setLineitemLifetimeImpressions(long lineitemLifetimeImpressions) {
		this.lineitemLifetimeImpressions = lineitemLifetimeImpressions;
	}
	public long getOrdeLifetimeClicks() {
		return ordeLifetimeClicks;
	}
	public void setOrdeLifetimeClicks(long ordeLifetimeClicks) {
		this.ordeLifetimeClicks = ordeLifetimeClicks;
	}
	public long getLineitemLifetimeClicks() {
		return lineitemLifetimeClicks;
	}
	public void setLineitemLifetimeClicks(long lineitemLifetimeClicks) {
		this.lineitemLifetimeClicks = lineitemLifetimeClicks;
	}
	public long getRequests() {
		return requests;
	}
	public void setRequests(long requests) {
		this.requests = requests;
	}
	public long getServed() {
		return served;
	}
	public void setServed(long served) {
		this.served = served;
	}
	public long getTotalImpressions() {
		return totalImpressions;
	}
	public void setTotalImpressions(long totalImpressions) {
		this.totalImpressions = totalImpressions;
	}
	
	public long getTotalClicks() {
		return totalClicks;
	}
	public void setTotalClicks(long totalClicks) {
		this.totalClicks = totalClicks;
	}
	
	public double getTotalRevenue() {
		return totalRevenue;
	}
	public void setTotalRevenue(double totalRevenue) {
		this.totalRevenue = totalRevenue;
	}
	
	public double getAdserverCPMAndCPCRevenue() {
		return adserverCPMAndCPCRevenue;
	}
	public void setAdserverCPMAndCPCRevenue(double adserverCPMAndCPCRevenue) {
		this.adserverCPMAndCPCRevenue = adserverCPMAndCPCRevenue;
	}	

	public double getBudget() {
		return budget;
	}
	public void setBudget(double budget) {
		this.budget = budget;
	}
	public double getSpentLifetime() {
		return spentLifetime;
	}
	public void setSpentLifetime(double spentLifetime) {
		this.spentLifetime = spentLifetime;
	}
	public double getBalanceLifetime() {
		return balanceLifetime;
	}
	public void setBalanceLifetime(double balanceLifetime) {
		this.balanceLifetime = balanceLifetime;
	}


	public void setPublisherId(long publisherId) {
		this.publisherId = publisherId;
	}


	public long getPublisherId() {
		return publisherId;
	}


	public void setAdSource(String adSource) {
		this.adSource = adSource;
	}


	public String getAdSource() {
		return adSource;
	}


	public void setImpressionsCPM(long impressionsCPM) {
		this.impressionsCPM = impressionsCPM;
	}


	public long getImpressionsCPM() {
		return impressionsCPM;
	}


	public void setImpressionsCPC(long impressionsCPC) {
		this.impressionsCPC = impressionsCPC;
	}


	public long getImpressionsCPC() {
		return impressionsCPC;
	}


	public void setImpressionsCPD(long impressionsCPD) {
		this.impressionsCPD = impressionsCPD;
	}


	public long getImpressionsCPD() {
		return impressionsCPD;
	}


	public void setClicksCPM(long clicksCPM) {
		this.clicksCPM = clicksCPM;
	}


	public long getClicksCPM() {
		return clicksCPM;
	}


	public void setClicksCPC(long clicksCPC) {
		this.clicksCPC = clicksCPC;
	}


	public long getClicksCPC() {
		return clicksCPC;
	}


	public void setClicksCPD(long clicksCPD) {
		this.clicksCPD = clicksCPD;
	}


	public long getClicksCPD() {
		return clicksCPD;
	}


	public void setRevenueCPM(double revenueCPM) {
		this.revenueCPM = revenueCPM;
	}


	public double getRevenueCPM() {
		return revenueCPM;
	}


	public void setECPM(double eCPM) {
		ECPM = eCPM;
	}


	public double getECPM() {
		return ECPM;
	}


	public void setRevenueCPC(double revenueCPC) {
		this.revenueCPC = revenueCPC;
	}


	public double getRevenueCPC() {
		return revenueCPC;
	}


	public double getRPM() {
		return RPM;
	}


	public void setRPM(double rPM) {
		RPM = rPM;
	}


	public double getFillRate() {
		return fillRate;
	}


	public void setFillRate(double fillRate) {
		this.fillRate = fillRate;
	}


	public double getCTR() {
		return CTR;
	}


	public void setCTR(double cTR) {
		CTR = cTR;
	}


	public void setRevenueCPD(double revenueCPD) {
		this.revenueCPD = revenueCPD;
	}


	public double getRevenueCPD() {
		return revenueCPD;
	}


	public void setLineItemType(String lineItemType) {
		this.lineItemType = lineItemType;
	}


	public String getLineItemType() {
		return lineItemType;
	}


	public void setDeliveryIndicator(double deliveryIndicator) {
		this.deliveryIndicator = deliveryIndicator;
	}


	public double getDeliveryIndicator() {
		return deliveryIndicator;
	}


	public void setContractedQty(double contractedQty) {
		this.contractedQty = contractedQty;
	}


	public double getContractedQty() {
		return contractedQty;
	}


	public void setGoalQty(long goalQty) {
		this.goalQty = goalQty;
	}


	public long getGoalQty() {
		return goalQty;
	}
	
	
}
