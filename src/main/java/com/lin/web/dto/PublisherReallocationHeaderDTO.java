package com.lin.web.dto;

public class PublisherReallocationHeaderDTO {

	private String requests;
	private String delivered;
	private String fillRate;
	private String clicks;
	private String CTR;
	private String eCPM;
	private String RPM;
	private String payOuts;
	
	
	public PublisherReallocationHeaderDTO() {
		
	}
	
	
	public PublisherReallocationHeaderDTO(String requests, String delivered,
			String fillRate, String clicks, String cTR, String eCPM,
			String rPM, String payOuts) {
		super();
		this.requests = requests;
		this.delivered = delivered;
		this.fillRate = fillRate;
		this.clicks = clicks;
		CTR = cTR;
		this.eCPM = eCPM;
		RPM = rPM;
		this.payOuts = payOuts;
	}


	public String getRequests() {
		return requests;
	}
	public void setRequests(String requests) {
		this.requests = requests;
	}
	public String getDelivered() {
		return delivered;
	}
	public void setDelivered(String delivered) {
		this.delivered = delivered;
	}
	public String getFillRate() {
		return fillRate;
	}
	public void setFillRate(String fillRate) {
		this.fillRate = fillRate;
	}
	public String getClicks() {
		return clicks;
	}
	public void setClicks(String clicks) {
		this.clicks = clicks;
	}
	public String getCTR() {
		return CTR;
	}
	public void setCTR(String cTR) {
		CTR = cTR;
	}
	public String geteCPM() {
		return eCPM;
	}
	public void seteCPM(String eCPM) {
		this.eCPM = eCPM;
	}
	public String getRPM() {
		return RPM;
	}
	public void setRPM(String rPM) {
		RPM = rPM;
	}
	public String getPayOuts() {
		return payOuts;
	}
	public void setPayOuts(String payOuts) {
		this.payOuts = payOuts;
	}
}
