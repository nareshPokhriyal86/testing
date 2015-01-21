package com.lin.server.bean;



/*
 * @author Youdhveer Panwar
 * 
 * This is a rich media common report template pojo file
 */
public class RichMediaCommonReportObj {

	private String loadTimestamp;
	private long channelId;
	private String channelName;
	private String channelType;
	private String salesType;
	private long publisherId;
	private String publisherName;
	private String dataSource;
	private String date;
	
	private String siteId;
	private String siteName;
	private String siteType;
	private long zoneId;
	private String zoneType;
	private String adFormat;
	private String servingPlatform;
	
	private long advertiserId;
	private String advertiser;
	private String orderId;
	private String order;
	private String lineItemId;
	private String lineItem;
	private String lineItemType;
	private String creativeId;
	private String creative;
	private String creativeSize;
	private String creativeType;	
	private String lineItemLabels;	
	private String orderStartDate;
	private String lineItemStartDate;
	private String orderEndDate;
	private String lineItemEndDate;	
	private String agency;
	private long agencyId;	
	private double goalQuantity;	
	private double rate;
	private long totalClicks;  //First_interactions
	private double CTR;        //firstClickThroughRate 
	private long totalImpressions;
	private long shownImpressions;
	private long displayTime;
	private long expansions;	
	private long interactionImpressions;	
	private long expandingTime;
	private long manualCloses;	
	private double averageInteractionTime;
	private long backupImages;		
	private long interactionTime;	
	private double averageDisplayTime;	
	private long fullScreenImpressions;
	private double interactionRate;	     //First_Interaction_Rate
	private long totalInteractions;      //interactionCount OR firstClickThroughs
	
	private String market;
	private String campaignCategory;
	
	private String customEvent;
	private String customEventType;	
	private long customEventId;		
	
	/*Rich Media viewership*/
	private long richMediaBackupImages;
	private double richMediaDisplayTime;
	private double richMediaAverageDisplayTime;
	
	 /* Rich Media interaction */
	private long richMediaExpansions;
	private double richMediaExpandingTime;
	private double richMediaInteractionTime;
	private long richMediaInteractionCount;
	private double richMediaInteractionRate;
	private double richMediaAverageInteractionTime;
	private long richMediaInteractionImpressions;
	private long richMediaManualCloses;
	private long richMediaFullScreenImpressions;
	 
	 /* Rich Media video metrics */
	private long richMediaVideoInteractions;
	private double richMediaVideoInteractionRate;
	private long richMediaVideoMutes;
	private long richMediaVideoPauses;
	private long richMediaVideoPlayes;
	private long richMediaVideoMidpoints;
	private long richMediaVideoCompletes;
	private long richMediaVideoReplays;
	private long richMediaVideoStops;
	private long richMediaVideoUnmutes;
	private double richMediaVideoViewRate;
	private double richMediaVideoViewTime;
	 
	/* Video viewership */
	private long start;
	private long firstQuartile;
	private long midpoint;
	private long thirdQuartile;
	private long complete;
	private double averageViewRate;
	private double completionRate;
	private long errorCount;
	private double videoLength;
	private long videoSkipShown;
	private long engageView;
	private double viewThroughRate;
	
	/* Video interaction */	
	private long pause;
	private long resume;
	private long rewind;
	private long mute;
	private long unmute;
	private long collapse;
	private long expand;
	private long fullScreen;
	private long videoSkips;
	private double averageInteractionRate;
	private double viewRate;
	
	
	public RichMediaCommonReportObj(){
		
	}

	public String getLoadTimestamp() {
		return loadTimestamp;
	}

	public void setLoadTimestamp(String loadTimestamp) {
		this.loadTimestamp = loadTimestamp;
	}

	public long getChannelId() {
		return channelId;
	}

	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	public String getSalesType() {
		return salesType;
	}

	public void setSalesType(String salesType) {
		this.salesType = salesType;
	}

	public long getPublisherId() {
		return publisherId;
	}

	public void setPublisherId(long publisherId) {
		this.publisherId = publisherId;
	}

	public String getPublisherName() {
		return publisherName;
	}

	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getSiteType() {
		return siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

	public long getZoneId() {
		return zoneId;
	}

	public void setZoneId(long zoneId) {
		this.zoneId = zoneId;
	}

	public String getZoneType() {
		return zoneType;
	}

	public void setZoneType(String zoneType) {
		this.zoneType = zoneType;
	}

	public String getAdFormat() {
		return adFormat;
	}

	public void setAdFormat(String adFormat) {
		this.adFormat = adFormat;
	}

	public String getServingPlatform() {
		return servingPlatform;
	}

	public void setServingPlatform(String servingPlatform) {
		this.servingPlatform = servingPlatform;
	}

	public long getAdvertiserId() {
		return advertiserId;
	}

	public void setAdvertiserId(long advertiserId) {
		this.advertiserId = advertiserId;
	}

	public String getAdvertiser() {
		return advertiser;
	}

	public void setAdvertiser(String advertiser) {
		this.advertiser = advertiser;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getLineItemId() {
		return lineItemId;
	}

	public void setLineItemId(String lineItemId) {
		this.lineItemId = lineItemId;
	}

	public String getLineItem() {
		return lineItem;
	}

	public void setLineItem(String lineItem) {
		this.lineItem = lineItem;
	}

	public String getLineItemType() {
		return lineItemType;
	}

	public void setLineItemType(String lineItemType) {
		this.lineItemType = lineItemType;
	}

	public String getCreativeId() {
		return creativeId;
	}

	public void setCreativeId(String creativeId) {
		this.creativeId = creativeId;
	}

	public String getCreative() {
		return creative;
	}

	public void setCreative(String creative) {
		this.creative = creative;
	}

	public String getCreativeSize() {
		return creativeSize;
	}

	public void setCreativeSize(String creativeSize) {
		this.creativeSize = creativeSize;
	}

	public String getCreativeType() {
		return creativeType;
	}

	public void setCreativeType(String creativeType) {
		this.creativeType = creativeType;
	}

	public String getLineItemLabels() {
		return lineItemLabels;
	}

	public void setLineItemLabels(String lineItemLabels) {
		this.lineItemLabels = lineItemLabels;
	}

	public String getOrderStartDate() {
		return orderStartDate;
	}

	public void setOrderStartDate(String orderStartDate) {
		this.orderStartDate = orderStartDate;
	}

	public String getLineItemStartDate() {
		return lineItemStartDate;
	}

	public void setLineItemStartDate(String lineItemStartDate) {
		this.lineItemStartDate = lineItemStartDate;
	}

	public String getOrderEndDate() {
		return orderEndDate;
	}

	public void setOrderEndDate(String orderEndDate) {
		this.orderEndDate = orderEndDate;
	}

	public String getLineItemEndDate() {
		return lineItemEndDate;
	}

	public void setLineItemEndDate(String lineItemEndDate) {
		this.lineItemEndDate = lineItemEndDate;
	}

	public String getAgency() {
		return agency;
	}

	public void setAgency(String agency) {
		this.agency = agency;
	}

	public long getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(long agencyId) {
		this.agencyId = agencyId;
	}

	public double getGoalQuantity() {
		return goalQuantity;
	}

	public void setGoalQuantity(double goalQuantity) {
		this.goalQuantity = goalQuantity;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public long getTotalClicks() {
		return totalClicks;
	}

	public void setTotalClicks(long totalClicks) {
		this.totalClicks = totalClicks;
	}

	public double getCTR() {
		return CTR;
	}

	public void setCTR(double cTR) {
		CTR = cTR;
	}

	public long getTotalImpressions() {
		return totalImpressions;
	}

	public void setTotalImpressions(long totalImpressions) {
		this.totalImpressions = totalImpressions;
	}

	public long getShownImpressions() {
		return shownImpressions;
	}

	public void setShownImpressions(long shownImpressions) {
		this.shownImpressions = shownImpressions;
	}

	public long getDisplayTime() {
		return displayTime;
	}

	public void setDisplayTime(long displayTime) {
		this.displayTime = displayTime;
	}

	public long getExpansions() {
		return expansions;
	}

	public void setExpansions(long expansions) {
		this.expansions = expansions;
	}

	public long getInteractionImpressions() {
		return interactionImpressions;
	}

	public void setInteractionImpressions(long interactionImpressions) {
		this.interactionImpressions = interactionImpressions;
	}

	public long getExpandingTime() {
		return expandingTime;
	}

	public void setExpandingTime(long expandingTime) {
		this.expandingTime = expandingTime;
	}

	public long getManualCloses() {
		return manualCloses;
	}

	public void setManualCloses(long manualCloses) {
		this.manualCloses = manualCloses;
	}

	public double getAverageInteractionTime() {
		return averageInteractionTime;
	}

	public void setAverageInteractionTime(double averageInteractionTime) {
		this.averageInteractionTime = averageInteractionTime;
	}

	public long getBackupImages() {
		return backupImages;
	}

	public void setBackupImages(long backupImages) {
		this.backupImages = backupImages;
	}

	public long getInteractionTime() {
		return interactionTime;
	}

	public void setInteractionTime(long interactionTime) {
		this.interactionTime = interactionTime;
	}

	public double getAverageDisplayTime() {
		return averageDisplayTime;
	}

	public void setAverageDisplayTime(double averageDisplayTime) {
		this.averageDisplayTime = averageDisplayTime;
	}

	public long getFullScreenImpressions() {
		return fullScreenImpressions;
	}

	public void setFullScreenImpressions(long fullScreenImpressions) {
		this.fullScreenImpressions = fullScreenImpressions;
	}

	public double getInteractionRate() {
		return interactionRate;
	}

	public void setInteractionRate(double interactionRate) {
		this.interactionRate = interactionRate;
	}

	public long getTotalInteractions() {
		return totalInteractions;
	}

	public void setTotalInteractions(long totalInteractions) {
		this.totalInteractions = totalInteractions;
	}

	public String getCustomEvent() {
		return customEvent;
	}

	public void setCustomEvent(String customEvent) {
		this.customEvent = customEvent;
	}

	public String getCustomEventType() {
		return customEventType;
	}

	public void setCustomEventType(String customEventType) {
		this.customEventType = customEventType;
	}

	public long getCustomEventId() {
		return customEventId;
	}

	public void setCustomEventId(long customEventId) {
		this.customEventId = customEventId;
	}

	public double getAverageInteractionRate() {
		return averageInteractionRate;
	}

	public void setAverageInteractionRate(double averageInteractionRate) {
		this.averageInteractionRate = averageInteractionRate;
	}

	public double getAverageViewRate() {
		return averageViewRate;
	}

	public void setAverageViewRate(double averageViewRate) {
		this.averageViewRate = averageViewRate;
	}

	public long getCollapse() {
		return collapse;
	}

	public void setCollapse(long collapse) {
		this.collapse = collapse;
	}

	public long getComplete() {
		return complete;
	}

	public void setComplete(long complete) {
		this.complete = complete;
	}

	public double getCompletionRate() {
		return completionRate;
	}

	public void setCompletionRate(double completionRate) {
		this.completionRate = completionRate;
	}

	public long getEngageView() {
		return engageView;
	}

	public void setEngageView(long engageView) {
		this.engageView = engageView;
	}

	public long getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(long errorCount) {
		this.errorCount = errorCount;
	}

	public long getExpand() {
		return expand;
	}

	public void setExpand(long expand) {
		this.expand = expand;
	}

	public long getFirstQuartile() {
		return firstQuartile;
	}

	public void setFirstQuartile(long firstQuartile) {
		this.firstQuartile = firstQuartile;
	}

	public long getFullScreen() {
		return fullScreen;
	}

	public void setFullScreen(long fullScreen) {
		this.fullScreen = fullScreen;
	}

	public long getMidpoint() {
		return midpoint;
	}

	public void setMidpoint(long midpoint) {
		this.midpoint = midpoint;
	}

	public long getMute() {
		return mute;
	}

	public void setMute(long mute) {
		this.mute = mute;
	}

	public long getPause() {
		return pause;
	}

	public void setPause(long pause) {
		this.pause = pause;
	}

	public long getResume() {
		return resume;
	}

	public void setResume(long resume) {
		this.resume = resume;
	}

	public long getRewind() {
		return rewind;
	}

	public void setRewind(long rewind) {
		this.rewind = rewind;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getThirdQuartile() {
		return thirdQuartile;
	}

	public void setThirdQuartile(long thirdQuartile) {
		this.thirdQuartile = thirdQuartile;
	}

	public long getUnmute() {
		return unmute;
	}

	public void setUnmute(long unmute) {
		this.unmute = unmute;
	}

	public double getVideoLength() {
		return videoLength;
	}

	public void setVideoLength(double videoLength) {
		this.videoLength = videoLength;
	}

	public long getVideoSkipShown() {
		return videoSkipShown;
	}

	public void setVideoSkipShown(long videoSkipShown) {
		this.videoSkipShown = videoSkipShown;
	}

	public long getVideoSkips() {
		return videoSkips;
	}

	public void setVideoSkips(long videoSkips) {
		this.videoSkips = videoSkips;
	}

	/*public double getViewRate() {
		return viewRate;
	}

	public void setViewRate(double viewRate) {
		this.viewRate = viewRate;
	}*/

	public double getViewThroughRate() {
		return viewThroughRate;
	}

	public void setViewThroughRate(double viewThroughRate) {
		this.viewThroughRate = viewThroughRate;
	}

	public String getMarket() {
		return market;
	}

	public void setMarket(String market) {
		this.market = market;
	}

	public String getCampaignCategory() {
		return campaignCategory;
	}

	public void setCampaignCategory(String campaignCategory) {
		this.campaignCategory = campaignCategory;
	}

	public long getRichMediaExpansions() {
		return richMediaExpansions;
	}

	public void setRichMediaExpansions(long richMediaExpansions) {
		this.richMediaExpansions = richMediaExpansions;
	}

	public double getRichMediaExpandingTime() {
		return richMediaExpandingTime;
	}

	public void setRichMediaExpandingTime(double richMediaExpandingTime) {
		this.richMediaExpandingTime = richMediaExpandingTime;
	}

	public double getRichMediaInteractionTime() {
		return richMediaInteractionTime;
	}

	public void setRichMediaInteractionTime(double richMediaInteractionTime) {
		this.richMediaInteractionTime = richMediaInteractionTime;
	}

	public long getRichMediaInteractionCount() {
		return richMediaInteractionCount;
	}

	public void setRichMediaInteractionCount(long richMediaInteractionCount) {
		this.richMediaInteractionCount = richMediaInteractionCount;
	}

	public double getRichMediaInteractionRate() {
		return richMediaInteractionRate;
	}

	public void setRichMediaInteractionRate(double richMediaInteractionRate) {
		this.richMediaInteractionRate = richMediaInteractionRate;
	}

	public double getRichMediaAverageInteractionTime() {
		return richMediaAverageInteractionTime;
	}

	public void setRichMediaAverageInteractionTime(
			double richMediaAverageInteractionTime) {
		this.richMediaAverageInteractionTime = richMediaAverageInteractionTime;
	}

	public long getRichMediaInteractionImpressions() {
		return richMediaInteractionImpressions;
	}

	public void setRichMediaInteractionImpressions(
			long richMediaInteractionImpressions) {
		this.richMediaInteractionImpressions = richMediaInteractionImpressions;
	}

	public long getRichMediaManualCloses() {
		return richMediaManualCloses;
	}

	public void setRichMediaManualCloses(long richMediaManualCloses) {
		this.richMediaManualCloses = richMediaManualCloses;
	}

	public long getRichMediaFullScreenImpressions() {
		return richMediaFullScreenImpressions;
	}

	public void setRichMediaFullScreenImpressions(
			long richMediaFullScreenImpressions) {
		this.richMediaFullScreenImpressions = richMediaFullScreenImpressions;
	}

	public long getRichMediaVideoInteractions() {
		return richMediaVideoInteractions;
	}

	public void setRichMediaVideoInteractions(long richMediaVideoInteractions) {
		this.richMediaVideoInteractions = richMediaVideoInteractions;
	}

	public double getRichMediaVideoInteractionRate() {
		return richMediaVideoInteractionRate;
	}

	public void setRichMediaVideoInteractionRate(
			double richMediaVideoInteractionRate) {
		this.richMediaVideoInteractionRate = richMediaVideoInteractionRate;
	}

	public long getRichMediaVideoMutes() {
		return richMediaVideoMutes;
	}

	public void setRichMediaVideoMutes(long richMediaVideoMutes) {
		this.richMediaVideoMutes = richMediaVideoMutes;
	}

	public long getRichMediaVideoPauses() {
		return richMediaVideoPauses;
	}

	public void setRichMediaVideoPauses(long richMediaVideoPauses) {
		this.richMediaVideoPauses = richMediaVideoPauses;
	}

	public long getRichMediaVideoPlayes() {
		return richMediaVideoPlayes;
	}

	public void setRichMediaVideoPlayes(long richMediaVideoPlayes) {
		this.richMediaVideoPlayes = richMediaVideoPlayes;
	}

	public long getRichMediaVideoMidpoints() {
		return richMediaVideoMidpoints;
	}

	public void setRichMediaVideoMidpoints(long richMediaVideoMidpoints) {
		this.richMediaVideoMidpoints = richMediaVideoMidpoints;
	}

	public long getRichMediaVideoCompletes() {
		return richMediaVideoCompletes;
	}

	public void setRichMediaVideoCompletes(long richMediaVideoCompletes) {
		this.richMediaVideoCompletes = richMediaVideoCompletes;
	}

	public long getRichMediaVideoReplays() {
		return richMediaVideoReplays;
	}

	public void setRichMediaVideoReplays(long richMediaVideoReplays) {
		this.richMediaVideoReplays = richMediaVideoReplays;
	}

	public long getRichMediaVideoStops() {
		return richMediaVideoStops;
	}

	public void setRichMediaVideoStops(long richMediaVideoStops) {
		this.richMediaVideoStops = richMediaVideoStops;
	}

	public long getRichMediaVideoUnmutes() {
		return richMediaVideoUnmutes;
	}

	public void setRichMediaVideoUnmutes(long richMediaVideoUnmutes) {
		this.richMediaVideoUnmutes = richMediaVideoUnmutes;
	}

	public double getRichMediaAverageDisplayTime() {
		return richMediaAverageDisplayTime;
	}

	public void setRichMediaAverageDisplayTime(double richMediaAverageDisplayTime) {
		this.richMediaAverageDisplayTime = richMediaAverageDisplayTime;
	}

	public double getRichMediaVideoViewRate() {
		return richMediaVideoViewRate;
	}

	public void setRichMediaVideoViewRate(double richMediaVideoViewRate) {
		this.richMediaVideoViewRate = richMediaVideoViewRate;
	}

	public double getRichMediaVideoViewTime() {
		return richMediaVideoViewTime;
	}

	public void setRichMediaVideoViewTime(double richMediaVideoViewTime) {
		this.richMediaVideoViewTime = richMediaVideoViewTime;
	}

	public double getViewRate() {
		return viewRate;
	}

	public void setViewRate(double viewRate) {
		this.viewRate = viewRate;
	}

	public long getRichMediaBackupImages() {
		return richMediaBackupImages;
	}

	public void setRichMediaBackupImages(long richMediaBackupImages) {
		this.richMediaBackupImages = richMediaBackupImages;
	}

	public double getRichMediaDisplayTime() {
		return richMediaDisplayTime;
	}

	public void setRichMediaDisplayTime(double richMediaDisplayTime) {
		this.richMediaDisplayTime = richMediaDisplayTime;
	}
	
	

		
	
}
