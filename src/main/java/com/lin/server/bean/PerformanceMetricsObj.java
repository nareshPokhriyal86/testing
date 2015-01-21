package com.lin.server.bean;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity
@Index
public class PerformanceMetricsObj implements Serializable{

	@Id private String id;
	private String date;
	private String advertiserName;
	private String lineItemName;
	private String startDate;
	private String endDate;
	private long impressionsBooked;
	private long impressionsDelivered;
	private long clicks;
	private double CTR ;
	private double budget;
	private double revenueRecoByDay;
	private double revenueLeftByDay;
	
	public PerformanceMetricsObj(){
		
	}
	
	public PerformanceMetricsObj(String id, String date, String advertiserName,
			String lineItemName, String startDate, String endDate,
			long impressionsBooked, long impressionsDelivered, long clicks,
			double cTR, double budget, double revenueRecoByDay,
			double revenueLeftByDay) {
		this.id = id;
		this.date = date;
		this.advertiserName = advertiserName;
		this.lineItemName = lineItemName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.impressionsBooked = impressionsBooked;
		this.impressionsDelivered = impressionsDelivered;
		this.clicks = clicks;
		CTR = cTR;
		this.budget = budget;
		this.revenueRecoByDay = revenueRecoByDay;
		this.revenueLeftByDay = revenueLeftByDay;
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
	public String getAdvertiserName() {
		return advertiserName;
	}
	public void setAdvertiserName(String advertiserName) {
		this.advertiserName = advertiserName;
	}
	public String getLineItemName() {
		return lineItemName;
	}
	public void setLineItemName(String lineItemName) {
		this.lineItemName = lineItemName;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public long getImpressionsBooked() {
		return impressionsBooked;
	}
	public void setImpressionsBooked(long impressionsBooked) {
		this.impressionsBooked = impressionsBooked;
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
	public double getCTR() {
		return CTR;
	}
	public void setCTR(double cTR) {
		CTR = cTR;
	}
	public double getBudget() {
		return budget;
	}
	public void setBudget(double budget) {
		this.budget = budget;
	}
	public double getRevenueRecoByDay() {
		return revenueRecoByDay;
	}
	public void setRevenueRecoByDay(double revenueRecoByDay) {
		this.revenueRecoByDay = revenueRecoByDay;
	}
	public double getRevenueLeftByDay() {
		return revenueLeftByDay;
	}
	public void setRevenueLeftByDay(double revenueLeftByDay) {
		this.revenueLeftByDay = revenueLeftByDay;
	}
	
	
}
