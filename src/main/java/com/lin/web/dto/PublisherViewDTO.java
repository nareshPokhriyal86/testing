package com.lin.web.dto;

import javax.persistence.Id;



public class PublisherViewDTO {

	@Id
	private Long id;
	private String callPriority;
	private String networkOrRTB;
	private String ecpm;
	private String fillRate;
	private String impressions;
	private String clicks;
	private String revenue;
	private String floorCPM;
	private String floorCPC;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PublisherViewDTO(String callPriority,String networkOrRTB,String ecpm,String fillRate,String impressions,String clicks,String revenue,String floorCPM,String floorCPC){
		this.callPriority=callPriority;
		this.networkOrRTB=networkOrRTB;
		this.ecpm=ecpm;
		this.fillRate=fillRate;
		this.impressions=impressions;
		this.clicks=clicks;
		this.revenue=revenue;
		this.floorCPM=floorCPM;
		this.floorCPC=floorCPC;
	}
	
	public PublisherViewDTO()
	{
		
	}
	
	public String getCallPriority() {
		return callPriority;
	}

	public void setCallPriority(String callPriority) {
		this.callPriority = callPriority;
	}

	public String getNetworkOrRTB() {
		return networkOrRTB;
	}

	public void setNetworkOrRTB(String networkOrRTB) {
		this.networkOrRTB = networkOrRTB;
	}

	public String getEcpm() {
		return ecpm;
	}

	public void setEcpm(String ecpm) {
		this.ecpm = ecpm;
	}

	public String getFillRate() {
		return fillRate;
	}

	public void setFillRate(String fillRate) {
		this.fillRate = fillRate;
	}

	public String getImpressions() {
		return impressions;
	}

	public void setImpressions(String impressions) {
		this.impressions = impressions;
	}

	public String getClicks() {
		return clicks;
	}

	public void setClicks(String clicks) {
		this.clicks = clicks;
	}

	public String getRevenue() {
		return revenue;
	}

	public void setRevenue(String revenue) {
		this.revenue = revenue;
	}

	public String getFloorCPM() {
		return floorCPM;
	}

	public void setFloorCPM(String floorCPM) {
		this.floorCPM = floorCPM;
	}

	public String getFloorCPC() {
		return floorCPC;
	}

	public void setFloorCPC(String floorCPC) {
		this.floorCPC = floorCPC;
	}

	
	
}
