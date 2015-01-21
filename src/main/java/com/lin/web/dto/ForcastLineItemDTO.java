package com.lin.web.dto;

import java.io.Serializable;

public class ForcastLineItemDTO implements Serializable {
	private Long lineItemId;
	private Long availableUnit;
	private Long deliveredUnit;
	private Long possibleUnit;
	private Long matchedUnit;
	private boolean status;
	private String ECPM;
	private String lineItem;
	private String bookedImpressions;
	private String 	startDate;
	private String 	endDate;
	private String archived;
	
	public ForcastLineItemDTO() {
		
	}

	public ForcastLineItemDTO(Long lineItemId, Long availableUnit,
			Long deliveredUnit, Long possibleUnit, Long matchedUnit,
			boolean status, String eCPM, String lineItem,
			String bookedImpressions, String startDate) {
		super();
		this.lineItemId = lineItemId;
		this.availableUnit = availableUnit;
		this.deliveredUnit = deliveredUnit;
		this.possibleUnit = possibleUnit;
		this.matchedUnit = matchedUnit;
		this.status = status;
		ECPM = eCPM;
		this.lineItem = lineItem;
		this.bookedImpressions = bookedImpressions;
		this.startDate = startDate;
	}




	public Long getLineItemId() {
		return lineItemId;
	}
	public void setLineItemId(Long lineItemId) {
		this.lineItemId = lineItemId;
	}
	public Long getAvailableUnit() {
		return availableUnit;
	}
	public void setAvailableUnit(Long availableUnit) {
		this.availableUnit = availableUnit;
	}
	public Long getDeliveredUnit() {
		return deliveredUnit;
	}
	public void setDeliveredUnit(Long deliveredUnit) {
		this.deliveredUnit = deliveredUnit;
	}
	public Long getPossibleUnit() {
		return possibleUnit;
	}
	public void setPossibleUnit(Long possibleUnit) {
		this.possibleUnit = possibleUnit;
	}
	public Long getMatchedUnit() {
		return matchedUnit;
	}
	public void setMatchedUnit(Long matchedUnit) {
		this.matchedUnit = matchedUnit;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public boolean isStatus() {
		return status;
	}



	public void setECPM(String eCPM) {
		ECPM = eCPM;
	}



	public String getECPM() {
		return ECPM;
	}



	public void setLineItem(String lineItem) {
		this.lineItem = lineItem;
	}



	public String getLineItem() {
		return lineItem;
	}



	public void setBookedImpressions(String bookedImpressions) {
		this.bookedImpressions = bookedImpressions;
	}



	public String getBookedImpressions() {
		return bookedImpressions;
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

	public String getArchived() {
		return archived;
	}

	public void setArchived(String archived) {
		this.archived = archived;
	}

}
