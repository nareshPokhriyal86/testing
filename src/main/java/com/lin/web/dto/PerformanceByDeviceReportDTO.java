package com.lin.web.dto;

import java.io.Serializable;
import java.util.Date;

public class PerformanceByDeviceReportDTO implements Serializable {

	private String targetValue;
	private Double CTR;
	private String benchCtr;
	private Long impressionDelivered;
	private Long clicks;
	private Double reportCTR;
	public PerformanceByDeviceReportDTO() {
		
	}


	public PerformanceByDeviceReportDTO(String targetValue, Double cTR,
			String benchCtr, Long impressionDelivered, Long clicks) {
		super();
		this.targetValue = targetValue;
		CTR = cTR;
		this.benchCtr = benchCtr;
		this.impressionDelivered = impressionDelivered;
		this.clicks = clicks;
	}


	@Override
	public String toString() {
		return "PerformanceByDeviceReportDTO [targetValue=" + targetValue
				+ ", CTR=" + CTR + ", benchCtr=" + benchCtr
				+ ", impressionDelivered=" + impressionDelivered + ", clicks="
				+ clicks + "]";
	}



	public String getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(String targetValue) {
		this.targetValue = targetValue;
	}

	public Double getCTR() {
		return CTR;
	}

	public void setCTR(Double cTR) {
		CTR = cTR;
	}

	public String getBenchCtr() {
		return benchCtr;
	}

	public void setBenchCtr(String benchCtr) {
		this.benchCtr = benchCtr;
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


	public Double getReportCTR() {
		return reportCTR;
	}


	public void setReportCTR(Double reportCTR) {
		this.reportCTR = reportCTR;
	}
	
	
}
