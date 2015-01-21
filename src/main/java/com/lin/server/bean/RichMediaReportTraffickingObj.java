package com.lin.server.bean;


/*
 * @author Youdhveer Panwar
 * 
 * This is a rich media trafficking report template pojo file
 */
public class RichMediaReportTraffickingObj {

	
	private String campaignId;
	private String campaignName;
	private String date;
	private String accountDate;
	private String creativeId;
	private String creativeName;
	private String format;
	private String placementId;
	private String placementName;
	private String platform;
	private String sdk;
	private long impressions;
	private long shownImpressions;
	private long interactions;
	private long firstClickThroughs;
	private double interactionRate;
	private double firstClickThroughRate;
	
	private String loadTimestamp;
	private long channelId;
	private String channelName;
	private String channelType;
	private String salesType;
	private long publisherId;
	private String publisherName;
	private String dataSource;
	
	public RichMediaReportTraffickingObj(){
		
	}

	
	public RichMediaReportTraffickingObj(String campaignId, String campaignName,
			String date, String accountDate, String creativeId,
			String creativeName, String format, String placementId,
			String placementName, String platform, String sdk,
			long impressions, long shownImpressions, long interactions,
			long firstClickThroughs, double interactionRate,
			double firstClickThroughRate) {
		this.campaignId = campaignId;
		this.campaignName = campaignName;
		this.date = date;
		this.accountDate = accountDate;
		this.creativeId = creativeId;
		this.creativeName = creativeName;
		this.format = format;
		this.placementId = placementId;
		this.placementName = placementName;
		this.platform = platform;
		this.sdk = sdk;
		this.impressions = impressions;
		this.shownImpressions = shownImpressions;
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
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

	public long getShownImpressions() {
		return shownImpressions;
	}

	public void setShownImpressions(long shownImpressions) {
		this.shownImpressions = shownImpressions;
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

	public void setLoadTimestamp(String loadTimestamp) {
		this.loadTimestamp = loadTimestamp;
	}

	public String getLoadTimestamp() {
		return loadTimestamp;
	}

	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}

	public long getChannelId() {
		return channelId;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	public String getChannelType() {
		return channelType;
	}

	public void setSalesType(String salesType) {
		this.salesType = salesType;
	}

	public String getSalesType() {
		return salesType;
	}

	public void setPublisherId(long publisherId) {
		this.publisherId = publisherId;
	}

	public long getPublisherId() {
		return publisherId;
	}

	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}

	public String getPublisherName() {
		return publisherName;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getDataSource() {
		return dataSource;
	}


	public void setImpressions(long impressions) {
		this.impressions = impressions;
	}


	public long getImpressions() {
		return impressions;
	}

	
	
}
