package com.lin.server.bean;

public class PublisherSummaryObj {

	private String channelName;
	private int site;
	private long impressionsDelivered;
	private long clicks;
	private long requests;
	private double CTR;
	private double eCPM;
	private double RPM;
	private double payOuts;
	
	public PublisherSummaryObj(){
		
	}
	
	public PublisherSummaryObj(String channelName,int site,long impressionsDelivered,long clicks,long requests,double CTR,double eCPM,double RPM,double payOuts) {
		super();
		this.channelName = channelName;
		this.site = site;
		this.impressionsDelivered = impressionsDelivered;
		this.clicks = clicks;
		this.requests = requests;
		this.CTR = CTR;
		this.eCPM = eCPM;
		this.RPM = RPM;
		this.payOuts = payOuts;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public int getSite() {
		return site;
	}

	public void setSite(int site) {
		this.site = site;
	}

	public long getImpressionsDelivered() {
		return impressionsDelivered;
	}

	public void setImpressionsDelivered(long impressionsDelivered) {
		this.impressionsDelivered = impressionsDelivered;
	}

	public long getClicks() {
		return clicks;
	}

	public void setClicks(long clicks) {
		this.clicks = clicks;
	}

	public long getRequests() {
		return requests;
	}

	public void setRequests(long requests) {
		this.requests = requests;
	}

	public double getCTR() {
		return CTR;
	}

	public void setCTR(double cTR) {
		CTR = cTR;
	}

	public double geteCPM() {
		return eCPM;
	}

	public void seteCPM(double eCPM) {
		this.eCPM = eCPM;
	}

	public double getRPM() {
		return RPM;
	}

	public void setRPM(double rPM) {
		RPM = rPM;
	}

	public double getPayOuts() {
		return payOuts;
	}

	public void setPayOuts(double payOuts) {
		this.payOuts = payOuts;
	}

	
}
