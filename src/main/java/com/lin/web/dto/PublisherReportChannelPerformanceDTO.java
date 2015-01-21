package com.lin.web.dto;

public class PublisherReportChannelPerformanceDTO {

	private String salesChannel;
	private double ecpm;
	private double change;
	private double changePercentage;
	private long impressionsDelivered;
	private long clicks;
	private double ctrPercentage;
	private double payouts;
	private double fillRate;
	
	public PublisherReportChannelPerformanceDTO() {
	}

	public PublisherReportChannelPerformanceDTO(String salesChannel,
			double ecpm, double change, double changePercentage,
			long impressionsDelivered, long clicks, double ctrPercentage,
			double payouts, double fillRate) {
		this.salesChannel = salesChannel;
		this.ecpm = ecpm;
		this.change = change;
		this.changePercentage = changePercentage;
		this.impressionsDelivered = impressionsDelivered;
		this.clicks = clicks;
		this.ctrPercentage = ctrPercentage;
		this.payouts = payouts;
		this.fillRate = fillRate;
	}

	@Override
	public String toString() {
		return "PublisherReportChannelPerformanceDTO [salesChannel="
				+ salesChannel + ", ecpm=" + ecpm + ", change=" + change
				+ ", changePercentage=" + changePercentage
				+ ", impressionsDelivered=" + impressionsDelivered
				+ ", clicks=" + clicks + ", ctrPercentage=" + ctrPercentage
				+ ", payouts=" + payouts + ", fillRate=" + fillRate + "]";
	}

	public String getSalesChannel() {
		return salesChannel;
	}

	public void setSalesChannel(String salesChannel) {
		this.salesChannel = salesChannel;
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

	public double getCtrPercentage() {
		return ctrPercentage;
	}

	public void setCtrPercentage(double ctrPercentage) {
		this.ctrPercentage = ctrPercentage;
	}

	public double getPayouts() {
		return payouts;
	}

	public void setPayouts(double payouts) {
		this.payouts = payouts;
	}

	public double getFillRate() {
		return fillRate;
	}

	public void setFillRate(double fillRate) {
		this.fillRate = fillRate;
	}

	
	
	
}
