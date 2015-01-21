package com.lin.server.bean;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity
@Index
public class ActualPublisherObj implements Serializable {
	@Id	private String id;
	private String date;
	private long request;
	private long served;
	private long delivered;
	private double fillRate;
	private long clicks;
	private double ctr;
	private double revenue;
	private double ecpm;
	private double rpm;
	private String publisher;
	private String channel;
	
	public ActualPublisherObj(){
		
	}
	
	


	public ActualPublisherObj(String id, String date, long request,
			long served, long delivered, double fillRate, long clicks,
			double ctr, double revenue, double ecpm, double rpm,
			String publisher, String channel) {
		super();
		this.id = id;
		this.date = date;
		this.request = request;
		this.served = served;
		this.delivered = delivered;
		this.fillRate = fillRate;
		this.clicks = clicks;
		this.ctr = ctr;
		this.revenue = revenue;
		this.ecpm = ecpm;
		this.rpm = rpm;
		this.publisher = publisher;
		this.channel = channel;
	}




	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public long getRequest() {
		return request;
	}
	public void setRequest(long request) {
		this.request = request;
	}
	public long getDelivered() {
		return delivered;
	}
	public void setDelivered(long delivered) {
		this.delivered = delivered;
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
	public double getCtr() {
		return ctr;
	}
	public void setCtr(double ctr) {
		this.ctr = ctr;
	}
	public double getRevenue() {
		return revenue;
	}
	public void setRevenue(double revenue) {
		this.revenue = revenue;
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

	public void setDate(String date) {
		this.date = date;
	}

	public String getDate() {
		return date;
	}

	public void setServed(long served) {
		this.served = served;
	}

	public long getServed() {
		return served;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getChannel() {
		return channel;
	}
	
}
