package com.lin.server.bean;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;


@Entity

@SuppressWarnings("serial")
public class OrderLineItemObj implements Serializable{

	@Id	private String id;
	private long orderId;
	private String orderName;
	private long lineItemId;
	
	private String lineItemName;
	
	
	private long impressionsLifeTime;
	private long clicksLifeTimes;
	private double cpm;
	private double ctr;
	private double revenueDelivered;
	private double revenueRemaining;
	
	private long impressionsBooked;
	private long clicks;
	private double totalBudget;
	
	private String startDate;
	private String endDate;
	
    public OrderLineItemObj(){
		
	}
	
	
	
	public OrderLineItemObj(String id, long orderId, String orderName,
			long lineItemId, String lineItemName, long impressionsLifeTime,
			long clicksLifeTimes, double cpm, double ctr,
			double revenueDelivered, double revenueRemaining,
			long impressionsBooked, long clicks, double totalBudget) {
		this.id = id;
		this.orderId = orderId;
		this.orderName = orderName;
		this.lineItemId = lineItemId;
		this.lineItemName = lineItemName;
		this.impressionsLifeTime = impressionsLifeTime;
		this.clicksLifeTimes = clicksLifeTimes;
		this.cpm = cpm;
		this.ctr = ctr;
		this.revenueDelivered = revenueDelivered;
		this.revenueRemaining = revenueRemaining;
		this.impressionsBooked = impressionsBooked;
		this.clicks = clicks;
		this.totalBudget = totalBudget;
	}



	public double getCpm() {
		return cpm;
	}
	public void setCpm(double cpm) {
		this.cpm = cpm;
	}
	public double getCtr() {
		return ctr;
	}
	public void setCtr(double ctr) {
		this.ctr = ctr;
	}
	public double getRevenueDelivered() {
		return revenueDelivered;
	}
	public void setRevenueDelivered(double revenueDelivered) {
		this.revenueDelivered = revenueDelivered;
	}
	public double getRevenueRemaining() {
		return revenueRemaining;
	}
	public void setRevenueRemaining(double revenueRemaining) {
		this.revenueRemaining = revenueRemaining;
	}
	
	public double getTotalBudget() {
		return totalBudget;
	}
	public void setTotalBudget(double totalBudget) {
		this.totalBudget = totalBudget;
	}
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public long getLineItemId() {
		return lineItemId;
	}

	public void setLineItemId(long lineItemId) {
		this.lineItemId = lineItemId;
	}

	public String getLineItemName() {
		return lineItemName;
	}

	public void setLineItemName(String lineItemName) {
		this.lineItemName = lineItemName;
	}
	public void setImpressionsLifeTime(long impressionsLifeTime) {
		this.impressionsLifeTime = impressionsLifeTime;
	}
	public long getImpressionsLifeTime() {
		return impressionsLifeTime;
	}
	public void setClicksLifeTimes(long clicksLifeTimes) {
		this.clicksLifeTimes = clicksLifeTimes;
	}
	public long getClicksLifeTimes() {
		return clicksLifeTimes;
	}
	public void setImpressionsBooked(long impressionsBooked) {
		this.impressionsBooked = impressionsBooked;
	}
	public long getImpressionsBooked() {
		return impressionsBooked;
	}
	public void setClicks(long clicks) {
		this.clicks = clicks;
	}
	public long getClicks() {
		return clicks;
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
