package com.lin.web.dto;


public class CeltraDTO {

	private String campaignId;
	private String campaignName;
	private String utcDate;
	private String accountDate;
	private String creativeId;
	private String creativeName;
	private String format;
	private String placementId;
	private String placementName;
	private String platform;
	private String sdk;
	private long sessions;
	private long creativeViews;
	private long interactions;
	private long firstClickThroughs;
	private double interactionRate;
	private double firstClickThroughRate;

	
	
	public CeltraDTO(){
		
	}



	public CeltraDTO(String campaignId, String campaignName, String utcDate,
			String accountDate, String creativeId, String creativeName,
			String format, String placementId, String placementName,
			String platform, String sdk, long sessions, long creativeViews,
			long interactions, long firstClickThroughs, double interactionRate,
			double firstClickThroughRate) {
		this.campaignId = campaignId;
		this.campaignName = campaignName;
		this.utcDate = utcDate;
		this.accountDate = accountDate;
		this.creativeId = creativeId;
		this.creativeName = creativeName;
		this.format = format;
		this.placementId = placementId;
		this.placementName = placementName;
		this.platform = platform;
		this.sdk = sdk;
		this.sessions = sessions;
		this.creativeViews = creativeViews;
		this.interactions = interactions;
		this.firstClickThroughs = firstClickThroughs;
		this.interactionRate = interactionRate;
		this.firstClickThroughRate = firstClickThroughRate;
	}



	public String getCampaignId() {
		return campaignId;
	}



	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}



	public String getCampaignName() {
		return campaignName;
	}



	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}



	public String getUtcDate() {
		return utcDate;
	}



	public void setUtcDate(String utcDate) {
		this.utcDate = utcDate;
	}



	public String getAccountDate() {
		return accountDate;
	}



	public void setAccountDate(String accountDate) {
		this.accountDate = accountDate;
	}



	public String getCreativeId() {
		return creativeId;
	}



	public void setCreativeId(String creativeId) {
		this.creativeId = creativeId;
	}



	public String getCreativeName() {
		return creativeName;
	}



	public void setCreativeName(String creativeName) {
		this.creativeName = creativeName;
	}



	public String getFormat() {
		return format;
	}



	public void setFormat(String format) {
		this.format = format;
	}



	public String getPlacementId() {
		return placementId;
	}



	public void setPlacementId(String placementId) {
		this.placementId = placementId;
	}



	public String getPlacementName() {
		return placementName;
	}



	public void setPlacementName(String placementName) {
		this.placementName = placementName;
	}



	public String getPlatform() {
		return platform;
	}



	public void setPlatform(String platform) {
		this.platform = platform;
	}



	public String getSdk() {
		return sdk;
	}



	public void setSdk(String sdk) {
		this.sdk = sdk;
	}



	public long getSessions() {
		return sessions;
	}



	public void setSessions(long sessions) {
		this.sessions = sessions;
	}



	public long getCreativeViews() {
		return creativeViews;
	}



	public void setCreativeViews(long creativeViews) {
		this.creativeViews = creativeViews;
	}



	public long getInteractions() {
		return interactions;
	}



	public void setInteractions(long interactions) {
		this.interactions = interactions;
	}



	public long getFirstClickThroughs() {
		return firstClickThroughs;
	}



	public void setFirstClickThroughs(long firstClickThroughs) {
		this.firstClickThroughs = firstClickThroughs;
	}



	public double getInteractionRate() {
		return interactionRate;
	}



	public void setInteractionRate(double interactionRate) {
		this.interactionRate = interactionRate;
	}



	public double getFirstClickThroughRate() {
		return firstClickThroughRate;
	}



	public void setFirstClickThroughRate(double firstClickThroughRate) {
		this.firstClickThroughRate = firstClickThroughRate;
	}


	
	
}
