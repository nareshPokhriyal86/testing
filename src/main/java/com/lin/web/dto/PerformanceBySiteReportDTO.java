package com.lin.web.dto;

import java.io.Serializable;
import java.util.Date;

public class PerformanceBySiteReportDTO implements Serializable {

	private Date startDateTime;
	private String site;
	private Long impressionDelivered;
	private Long clicks;
	private Double CTR;
	
	public PerformanceBySiteReportDTO() {
	}
	
	public PerformanceBySiteReportDTO(Date startDateTime, String site,
			Long impressionDelivered, Long clicks, Double cTR) {
		super();
		this.startDateTime = startDateTime;
		this.site = site;
		this.impressionDelivered = impressionDelivered;
		this.clicks = clicks;
		CTR = cTR;
	}
	@Override
	public String toString() {
		return "PerformanceBySiteReportDTO [startDateTime=" + startDateTime
				+ ", site=" + site + ", impressionDelivered="
				+ impressionDelivered + ", clicks=" + clicks + ", CTR=" + CTR
				+ "]";
	}
	public Date getStartDateTime() {
		return startDateTime;
	}
	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
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
	public Double getCTR() {
		return CTR;
	}
	public void setCTR(Double cTR) {
		CTR = cTR;
	}
}
