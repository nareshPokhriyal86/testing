package com.lin.server.bean;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity
@Index
public class CustomLineItemObj implements Serializable{
	
	@Id private Long id;
	private String lineItemName;
	private double CTR;
	private double changeInSelectedTime;
	private double changeLifeTime;
	private long deliveredImpressions;
	private double deliveryIndicator;
	private String date;
	
	public CustomLineItemObj(){
		
	}
	
	
	
	public CustomLineItemObj(Long id, String lineItemName, double cTR,
			double changeInSelectedTime, double changeLifeTime,
			long deliveredImpressions, double deliveryIndicator, String date) {
		this.id = id;
		this.lineItemName = lineItemName;
		CTR = cTR;
		this.changeInSelectedTime = changeInSelectedTime;
		this.changeLifeTime = changeLifeTime;
		this.deliveredImpressions = deliveredImpressions;
		this.deliveryIndicator = deliveryIndicator;
		this.date = date;
	}

	public CustomLineItemObj(String lineItemName, double cTR,
			double changeInSelectedTime, double changeLifeTime,
			long deliveredImpressions, double deliveryIndicator, String date) {		
		this.lineItemName = lineItemName;
		CTR = cTR;
		this.changeInSelectedTime = changeInSelectedTime;
		this.changeLifeTime = changeLifeTime;
		this.deliveredImpressions = deliveredImpressions;
		this.deliveryIndicator = deliveryIndicator;
		this.date = date;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getLineItemName() {
		return lineItemName;
	}
	public void setLineItemName(String lineItemName) {
		this.lineItemName = lineItemName;
	}
	public double getCTR() {
		return CTR;
	}
	public void setCTR(double CTR) {
		this.CTR = CTR;
	}
	public double getChangeInSelectedTime() {
		return changeInSelectedTime;
	}
	public void setChangeInSelectedTime(double changeInSelectedTime) {
		this.changeInSelectedTime = changeInSelectedTime;
	}
	public double getChangeLifeTime() {
		return changeLifeTime;
	}
	public void setChangeLifeTime(double changeLifeTime) {
		this.changeLifeTime = changeLifeTime;
	}
	public long getDeliveredImpressions() {
		return deliveredImpressions;
	}
	public void setDeliveredImpressions(long deliveredImpressions) {
		this.deliveredImpressions = deliveredImpressions;
	}
	public double getDeliveryIndicator() {
		return deliveryIndicator;
	}
	public void setDeliveryIndicator(double deliveryIndicator) {
		this.deliveryIndicator = deliveryIndicator;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDate() {
		return date;
	}
	
	
	

}
