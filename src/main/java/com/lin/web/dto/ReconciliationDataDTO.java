package com.lin.web.dto;

import java.io.Serializable;
import java.util.Date;


public class ReconciliationDataDTO implements Serializable{

	
	private String formattedDate;
	private long DFP_requests;
	private long DFP_delivered;
	private long DFP_passback;
	private long demandPartnerRequests;
	private long demandPartnerDelivered;
	private long demandPartnerPassbacks;
	private double varianceRequests;
	private double varianceDelivered;
	private double variancePassbacks;
	
	private Date date;
	private String channelName;
	private String dataSource;
	private String siteType;
	private long delivered;
	private long requests;
	
	public ReconciliationDataDTO(){
		
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public long getDFP_requests() {
		return DFP_requests;
	}

	public void setDFP_requests(long dFP_requests) {
		DFP_requests = dFP_requests;
	}

	public long getDFP_delivered() {
		return DFP_delivered;
	}

	public void setDFP_delivered(long dFP_delivered) {
		DFP_delivered = dFP_delivered;
	}

	public long getDFP_passback() {
		return DFP_passback;
	}

	public void setDFP_passback(long dFP_passback) {
		DFP_passback = dFP_passback;
	}

	public long getDemandPartnerRequests() {
		return demandPartnerRequests;
	}

	public void setDemandPartnerRequests(long demandPartnerRequests) {
		this.demandPartnerRequests = demandPartnerRequests;
	}

	public long getDemandPartnerDelivered() {
		return demandPartnerDelivered;
	}

	public void setDemandPartnerDelivered(long demandPartnerDelivered) {
		this.demandPartnerDelivered = demandPartnerDelivered;
	}

	public long getDemandPartnerPassbacks() {
		return demandPartnerPassbacks;
	}

	public void setDemandPartnerPassbacks(long demandPartnerPassbacks) {
		this.demandPartnerPassbacks = demandPartnerPassbacks;
	}

	public double getVarianceRequests() {
		return varianceRequests;
	}

	public void setVarianceRequests(double varianceRequests) {
		this.varianceRequests = varianceRequests;
	}

	public double getVarianceDelivered() {
		return varianceDelivered;
	}

	public void setVarianceDelivered(double varianceDelivered) {
		this.varianceDelivered = varianceDelivered;
	}

	public double getVariancePassbacks() {
		return variancePassbacks;
	}

	public void setVariancePassbacks(double variancePassbacks) {
		this.variancePassbacks = variancePassbacks;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getSiteType() {
		return siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

	public long getDelivered() {
		return delivered;
	}

	public void setDelivered(long delivered) {
		this.delivered = delivered;
	}

	public long getRequests() {
		return requests;
	}

	public void setRequests(long requests) {
		this.requests = requests;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

	public void setFormattedDate(String formattedDate) {
		this.formattedDate = formattedDate;
	}

	public String getFormattedDate() {
		return formattedDate;
	}
	
	
}
