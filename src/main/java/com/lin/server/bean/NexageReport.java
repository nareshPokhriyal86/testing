package com.lin.server.bean;

public class NexageReport {
	
	private String createdOn;
	private long channelId;
	private String channelName;
	private String channelType;
	private String salesType;
	private long publisherId;
	private String publisherName;
	private String siteId;
	private String siteName;
	private String adSource;
	private String date;
	private long requests;
	private long served;
	private long totalImpressions;
	private long impressionsCPM;
	private long impressionsCPC;
	private long totalClicks;
	private long clicksCPM;
	private long clicksCPC;
	private double totalRevenue;
	private double revenueCPM;
	private double revenueCPC;
	private double revenueCPD;
	private double fillRate;
	private double CTR;
	private double ECPM;
	private double RPM;
	
	public NexageReport(){
		
	}
	public NexageReport(String createdOn, long channelId, String channelName,
			String channelType, String salesType, long publisherId,
			String publisherName, String siteId, String siteName,
			String adSource, String date, long requests, long served,
			long totalImpressions, long impressionsCPM, long impressionsCPC,
			long totalClicks, long clicksCPM, long clicksCPC,
			double totalRevenue, double revenueCPM, double revenueCPC,
			double revenueCPD, double fillRate, double cTR, double eCPM,
			double rPM) {
		this.createdOn = createdOn;
		this.channelId = channelId;
		this.channelName = channelName;
		this.channelType = channelType;
		this.salesType = salesType;
		this.publisherId = publisherId;
		this.publisherName = publisherName;
		this.siteId = siteId;
		this.siteName = siteName;
		this.adSource = adSource;
		this.date = date;
		this.requests = requests;
		this.served = served;
		this.totalImpressions = totalImpressions;
		this.impressionsCPM = impressionsCPM;
		this.impressionsCPC = impressionsCPC;
		this.totalClicks = totalClicks;
		this.clicksCPM = clicksCPM;
		this.clicksCPC = clicksCPC;
		this.totalRevenue = totalRevenue;
		this.revenueCPM = revenueCPM;
		this.revenueCPC = revenueCPC;
		this.revenueCPD = revenueCPD;
		this.fillRate = fillRate;
		CTR = cTR;
		ECPM = eCPM;
		RPM = rPM;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NexageReport [createdOn=").append(createdOn)
				.append(", channelId=").append(channelId)
				.append(", channelName=").append(channelName)
				.append(", channelType=").append(channelType)
				.append(", salesType=").append(salesType)
				.append(", publisherId=").append(publisherId)
				.append(", publisherName=").append(publisherName)
				.append(", siteId=").append(siteId).append(", siteName=")
				.append(siteName).append(", adSource=").append(adSource)
				.append(", date=").append(date).append(", requests=")
				.append(requests).append(", served=").append(served)
				.append(", totalImpressions=").append(totalImpressions)
				.append(", impressionsCPM=").append(impressionsCPM)
				.append(", impressionsCPC=").append(impressionsCPC)
				.append(", totalClicks=").append(totalClicks)
				.append(", clicksCPM=").append(clicksCPM)
				.append(", clicksCPC=").append(clicksCPC)
				.append(", totalRevenue=").append(totalRevenue)
				.append(", revenueCPM=").append(revenueCPM)
				.append(", revenueCPC=").append(revenueCPC)
				.append(", revenueCPD=").append(revenueCPD)
				.append(", fillRate=").append(fillRate).append(", CTR=")
				.append(CTR).append(", ECPM=").append(ECPM).append(", RPM=")
				.append(RPM).append("]");
		return builder.toString();
	}

	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
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
	public String getAdSource() {
		return adSource;
	}
	public void setAdSource(String adSource) {
		this.adSource = adSource;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
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
	public long getImpressionsCPM() {
		return impressionsCPM;
	}
	public void setImpressionsCPM(long impressionsCPM) {
		this.impressionsCPM = impressionsCPM;
	}
	public long getImpressionsCPC() {
		return impressionsCPC;
	}
	public void setImpressionsCPC(long impressionsCPC) {
		this.impressionsCPC = impressionsCPC;
	}
	public long getTotalClicks() {
		return totalClicks;
	}
	public void setTotalClicks(long totalClicks) {
		this.totalClicks = totalClicks;
	}
	public long getClicksCPM() {
		return clicksCPM;
	}
	public void setClicksCPM(long clicksCPM) {
		this.clicksCPM = clicksCPM;
	}
	public long getClicksCPC() {
		return clicksCPC;
	}
	public void setClicksCPC(long clicksCPC) {
		this.clicksCPC = clicksCPC;
	}
	public double getTotalRevenue() {
		return totalRevenue;
	}
	public void setTotalRevenue(double totalRevenue) {
		this.totalRevenue = totalRevenue;
	}
	public double getRevenueCPM() {
		return revenueCPM;
	}
	public void setRevenueCPM(double revenueCPM) {
		this.revenueCPM = revenueCPM;
	}
	public double getRevenueCPC() {
		return revenueCPC;
	}
	public void setRevenueCPC(double revenueCPC) {
		this.revenueCPC = revenueCPC;
	}
	public double getRevenueCPD() {
		return revenueCPD;
	}
	public void setRevenueCPD(double revenueCPD) {
		this.revenueCPD = revenueCPD;
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
	public double getECPM() {
		return ECPM;
	}
	public void setECPM(double eCPM) {
		ECPM = eCPM;
	}
	public double getRPM() {
		return RPM;
	}
	public void setRPM(double rPM) {
		RPM = rPM;
	}
	
}
