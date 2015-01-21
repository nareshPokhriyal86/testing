package com.lin.server.bean;

public class PublisherInventoryRevenueObj {
	private String site;
	
	private String delivered;
	private String mobileApp;
	private String mobileWeb;
	private String clicks;
	private String CTR;
	private String eCPM;
	private String RPM;
	private String payOuts;
	private String fillPercentage;
	
	public PublisherInventoryRevenueObj(){
		
	}
	
	
	
	
	public PublisherInventoryRevenueObj(String site, String delivered,
			String mobileApp, String mobileWeb, String clicks, String cTR,
			String eCPM, String rPM, String payOuts, String fillPercentage) {
		super();
		this.site = site;
		this.delivered = delivered;
		this.mobileApp = mobileApp;
		this.mobileWeb = mobileWeb;
		this.clicks = clicks;
		CTR = cTR;
		this.eCPM = eCPM;
		RPM = rPM;
		this.payOuts = payOuts;
		this.fillPercentage = fillPercentage;
	}




	public String getDelivered() {
		return delivered;
	}
	public void setDelivered(String delivered) {
		this.delivered = delivered;
	}
	public String getMobileApp() {
		return mobileApp;
	}
	public void setMobileApp(String mobileApp) {
		this.mobileApp = mobileApp;
	}
	public String getMobileWeb() {
		return mobileWeb;
	}
	public void setMobileWeb(String mobileWeb) {
		this.mobileWeb = mobileWeb;
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
	public void setSite(String site) {
		this.site = site;
	}
	public String getSite() {
		return site;
	}
	public String getFillPercentage() {
		return fillPercentage;
	}
	public void setFillPercentage(String fillPercentage) {
		this.fillPercentage = fillPercentage;
	}
	
}
