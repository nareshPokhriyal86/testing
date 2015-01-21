package com.lin.web.dto;

import java.io.Serializable;

import com.lin.web.util.StringUtil;

public class SmartCampaignFlightDTO implements Serializable{

	private String flightName;
	private String startDate;
	private String endDate; 
	private String goal;
	private int flightId;
	
	public SmartCampaignFlightDTO() {
	}
	

	public SmartCampaignFlightDTO(String flightName, String startDate,
			String endDate, String goal, int flightId) {
		this.flightName = flightName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.goal = goal;
		this.flightId = flightId;
	}
	
	public SmartCampaignFlightDTO(String flightName, String startDate,
			String endDate, String goal) {
		this.flightName = flightName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.goal = goal;
	}


	@Override
	public String toString() {
		return "SmartCampaignFlightDTO [startDate=" + startDate + ", endDate=" + endDate
				+ ", goal=" + goal + ", flightId=" + flightId + "]";
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

	public int getFlightId() {
		return flightId;
	}

	public void setFlightId(int flightId) {
		this.flightId = flightId;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((goal == null) ? 0 : goal.hashCode());
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SmartCampaignFlightDTO other = (SmartCampaignFlightDTO) obj;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (goal == null) {
			if (other.goal != null)
				return false;
		} else if (!StringUtil.removeMediaPlanFormatters(goal).equals(StringUtil.removeMediaPlanFormatters(other.goal)))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		return true;
	}


	
	
	
	
	
}
