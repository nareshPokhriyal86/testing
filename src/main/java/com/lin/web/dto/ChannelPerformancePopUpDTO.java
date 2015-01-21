package com.lin.web.dto;

import java.io.Serializable;

public class ChannelPerformancePopUpDTO implements Serializable{

	private String id;
	private String salesChannel;
	private String impressionsDelivered;
	private String clicks;
	private String ctr;
	private String eCPM;
	private String payouts;
	private String graphData;
	
	private String impressionsDeliveredLastTime;
	private String clicksLastTime;
	private String ctrLastTime;
	private String eCPMLastTime;
	private String payoutsLastTime;
	
	private String impressionsDeliveredMTD;
	private String clicksMTD;
	private String ctrMTD;
	private String eCPMMTD;
	private String payoutsMTD;
	
	private long advertiserId;
	private long campaignId;
	private String campaignName;
	private long campaignImpressions;
	private long campaignClicks;
	private double campaignCtr;
	private double campaignSpend;
	private double campaignPacing;
	
	public ChannelPerformancePopUpDTO(){
		
	}	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getImpressionsDelivered() {
		return impressionsDelivered;
	}

	public void setImpressionsDelivered(String impressionsDelivered) {
		this.impressionsDelivered = impressionsDelivered;
	}

	public String getClicks() {
		return clicks;
	}

	public void setClicks(String clicks) {
		this.clicks = clicks;
	}

	
	public String geteCPM() {
		return eCPM;
	}

	public void seteCPM(String eCPM) {
		this.eCPM = eCPM;
	}

	public String getPayouts() {
		return payouts;
	}

	public void setPayouts(String payouts) {
		this.payouts = payouts;
	}

	public String getGraphData() {
		return graphData;
	}

	public void setGraphData(String graphData) {
		this.graphData = graphData;
	}

	public String getSalesChannel() {
		return salesChannel;
	}

	public void setSalesChannel(String salesChannel) {
		this.salesChannel = salesChannel;
	}

	public String getCtr() {
		return ctr;
	}

	public void setCtr(String ctr) {
		this.ctr = ctr;
	}

	public String getImpressionsDeliveredLastTime() {
		return impressionsDeliveredLastTime;
	}

	public void setImpressionsDeliveredLastTime(String impressionsDeliveredLastTime) {
		this.impressionsDeliveredLastTime = impressionsDeliveredLastTime;
	}

	public String getClicksLastTime() {
		return clicksLastTime;
	}

	public void setClicksLastTime(String clicksLastTime) {
		this.clicksLastTime = clicksLastTime;
	}

	public String getCtrLastTime() {
		return ctrLastTime;
	}

	public void setCtrLastTime(String ctrLastTime) {
		this.ctrLastTime = ctrLastTime;
	}

	public String geteCPMLastTime() {
		return eCPMLastTime;
	}

	public void seteCPMLastTime(String eCPMLastTime) {
		this.eCPMLastTime = eCPMLastTime;
	}

	public String getPayoutsLastTime() {
		return payoutsLastTime;
	}

	public void setPayoutsLastTime(String payoutsLastTime) {
		this.payoutsLastTime = payoutsLastTime;
	}

	public String getImpressionsDeliveredMTD() {
		return impressionsDeliveredMTD;
	}

	public void setImpressionsDeliveredMTD(String impressionsDeliveredMTD) {
		this.impressionsDeliveredMTD = impressionsDeliveredMTD;
	}

	public String getClicksMTD() {
		return clicksMTD;
	}

	public void setClicksMTD(String clicksMTD) {
		this.clicksMTD = clicksMTD;
	}

	public String getCtrMTD() {
		return ctrMTD;
	}

	public void setCtrMTD(String ctrMTD) {
		this.ctrMTD = ctrMTD;
	}

	public String geteCPMMTD() {
		return eCPMMTD;
	}

	public void seteCPMMTD(String eCPMMTD) {
		this.eCPMMTD = eCPMMTD;
	}

	public String getPayoutsMTD() {
		return payoutsMTD;
	}

	public void setPayoutsMTD(String payoutsMTD) {
		this.payoutsMTD = payoutsMTD;
	}

	public long getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(long campaignId) {
		this.campaignId = campaignId;
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public long getCampaignImpressions() {
		return campaignImpressions;
	}

	public void setCampaignImpressions(long campaignImpressions) {
		this.campaignImpressions = campaignImpressions;
	}

	public long getCampaignClicks() {
		return campaignClicks;
	}

	public void setCampaignClicks(long campaignClicks) {
		this.campaignClicks = campaignClicks;
	}

	public double getCampaignCtr() {
		return campaignCtr;
	}

	public void setCampaignCtr(double campaignCtr) {
		this.campaignCtr = campaignCtr;
	}

	public double getCampaignSpend() {
		return campaignSpend;
	}

	public void setCampaignSpend(double campaignSpend) {
		this.campaignSpend = campaignSpend;
	}

	public double getCampaignPacing() {
		return campaignPacing;
	}

	public void setCampaignPacing(double campaignPacing) {
		this.campaignPacing = campaignPacing;
	}

	public long getAdvertiserId() {
		return advertiserId;
	}

	public void setAdvertiserId(long advertiserId) {
		this.advertiserId = advertiserId;
	}
	
	
	
	
}
