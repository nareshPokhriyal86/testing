package com.lin.server.bean;

import java.io.Serializable;
import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
/*
 * It is a Alert Engine Flight entity bean for datastore to save Flight Lavel daily alerts for campaign performance
 *  
 * @author Shubham Goel
 */
@Index
public class AlertEngineFlightObj implements Serializable {
	@Id	private Long id;
	private long placementId; 
	private int flightId;
	
	//Delivery Alert Values
	private long deliveryExpected;
	private long deliveryCurent;
	private int deliveyAlert;
	private int pacingAlert;
	private Date alertDate;
	private String startDate;
	private String endDate;
	private long flightDailyPacingCurrent;
	private long flightDailyPacingExpected;
	private long flightDailyPacingRevised;
	private long flightGoal;	
	
	public AlertEngineFlightObj() {
		
	}



	public long getPlacementId() {
		return placementId;
	}

	public void setPlacementId(long placementId) {
		this.placementId = placementId;
	}

	public long getDeliveryExpected() {
		return deliveryExpected;
	}

	public void setDeliveryExpected(long deliveryExpected) {
		this.deliveryExpected = deliveryExpected;
	}

	public long getDeliveryCurent() {
		return deliveryCurent;
	}

	public void setDeliveryCurent(long deliveryCurent) {
		this.deliveryCurent = deliveryCurent;
	}

	public int getDeliveyAlert() {
		return deliveyAlert;
	}

	public void setDeliveyAlert(int deliveyAlert) {
		this.deliveyAlert = deliveyAlert;
	}

	public Date getAlertDate() {
		return alertDate;
	}

	public void setAlertDate(Date alertDate) {
		this.alertDate = alertDate;
	}



	public int getFlightId() {
		return flightId;
	}



	public void setFlightId(int flightId) {
		this.flightId = flightId;
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



	public int getPacingAlert() {
		return pacingAlert;
	}



	public void setPacingAlert(int pacingAlert) {
		this.pacingAlert = pacingAlert;
	}



	public long getFlightDailyPacingCurrent() {
		return flightDailyPacingCurrent;
	}



	public void setFlightDailyPacingCurrent(long flightDailyPacingCurrent) {
		this.flightDailyPacingCurrent = flightDailyPacingCurrent;
	}



	public long getFlightDailyPacingExpected() {
		return flightDailyPacingExpected;
	}



	public void setFlightDailyPacingExpected(long flightDailyPacingExpected) {
		this.flightDailyPacingExpected = flightDailyPacingExpected;
	}



	public long getFlightDailyPacingRevised() {
		return flightDailyPacingRevised;
	}



	public void setFlightDailyPacingRevised(long flightDailyPacingRevised) {
		this.flightDailyPacingRevised = flightDailyPacingRevised;
	}



	public long getFlightGoal() {
		return flightGoal;
	}



	public void setFlightGoal(long flightGoal) {
		this.flightGoal = flightGoal;
	}
	
	
	
}
