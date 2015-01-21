package com.lin.web.dto;

import java.io.Serializable;
import java.util.Date;

public class CampaignStatusAlertDTO implements Serializable {

	private String alertDate;
	private String campaignName;
	private String placement;
	private String publisherName;
	private String exceptions;
	private String rowColor;
	
	public CampaignStatusAlertDTO()
	{
		
	}
	
	public CampaignStatusAlertDTO(String alertDate, String campaignName, String placement, String publisherName, String exceptions) {
		this.alertDate = alertDate;
		this.campaignName = campaignName;
		this.placement = placement;
		this.publisherName = publisherName;
		this.exceptions = exceptions;
	}

	public String getAlertDate() {
		return alertDate;
	}

	public void setAlertDate(String alertDate) {
		this.alertDate = alertDate;
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public String getPlacement() {
		return placement;
	}

	public void setPlacement(String placement) {
		this.placement = placement;
	}

	public String getPublisherName() {
		return publisherName;
	}

	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}

	public String getExceptions() {
		return exceptions;
	}

	public void setExceptions(String exceptions) {
		this.exceptions = exceptions;
	}

	public String getRowColor() {
		return rowColor;
	}

	public void setRowColor(String rowColor) {
		this.rowColor = rowColor;
	}
	
	
}
