package com.lin.web.dto;

import java.io.Serializable;

public class ForcastDTO implements Serializable{

	private long matchedImpressions;
	private long availableImpressions;		
	private long possibleImpressions;
	private long deliveredImpressions;
	private long reservedImpressions;
	
	private long matchedClicks;
	private long availableClicks;		
	private long possibleClicks;
	private long deliveredClicks;
	private long reservedClicks;
	
	private long orderId;
	
	private String unitType;
	private String publisherId;
	private String adUnitId;
	private String adUnitName;
	private String publisherName;
	
	public ForcastDTO(){
		
	}

	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ForcastDTO [matchedImpressions=")
				.append(matchedImpressions).append(", availableImpressions=")
				.append(availableImpressions).append(", possibleImpressions=")
				.append(possibleImpressions).append(", deliveredImpressions=")
				.append(deliveredImpressions).append(", reservedImpressions=")
				.append(reservedImpressions).append(", matchedClicks=")
				.append(matchedClicks).append(", availableClicks=")
				.append(availableClicks).append(", possibleClicks=")
				.append(possibleClicks).append(", deliveredClicks=")
				.append(deliveredClicks).append(", reservedClicks=")
				.append(reservedClicks).append(", orderId=").append(orderId)
				.append(", unitType=").append(unitType).append("]");
		return builder.toString();
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

	public long getMatchedClicks() {
		return matchedClicks;
	}

	public void setMatchedClicks(long matchedClicks) {
		this.matchedClicks = matchedClicks;
	}

	public long getAvailableClicks() {
		return availableClicks;
	}

	public void setAvailableClicks(long availableClicks) {
		this.availableClicks = availableClicks;
	}

	public long getPossibleClicks() {
		return possibleClicks;
	}

	public void setPossibleClicks(long possibleClicks) {
		this.possibleClicks = possibleClicks;
	}

	public long getDeliveredClicks() {
		return deliveredClicks;
	}

	public void setDeliveredClicks(long deliveredClicks) {
		this.deliveredClicks = deliveredClicks;
	}

	public long getReservedClicks() {
		return reservedClicks;
	}

	public void setReservedClicks(long reservedClicks) {
		this.reservedClicks = reservedClicks;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public String getUnitType() {
		return unitType;
	}

	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}


	public String getPublisherId() {
		return publisherId;
	}


	public void setPublisherId(String publisherId) {
		this.publisherId = publisherId;
	}


	
	public String getPublisherName() {
		return publisherName;
	}


	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}


	public String getAdUnitName() {
		return adUnitName;
	}


	public void setAdUnitName(String adUnitName) {
		this.adUnitName = adUnitName;
	}


	public String getAdUnitId() {
		return adUnitId;
	}


	public void setAdUnitId(String adUnitId) {
		this.adUnitId = adUnitId;
	}
	
	
}
