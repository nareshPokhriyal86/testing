package com.lin.server.bean;

/*
 * This is pojo file for DFPTargetReportObj
 * 
 * @author Youdhveer Panwar
 */
public class DFPTargetReportObj {

	private String loadTimestamp;
	private String date;
	private long publisherId;
	private String publisherName;
	private String advertiser;
	private String order;
	private String lineItemType;
	private String lineItem;	
	private long advertiserId;
	private long orderId;
	private long lineItemId;
	private long requests;
	private long served;
	private long totalImpressions;
	private long totalClicks;
	private double totalRevenue;
	private double adserverCPMAndCPCRevenue;
	private double ECPM;
	private double fillRate;
	private double CTR;
	private double deliveryIndicator;
	private String dataSource;
	private String targetCategory;
	private String targetValue;
	
		
	public DFPTargetReportObj(){
		
	}


	public String getLoadTimestamp() {
		return loadTimestamp;
	}


	public void setLoadTimestamp(String loadTimestamp) {
		this.loadTimestamp = loadTimestamp;
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public long getPublisherId() {
		return publisherId;
	}


	public void setPublisherId(long publisherId) {
		this.publisherId = publisherId;
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


	public double getECPM() {
		return ECPM;
	}


	public void setECPM(double eCPM) {
		ECPM = eCPM;
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


	public double getDeliveryIndicator() {
		return deliveryIndicator;
	}


	public void setDeliveryIndicator(double deliveryIndicator) {
		this.deliveryIndicator = deliveryIndicator;
	}


	public String getDataSource() {
		return dataSource;
	}


	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}


	public String getTargetCategory() {
		return targetCategory;
	}


	public void setTargetCategory(String targetCategory) {
		this.targetCategory = targetCategory;
	}


	public String getTargetValue() {
		return targetValue;
	}


	public void setTargetValue(String targetValue) {
		this.targetValue = targetValue;
	}
	
	
	
	
}
