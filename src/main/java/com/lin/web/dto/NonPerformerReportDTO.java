package com.lin.web.dto;

import java.io.Serializable;

public class NonPerformerReportDTO implements Serializable{

	private String site;
	private Long impressionDeliveredReport;
	private Long clicksReport;
	private Double CTRReport;
	
	public NonPerformerReportDTO() {
		
	}

	public NonPerformerReportDTO(String site, Long impressionDeliveredReport,
			Long clicksReport, Double cTRReport) {
		super();
		this.site = site;
		this.impressionDeliveredReport = impressionDeliveredReport;
		this.clicksReport = clicksReport;
		CTRReport = cTRReport;
	}

	@Override
	public String toString() {
		return "NonPerformerReportDTO [site=" + site
				+ ", impressionDeliveredReport=" + impressionDeliveredReport
				+ ", clicksReport=" + clicksReport + ", CTRReport=" + CTRReport
				+ "]";
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public Long getImpressionDeliveredReport() {
		return impressionDeliveredReport;
	}

	public void setImpressionDeliveredReport(Long impressionDeliveredReport) {
		this.impressionDeliveredReport = impressionDeliveredReport;
	}

	public Long getClicksReport() {
		return clicksReport;
	}

	public void setClicksReport(Long clicksReport) {
		this.clicksReport = clicksReport;
	}

	public Double getCTRReport() {
		return CTRReport;
	}

	public void setCTRReport(Double cTRReport) {
		CTRReport = cTRReport;
	}
	
	
}
