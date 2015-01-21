package com.lin.server.bean;

import java.io.Serializable;
import java.util.Date;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity
@SuppressWarnings("serial")
@Index
public class PublisherChannelObj implements Serializable{
	@Id	private String id;
	private String date;
	private long requests;
	private long served;
	private long lastImpressionsDelivered;
	private long impressionsDelivered;
	private double fillRate;
	private long clicks;
	private double CTR;
	private double lastPayout;
	private double revenue;
	private double eCPM;
	private double RPM;
	private String publisherName;
	private String channelName;
	private double CHG;
	private double percentageCHG;	
	private double payout;
	private double CPC;
	private double floorCPM;
	private String timePeriod;
	private Date reportDate;
	
	public PublisherChannelObj(){
		
	}
	public PublisherChannelObj(String id, String date, long requests,
			long served, long impressionsDelivered, double fillRate,
			long clicks, double cTR, double revenue, double eCPM, double rPM,
			String publisherName, String channelName, double cHG,
			double percentageCHG, double payout) {
		this.id = id;
		this.date = date;
		this.requests = requests;
		this.served = served;
		this.impressionsDelivered = impressionsDelivered;
		this.fillRate = fillRate;
		this.clicks = clicks;
		CTR = cTR;
		this.revenue = revenue;
		this.eCPM = eCPM;
		RPM = rPM;
		this.setPublisherName(publisherName);
		this.channelName = channelName;
		CHG = cHG;
		this.percentageCHG = percentageCHG;
		this.payout = payout;
	}
	
	public PublisherChannelObj(String id,String channelName,double eCPM, double lastPayout,long lastImpressionsDelivered, double CTR,double cHG,double percentageCHG,long impressionsDelivered,long clicks,double payout) {
		this.id = id;
		this.channelName = channelName;
		this.eCPM = eCPM;
		this.CHG = cHG;
		this.percentageCHG = percentageCHG;
		this.impressionsDelivered = impressionsDelivered;
		this.clicks = clicks;
		this.payout = payout;
		this.CTR = CTR;
		this.lastImpressionsDelivered = lastImpressionsDelivered;
		this.lastPayout = lastPayout;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public long getImpressionsDelivered() {
		return impressionsDelivered;
	}
	public void setImpressionsDelivered(long impressionsDelivered) {
		this.impressionsDelivered = impressionsDelivered;
	}
	public double getFillRate() {
		return fillRate;
	}
	public void setFillRate(double fillRate) {
		this.fillRate = fillRate;
	}
	public long getClicks() {
		return clicks;
	}
	public void setClicks(long clicks) {
		this.clicks = clicks;
	}
	public double getCTR() {
		return CTR;
	}
	public void setCTR(double cTR) {
		CTR = cTR;
	}
	public double getRevenue() {
		return revenue;
	}
	public void setRevenue(double revenue) {
		this.revenue = revenue;
	}
	public double geteCPM() {
		return eCPM;
	}
	public void seteCPM(double eCPM) {
		this.eCPM = eCPM;
	}
	public double getRPM() {
		return RPM;
	}
	public void setRPM(double rPM) {
		RPM = rPM;
	}
	
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public double getCHG() {
		return CHG;
	}
	public void setCHG(double cHG) {
		CHG = cHG;
	}
	public double getPercentageCHG() {
		return percentageCHG;
	}
	public void setPercentageCHG(double percentageCHG) {
		this.percentageCHG = percentageCHG;
	}
	public double getPayout() {
		return payout;
	}
	public void setPayout(double payout) {
		this.payout = payout;
	}
	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}
	public String getPublisherName() {
		return publisherName;
	}
	public void setCPC(double cPC) {
		CPC = cPC;
	}
	public double getCPC() {
		return CPC;
	}
	public void setFloorCPM(double floorCPM) {
		this.floorCPM = floorCPM;
	}
	public double getFloorCPM() {
		return floorCPM;
	}
	public void setTimePeriod(String timePeriod) {
		this.timePeriod = timePeriod;
	}
	public String getTimePeriod() {
		return timePeriod;
	}
	public long getLastImpressionsDelivered() {
		return lastImpressionsDelivered;
	}
	public void setLastImpressionsDelivered(long lastImpressionsDelivered) {
		this.lastImpressionsDelivered = lastImpressionsDelivered;
	}
	public double getLastPayout() {
		return lastPayout;
	}
	public void setLastPayout(double lastPayout) {
		this.lastPayout = lastPayout;
	}
	public Date getReportDate() {
		return reportDate;
	}
	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}
	

}
