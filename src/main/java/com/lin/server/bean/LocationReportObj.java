package com.lin.server.bean;

public class LocationReportObj {
	
	private String loadTimestamp;	
	private String advertiser;
	private String order;
	private String lineItemType;
	private String lineItem;
	private String countryName;
	private String regionName;
	private String cityName;	
	private long advertiserId;
	private long orderId;
	private long lineitemId;
	private long countryCriteriaId;
	private long regionCriteriaId;
	private long cityCriteriaId;	
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
	private String date;
	private String costType;
	private double rate;
	private long goalQty;	
	private long orderLifetimeImpressions;
	private long lineitemLifetimeImpressions;
	private long ordeLifetimeClicks;
	private long lineitemLifetimeClicks;
	private long totalImpressions;	
	private long totalClicks;	
	private double ECPM;
	private double CTR;
	private double adserverCPMAndCPCRevenue;
	private double revenueCPD;
	private double totalRevenue;
	
	private long publisherId;
	private String publisherName;
	private String dataSource;
	
	public LocationReportObj(){
		
	}

	public String getLoadTimestamp() {
		return loadTimestamp;
	}

	public void setLoadTimestamp(String loadTimestamp) {
		this.loadTimestamp = loadTimestamp;
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

	public String getLineItemType() {
		return lineItemType;
	}

	public void setLineItemType(String lineItemType) {
		this.lineItemType = lineItemType;
	}

	public String getLineItem() {
		return lineItem;
	}

	public void setLineItem(String lineItem) {
		this.lineItem = lineItem;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
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

	public long getCountryCriteriaId() {
		return countryCriteriaId;
	}

	public void setCountryCriteriaId(long countryCriteriaId) {
		this.countryCriteriaId = countryCriteriaId;
	}

	public long getRegionCriteriaId() {
		return regionCriteriaId;
	}

	public void setRegionCriteriaId(long regionCriteriaId) {
		this.regionCriteriaId = regionCriteriaId;
	}

	public long getCityCriteriaId() {
		return cityCriteriaId;
	}

	public void setCityCriteriaId(long cityCriteriaId) {
		this.cityCriteriaId = cityCriteriaId;
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

	public long getGoalQty() {
		return goalQty;
	}

	public void setGoalQty(long goalQty) {
		this.goalQty = goalQty;
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

	public double getECPM() {
		return ECPM;
	}

	public void setECPM(double eCPM) {
		ECPM = eCPM;
	}

	public double getCTR() {
		return CTR;
	}

	public void setCTR(double cTR) {
		CTR = cTR;
	}

	public double getAdserverCPMAndCPCRevenue() {
		return adserverCPMAndCPCRevenue;
	}

	public void setAdserverCPMAndCPCRevenue(double adserverCPMAndCPCRevenue) {
		this.adserverCPMAndCPCRevenue = adserverCPMAndCPCRevenue;
	}

	public double getRevenueCPD() {
		return revenueCPD;
	}

	public void setRevenueCPD(double revenueCPD) {
		this.revenueCPD = revenueCPD;
	}

	public double getTotalRevenue() {
		return totalRevenue;
	}

	public void setTotalRevenue(double totalRevenue) {
		this.totalRevenue = totalRevenue;
	}

	public void setPublisherId(long publisherId) {
		this.publisherId = publisherId;
	}

	public long getPublisherId() {
		return publisherId;
	}

	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}

	public String getPublisherName() {
		return publisherName;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getDataSource() {
		return dataSource;
	}
	
	
	
}
