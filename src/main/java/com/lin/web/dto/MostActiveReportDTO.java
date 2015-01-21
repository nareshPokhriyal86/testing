package com.lin.web.dto;

import java.io.Serializable;

public class MostActiveReportDTO implements Serializable {

	private String c1lineItem;
	private Long impressionBookedReport;
	private Long impressionDeliveredReport;
	private Double pacingReport;
	private String c2impressionBooked;
	private String c3impressionDelivered;
	private String c4pacing;
	
    public MostActiveReportDTO() {
		
	}

	public MostActiveReportDTO(String c1lineItem, Long impressionBookedReport,
			Long impressionDeliveredReport, Double pacingReport,
			String c2impressionBooked, String c3impressionDelivered,
			String c4pacing) {
		super();
		this.c1lineItem = c1lineItem;
		this.impressionBookedReport = impressionBookedReport;
		this.impressionDeliveredReport = impressionDeliveredReport;
		this.pacingReport = pacingReport;
		this.c2impressionBooked = c2impressionBooked;
		this.c3impressionDelivered = c3impressionDelivered;
		this.c4pacing = c4pacing;
	}

	@Override
	public String toString() {
		return "MostActiveReportDTO [c1lineItem=" + c1lineItem
				+ ", impressionBookedReport=" + impressionBookedReport
				+ ", impressionDeliveredReport=" + impressionDeliveredReport
				+ ", pacingReport=" + pacingReport + ", c2impressionBooked="
				+ c2impressionBooked + ", c3impressionDelivered="
				+ c3impressionDelivered + ", c4pacing=" + c4pacing + "]";
	}

	public String getC1lineItem() {
		return c1lineItem;
	}

	public void setC1lineItem(String c1lineItem) {
		this.c1lineItem = c1lineItem;
	}

	public Long getImpressionBookedReport() {
		return impressionBookedReport;
	}

	public void setImpressionBookedReport(Long impressionBookedReport) {
		this.impressionBookedReport = impressionBookedReport;
	}

	public Long getImpressionDeliveredReport() {
		return impressionDeliveredReport;
	}

	public void setImpressionDeliveredReport(Long impressionDeliveredReport) {
		this.impressionDeliveredReport = impressionDeliveredReport;
	}

	public Double getPacingReport() {
		return pacingReport;
	}

	public void setPacingReport(Double pacingReport) {
		this.pacingReport = pacingReport;
	}

	public String getC2impressionBooked() {
		return c2impressionBooked;
	}

	public void setC2impressionBooked(String c2impressionBooked) {
		this.c2impressionBooked = c2impressionBooked;
	}

	public String getC3impressionDelivered() {
		return c3impressionDelivered;
	}

	public void setC3impressionDelivered(String c3impressionDelivered) {
		this.c3impressionDelivered = c3impressionDelivered;
	}

	public String getC4pacing() {
		return c4pacing;
	}

	public void setC4pacing(String c4pacing) {
		this.c4pacing = c4pacing;
	}

	
	
	
	
	
	
}
