package com.lin.web.dto;

import java.util.Date;


public class PublisherReportHeaderDTO {

	private String publisherName;	
	private String channels;
	private Date startDate;
	private Date endDate;
	private Date compareStartDate;
	private Date compareEndDate;
	private long site;
	private long impressionsDelivered;
	private long clicks;
	private double ctrPercentage;
	private double ecpm;
	private double rpm;
	private double payouts;
	private double fillPercentage; 
	
	public PublisherReportHeaderDTO() {
	}

	public PublisherReportHeaderDTO(String publisherName, String channels,
			Date startDate, Date endDate, Date compareStartDate,
			Date compareEndDate, long site, long impressionsDelivered,
			long clicks, double ctrPercentage, double ecpm, double rpm,
			double payouts, double fillPercentage) {
		this.publisherName = publisherName;
		this.channels = channels;
		this.startDate = startDate;
		this.endDate = endDate;
		this.compareStartDate = compareStartDate;
		this.compareEndDate = compareEndDate;
		this.site = site;
		this.impressionsDelivered = impressionsDelivered;
		this.clicks = clicks;
		this.ctrPercentage = ctrPercentage;
		this.ecpm = ecpm;
		this.rpm = rpm;
		this.payouts = payouts;
		this.fillPercentage = fillPercentage;
	}

	@Override
	public String toString() {
		return "PublisherReportHeaderDTO [publisherName=" + publisherName
				+ ", channels=" + channels + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", compareStartDate="
				+ compareStartDate + ", compareEndDate=" + compareEndDate
				+ ", site=" + site + ", impressionsDelivered="
				+ impressionsDelivered + ", clicks=" + clicks
				+ ", ctrPercentage=" + ctrPercentage + ", ecpm=" + ecpm
				+ ", rpm=" + rpm + ", payouts=" + payouts
				+ ", fillPercentage=" + fillPercentage + "]";
	}

	public String getPublisherName() {
		return publisherName;
	}

	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}

	public String getChannels() {
		return channels;
	}

	public void setChannels(String channels) {
		this.channels = channels;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getCompareStartDate() {
		return compareStartDate;
	}

	public void setCompareStartDate(Date compareStartDate) {
		this.compareStartDate = compareStartDate;
	}

	public Date getCompareEndDate() {
		return compareEndDate;
	}

	public void setCompareEndDate(Date compareEndDate) {
		this.compareEndDate = compareEndDate;
	}

	public long getSite() {
		return site;
	}

	public void setSite(long site) {
		this.site = site;
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

	public double getEcpm() {
		return ecpm;
	}

	public void setEcpm(double ecpm) {
		this.ecpm = ecpm;
	}

	public double getRpm() {
		return rpm;
	}

	public void setRpm(double rpm) {
		this.rpm = rpm;
	}

	public double getPayouts() {
		return payouts;
	}

	public void setPayouts(double payouts) {
		this.payouts = payouts;
	}

	public double getFillPercentage() {
		return fillPercentage;
	}

	public void setFillPercentage(double fillPercentage) {
		this.fillPercentage = fillPercentage;
	}
	
	
}
