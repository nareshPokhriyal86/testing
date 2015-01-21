package com.lin.web.dto;

import java.io.Serializable;

public class PerformanceByLocationReportDTO implements Serializable{

	private String region;
	private Double CTR;
	private Long impressionDelivered;
	private Long clicks;
	
	public PerformanceByLocationReportDTO() {

	}

	

	public PerformanceByLocationReportDTO(String region, Double cTR,
			Long impressionDelivered, Long clicks) {
		super();
		this.region = region;
		CTR = cTR;
		this.impressionDelivered = impressionDelivered;
		this.clicks = clicks;
	}



	@Override
	public String toString() {
		return "PerformanceByLocationReportDTO [region=" + region + ", CTR="
				+ CTR + ", impressionDelivered=" + impressionDelivered
				+ ", clicks=" + clicks + "]";
	}



	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public Double getCTR() {
		return CTR;
	}

	public void setCTR(Double cTR) {
		CTR = cTR;
	}

	public Long getImpressionDelivered() {
		return impressionDelivered;
	}

	public void setImpressionDelivered(Long impressionDelivered) {
		this.impressionDelivered = impressionDelivered;
	}

	public Long getClicks() {
		return clicks;
	}

	public void setClicks(Long clicks) {
		this.clicks = clicks;
	}
	
	
	
}
