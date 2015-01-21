package com.lin.server.bean;

public class SellThroughReportObj {

	private String loadTimestamp;
	private String frequency;
	private String siteName;
	private String creativeSize;	
	private String startDate;
	private String endDate;
	private String siteID;
	private long forecastedImpressions;
	private long availableImpressions;
	private long reservedImpressions;
	private double sellThroughRate;
	private String dataSource;
	private String publisherId;
	private String publisherName;
	
	public SellThroughReportObj(){
		
	}

	
	
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getCreativeSize() {
		return creativeSize;
	}
	public void setCreativeSize(String creativeSize) {
		this.creativeSize = creativeSize;
	}
	
	public String getSiteID() {
		return siteID;
	}
	public void setSiteID(String siteID) {
		this.siteID = siteID;
	}
	public long getForecastedImpressions() {
		return forecastedImpressions;
	}
	public void setForecastedImpressions(long forecastedImpressions) {
		this.forecastedImpressions = forecastedImpressions;
	}
	public long getAvailableImpressions() {
		return availableImpressions;
	}
	public void setAvailableImpressions(long availableImpressions) {
		this.availableImpressions = availableImpressions;
	}
	public long getReservedImpressions() {
		return reservedImpressions;
	}
	public void setReservedImpressions(long reservedImpressions) {
		this.reservedImpressions = reservedImpressions;
	}
	public double getSellThroughRate() {
		return sellThroughRate;
	}
	public void setSellThroughRate(double sellThroughRate) {
		this.sellThroughRate = sellThroughRate;
	}



	public void setLoadTimestamp(String loadTimestamp) {
		this.loadTimestamp = loadTimestamp;
	}



	public String getLoadTimestamp() {
		return loadTimestamp;
	}



	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}



	public String getDataSource() {
		return dataSource;
	}



	public void setPublisherId(String publisherId) {
		this.publisherId = publisherId;
	}



	public String getPublisherId() {
		return publisherId;
	}



	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}



	public String getPublisherName() {
		return publisherName;
	}



	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}



	public String getStartDate() {
		return startDate;
	}



	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}



	public String getEndDate() {
		return endDate;
	}
	
	
}
