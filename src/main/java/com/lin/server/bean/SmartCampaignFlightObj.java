package com.lin.server.bean;

import java.io.Serializable;
import java.util.List;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Parent;

@SuppressWarnings("serial")
@Entity
@Index
public class SmartCampaignFlightObj implements Serializable {
	
	@Id	private Long id;
	@Parent
	private Key<SmartCampaignPlacementObj> placementId;
	private Long flightid; 
	private String flightName;
	private String startDate;
	private String endDate; 
	private String goal;
	

	
	public SmartCampaignFlightObj() {
	}

	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public Key<SmartCampaignPlacementObj> getPlacementId() {
		return placementId;
	}



	public void setPlacementId(Key<SmartCampaignPlacementObj> placementId) {
		this.placementId = placementId;
	}



	public String getFlightName() {
		return flightName;
	}



	public void setFlightName(String flightName) {
		this.flightName = flightName;
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



	public String getGoal() {
		return goal;
	}



	public void setGoal(String goal) {
		this.goal = goal;
	}



	public Long getFlightid() {
		return flightid;
	}



	public void setFlightid(Long flightid) {
		this.flightid = flightid;
	}
	
	

}
