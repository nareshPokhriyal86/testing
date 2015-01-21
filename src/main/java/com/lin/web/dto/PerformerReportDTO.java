package com.lin.web.dto;

import java.io.Serializable;

public class PerformerReportDTO implements Serializable {
	private String s1Site;
	private Long impressionDeliveredReport;
	private Long clicksReport;
	private Double CTRReport;
	private String s2ImpressionDelivered;
	private String s3Clicks;
	private String s4CTR;
	private Double benchCtr;
	public PerformerReportDTO() {
		
	}
	
	public PerformerReportDTO(String s1Site, Long impressionDeliveredReport,
			Long clicksReport, Double cTRReport, String s2ImpressionDelivered,
			String s3Clicks, String s4ctr, Double benchCtr) {
		super();
		this.s1Site = s1Site;
		this.impressionDeliveredReport = impressionDeliveredReport;
		this.clicksReport = clicksReport;
		CTRReport = cTRReport;
		this.s2ImpressionDelivered = s2ImpressionDelivered;
		this.s3Clicks = s3Clicks;
		s4CTR = s4ctr;
		this.benchCtr = benchCtr;
	}
	
	@Override
	public String toString() {
		return "PerformerReportDTO [s1Site=" + s1Site
				+ ", impressionDeliveredReport=" + impressionDeliveredReport
				+ ", clicksReport=" + clicksReport + ", CTRReport=" + CTRReport
				+ ", s2ImpressionDelivered=" + s2ImpressionDelivered
				+ ", s3Clicks=" + s3Clicks + ", s4CTR=" + s4CTR + ", benchCtr="
				+ benchCtr + "]";
	}
	
	public String getS1Site() {
		return s1Site;
	}
	public void setS1Site(String s1Site) {
		this.s1Site = s1Site;
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
	public String getS2ImpressionDelivered() {
		return s2ImpressionDelivered;
	}
	public void setS2ImpressionDelivered(String s2ImpressionDelivered) {
		this.s2ImpressionDelivered = s2ImpressionDelivered;
	}
	public String getS3Clicks() {
		return s3Clicks;
	}
	public void setS3Clicks(String s3Clicks) {
		this.s3Clicks = s3Clicks;
	}
	public String getS4CTR() {
		return s4CTR;
	}
	public void setS4CTR(String s4ctr) {
		s4CTR = s4ctr;
	}
	public Double getBenchCtr() {
		return benchCtr;
	}
	public void setBenchCtr(Double benchCtr) {
		this.benchCtr = benchCtr;
	}

	
	
}
