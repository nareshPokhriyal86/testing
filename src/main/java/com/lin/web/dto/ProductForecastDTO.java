package com.lin.web.dto;

import java.io.Serializable;
import java.util.Date;

public class ProductForecastDTO implements Serializable{

	private Date startDate;
	private Date endDate;
	private long matchedImpressions;
	private long availableImpressions;		
	private long possibleImpressions;
	private long deliveredImpressions;
	private long reservedImpressions;
	private String adUnitId;
	private String adUnitName;
	
	public ProductForecastDTO() {
		
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ForcastProductDTO [startDate=").append(startDate)
				.append(", endDate=").append(endDate)
				.append(", matchedImpressions=").append(matchedImpressions)
				.append(", availableImpressions=").append(availableImpressions)
				.append(", possibleImpressions=").append(possibleImpressions)
				.append(", deliveredImpressions=").append(deliveredImpressions)
				.append(", reservedImpressions=").append(reservedImpressions)
				.append(", adUnitId=").append(adUnitId).append(", adUnitName=")
				.append(adUnitName).append("]");
		return builder.toString();
	}



	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startdate) {
		this.startDate = startdate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public long getMatchedImpressions() {
		return matchedImpressions;
	}
	public void setMatchedImpressions(long matchedImpressions) {
		this.matchedImpressions = matchedImpressions;
	}
	public long getAvailableImpressions() {
		return availableImpressions;
	}
	public void setAvailableImpressions(long availableImpressions) {
		this.availableImpressions = availableImpressions;
	}
	public long getPossibleImpressions() {
		return possibleImpressions;
	}
	public void setPossibleImpressions(long possibleImpressions) {
		this.possibleImpressions = possibleImpressions;
	}
	public long getDeliveredImpressions() {
		return deliveredImpressions;
	}
	public void setDeliveredImpressions(long deliveredImpressions) {
		this.deliveredImpressions = deliveredImpressions;
	}
	public long getReservedImpressions() {
		return reservedImpressions;
	}
	public void setReservedImpressions(long reservedImpressions) {
		this.reservedImpressions = reservedImpressions;
	}

	public String getAdUnitId() {
		return adUnitId;
	}

	public void setAdUnitId(String adUnitId) {
		this.adUnitId = adUnitId;
	}

	public String getAdUnitName() {
		return adUnitName;
	}

	public void setAdUnitName(String adUnitName) {
		this.adUnitName = adUnitName;
	}
	
	
	
}
