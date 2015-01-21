package com.lin.web.dto;


public class PublisherReportPerformanceByPropertyDTO {

	private String property;
	private double ecpm;
	private double change;
	private double changePercentage;
	private long impressionsDelivered;
	private long clicks;
	private double payouts;
	
	public PublisherReportPerformanceByPropertyDTO() {
	}

	public PublisherReportPerformanceByPropertyDTO(String property,
			double ecpm, double change, double changePercentage,
			long impressionsDelivered, long clicks,
			double payouts) {
		this.property = property;
		this.ecpm = ecpm;
		this.change = change;
		this.changePercentage = changePercentage;
		this.impressionsDelivered = impressionsDelivered;
		this.clicks = clicks;
		this.payouts = payouts;
	}

	@Override
	public String toString() {
		return "PublisherReportChannelPerformanceDTO [property="
				+ property + ", ecpm=" + ecpm + ", change=" + change
				+ ", changePercentage=" + changePercentage
				+ ", impressionsDelivered=" + impressionsDelivered
				+ ", clicks=" + clicks
				+ ", payouts=" + payouts + "]";
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public double getEcpm() {
		return ecpm;
	}

	public void setEcpm(double ecpm) {
		this.ecpm = ecpm;
	}

	public double getChange() {
		return change;
	}

	public void setChange(double change) {
		this.change = change;
	}

	public double getChangePercentage() {
		return changePercentage;
	}

	public void setChangePercentage(double changePercentage) {
		this.changePercentage = changePercentage;
	}

	public long getImpressionsDelivered() {
		return impressionsDelivered;
	}

	public void setImpressionsDelivered(long impressionsDelivered) {
		this.impressionsDelivered = impressionsDelivered;
	}

	public long getClicks() {
		return clicks;
	}

	public void setClicks(long clicks) {
		this.clicks = clicks;
	}

	public double getPayouts() {
		return payouts;
	}

	public void setPayouts(double payouts) {
		this.payouts = payouts;
	}

	
	
	
}
