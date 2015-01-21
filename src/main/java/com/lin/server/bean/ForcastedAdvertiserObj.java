package com.lin.server.bean;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity
@Index
public class ForcastedAdvertiserObj implements Serializable {
	@Id	private String id;
	private String days;
	
	private float deliveredImpressions;
	private String startDate;
	private String endDate;
	private int clicks;
	private float ctr;
	private float revenueDelivered;
	private float revenueRemaining;
	
	public ForcastedAdvertiserObj(){
		
	}
	
	
	public ForcastedAdvertiserObj(String id, String days,
			float deliveredImpressions, String startDate, String endDate,
			int clicks, float ctr, float revenueDelivered,
			float revenueRemaining) {
		super();
		this.id = id;
		this.days = days;
		this.deliveredImpressions = deliveredImpressions;
		this.startDate = startDate;
		this.endDate = endDate;
		this.clicks = clicks;
		this.ctr = ctr;
		this.revenueDelivered = revenueDelivered;
		this.revenueRemaining = revenueRemaining;
	}


	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDays() {
		return days;
	}
	public void setDays(String days) {
		this.days = days;
	}
	public float getDeliveredImpressions() {
		return deliveredImpressions;
	}
	public void setDeliveredImpressions(float deliveredImpressions) {
		this.deliveredImpressions = deliveredImpressions;
	}
	public int getClicks() {
		return clicks;
	}
	public void setClicks(int clicks) {
		this.clicks = clicks;
	}
	public float getCtr() {
		return ctr;
	}
	public void setCtr(float ctr) {
		this.ctr = ctr;
	}
	public float getRevenueDelivered() {
		return revenueDelivered;
	}
	public void setRevenueDelivered(float revenueDelivered) {
		this.revenueDelivered = revenueDelivered;
	}
	public float getRevenueRemaining() {
		return revenueRemaining;
	}
	public void setRevenueRemaining(float revenueRemaining) {
		this.revenueRemaining = revenueRemaining;
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
