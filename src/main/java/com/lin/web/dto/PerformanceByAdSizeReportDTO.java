package com.lin.web.dto;

import java.io.Serializable;
import java.util.Date;

public class PerformanceByAdSizeReportDTO implements Serializable {

	private String creativeSize;
	private Double CTR;
	private String benchCtr;
	private Long impressionDelivered;
	private Long clicks;
	private Double reportCTR;
	public PerformanceByAdSizeReportDTO() {
		
	}

	
	public PerformanceByAdSizeReportDTO(String creativeSize, Double cTR,
			String benchCtr, Long impressionDelivered, Long clicks) {
		super();
		this.creativeSize = creativeSize;
		CTR = cTR;
		this.benchCtr = benchCtr;
		this.impressionDelivered = impressionDelivered;
		this.clicks = clicks;
	}


	@Override
	public String toString() {
		return "PerformanceByAdSizeReportDTO [creativeSize=" + creativeSize
				+ ", CTR=" + CTR + ", benchCtr=" + benchCtr
				+ ", impressionDelivered=" + impressionDelivered + ", clicks="
				+ clicks + "]";
	}


	public String getCreativeSize() {
		return creativeSize;
	}

	public void setCreativeSize(String creativeSize) {
		this.creativeSize = creativeSize;
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
