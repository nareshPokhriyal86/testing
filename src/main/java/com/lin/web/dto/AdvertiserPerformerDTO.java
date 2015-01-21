package com.lin.web.dto;

import java.io.Serializable;
import java.util.List;

public class AdvertiserPerformerDTO implements Serializable{
	
	/*private List<String> campaignLineItems;
	private List<Long> impressionDelivered;
	private List<Long> clicks;
	private List<Double> CTR;*/
	
	private Long lineItemId;
	private String campaignLineItem;
	private Long impressionDelivered;
	private Long clicks;
	private Double CTR;
	private Long bookedImpressions;
	private Double deliveryIndicator;
	private Double changeInTimePeriod;
	private Double changeInLifeTime;
	private String date;
	private String campaignIO;
	private Double budget;
	private Double spent;
	private Double balance;
	private Double revenueDeliverd;
	private Double lifeTimeCTR;
	private String agency;
	private String advertiser;
	
	private String customizedCustomEvent;
	private String customEvent;
	private String value;
	private String market;
	private String siteName;
	private String campaignCategory;
	private String ClickToCalls;
	private String URL;
	private String FindStore;
	private String CouponsDownloads;
	private String site;
	private String creativeType;
	private String publisherName;
	private Double percentageTimeElapsed;
	private int lineItemCountFlag;
	private List<CommonDTO> allRichMediaEvents;
	private String endDateTime;
	

	public AdvertiserPerformerDTO() {
		
	}

	public AdvertiserPerformerDTO(Long lineItemId, Double lifeTimeCTR,Long bookedImpressions,Double budget,Double percentageTimeElapsed) {
		this.lineItemId = lineItemId;
		this.lifeTimeCTR = lifeTimeCTR;
		this.bookedImpressions = bookedImpressions;
		this.budget = budget;
		this.percentageTimeElapsed = percentageTimeElapsed;
	}
	
	public AdvertiserPerformerDTO(Long lineItemId, Double lifeTimeCTR,Long bookedImpressions,Double budget) {
		this.lineItemId = lineItemId;
		this.lifeTimeCTR = lifeTimeCTR;
		this.bookedImpressions = bookedImpressions;
		this.budget = budget;
	}
	
	public AdvertiserPerformerDTO(String campaignLineItem, Long impressionDelivered, Long clicks, Double cTR) {
		this.campaignLineItem = campaignLineItem;
		this.impressionDelivered = impressionDelivered;
		this.clicks = clicks;
		CTR = cTR;
	}
	
	public AdvertiserPerformerDTO(String campaignIO, Long lineItemId, String campaignLineItem, Long impressionDelivered, Long clicks, Double cTR, Double revenueDeliverd,String advertiser, String agency, String creativeType, String market,double deliveryIndicator, String site, long bookedImpressions,double budget,String publisherName,String siteName,String endDateTime) {
		this.campaignIO = campaignIO;
		this.lineItemId = lineItemId;
		this.campaignLineItem = campaignLineItem;
		this.impressionDelivered = impressionDelivered;
		this.clicks = clicks;
		this.CTR = cTR;
		this.revenueDeliverd = revenueDeliverd;
		this.agency = agency;
		this.advertiser = advertiser;
		this.creativeType = creativeType;
		this.market = market;
		this.deliveryIndicator = deliveryIndicator;
		this.site = site;
		this.bookedImpressions = bookedImpressions;
		this.budget = budget;
		this.publisherName = publisherName;
		this.siteName = siteName;
		this.endDateTime = endDateTime;
	}
	
	public AdvertiserPerformerDTO(String campaignIO, Long lineItemId, String campaignLineItem, Long impressionDelivered, Long clicks, Double cTR, Double revenueDeliverd,String advertiser, String agency,String publisherName,String siteName) {
		this.campaignIO = campaignIO;
		this.lineItemId = lineItemId;
		this.campaignLineItem = campaignLineItem;
		this.impressionDelivered = impressionDelivered;
		this.clicks = clicks;
		this.CTR = cTR;
		this.revenueDeliverd = revenueDeliverd;
		this.agency = agency;
		this.advertiser = advertiser;
		this.publisherName = publisherName;
		this.siteName = siteName;
	}
	
	public AdvertiserPerformerDTO(String campaignIO, Long lineItemId, String campaignLineItem, Long impressionDelivered, Long clicks, Double cTR, Double revenueDeliverd,String advertiser, String agency) {
		this.campaignIO = campaignIO;
		this.lineItemId = lineItemId;
		this.campaignLineItem = campaignLineItem;
		this.impressionDelivered = impressionDelivered;
		this.clicks = clicks;
		this.CTR = cTR;
		this.revenueDeliverd = revenueDeliverd;
		this.agency = agency;
		this.advertiser = advertiser;
		
	}

	public String getCampaignLineItem() {
		return campaignLineItem;
	}

	public void setCampaignLineItem(String campaignLineItem) {
		this.campaignLineItem = campaignLineItem;
	}

	public Long getImpressionDelivered() {
		return impressionDelivered;
	}

	public void setImpressionDelivered(Long impressionDelivered) {
		this.impressionDelivered = impressionDelivered;
	}

	public Long getClicks() {
		return clicks;
	}

	public void setClicks(Long clicks) {
		this.clicks = clicks;
	}

	public Double getCTR() {
		return CTR;
	}

	public void setCTR(Double cTR) {
		CTR = cTR;
	}

	public void setBookedImpressions(Long bookedImpressions) {
		this.bookedImpressions = bookedImpressions;
	}

	public Long getBookedImpressions() {
		return bookedImpressions;
	}

	public void setDeliveryIndicator(Double deliveryIndicator) {
		this.deliveryIndicator = deliveryIndicator;
	}

	public Double getDeliveryIndicator() {
		return deliveryIndicator;
	}

	public void setChangeInTimePeriod(Double changeInTimePeriod) {
		this.changeInTimePeriod = changeInTimePeriod;
	}

	public Double getChangeInTimePeriod() {
		return changeInTimePeriod;
	}

	public void setChangeInLifeTime(Double changeInLifeTime) {
		this.changeInLifeTime = changeInLifeTime;
	}

	public Double getChangeInLifeTime() {
		return changeInLifeTime;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDate() {
		return date;
	}

	public void setCampaignIO(String campaignIO) {
		this.campaignIO = campaignIO;
	}

	public String getCampaignIO() {
		return campaignIO;
	}

	public void setSpent(Double spent) {
		this.spent = spent;
	}

	public Double getSpent() {
		return spent;
	}

	public void setBudget(Double budget) {
		this.budget = budget;
	}

	public Double getBudget() {
		return budget;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Double getBalance() {
		return balance;
	}

	public void setRevenueDeliverd(Double revenueDeliverd) {
		this.revenueDeliverd = revenueDeliverd;
	}

	public Double getRevenueDeliverd() {
		return revenueDeliverd;
	}

	public Double getLifeTimeCTR() {
		return lifeTimeCTR;
	}

	public void setLifeTimeCTR(Double lifeTimeCTR) {
		this.lifeTimeCTR = lifeTimeCTR;
	}

	public Long getLineItemId() {
		return lineItemId;
	}

	public void setLineItemId(Long lineItemId) {
		this.lineItemId = lineItemId;
	}

	public String getAgency() {
		return agency;
	}

	public void setAgency(String agency) {
		this.agency = agency;
	}

	public String getAdvertiser() {
		return advertiser;
	}

	public void setAdvertiser(String advertiser) {
		this.advertiser = advertiser;
	}

	public void setCustomEvent(String customEvent) {
		this.customEvent = customEvent;
	}

	public String getCustomEvent() {
		return customEvent;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setMarket(String market) {
		this.market = market;
	}

	public String getMarket() {
		return market;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setCampaignCategory(String campaignCategory) {
		this.campaignCategory = campaignCategory;
	}

	public String getCampaignCategory() {
		return campaignCategory;
	}

	public void setClickToCalls(String clickToCalls) {
		ClickToCalls = clickToCalls;
	}

	public String getClickToCalls() {
		return ClickToCalls;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public String getURL() {
		return URL;
	}

	public void setCouponsDownloads(String couponsDownloads) {
		CouponsDownloads = couponsDownloads;
	}

	public String getCouponsDownloads() {
		return CouponsDownloads;
	}

	public void setFindStore(String findStore) {
		FindStore = findStore;
	}

	public String getFindStore() {
		return FindStore;
	}

	public void setCustomizedCustomEvent(String customizedCustomEvent) {
		this.customizedCustomEvent = customizedCustomEvent;
	}

	public String getCustomizedCustomEvent() {
		return customizedCustomEvent;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getSite() {
		return site;
	}

	public void setCreativeType(String creativeType) {
		this.creativeType = creativeType;
	}

	public String getCreativeType() {
		return creativeType;
	}

	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}

	public String getPublisherName() {
		return publisherName;
	}

	public void setAllRichMediaEvents(List<CommonDTO> allRichMediaEvents) {
		this.allRichMediaEvents = allRichMediaEvents;
	}

	public List<CommonDTO> getAllRichMediaEvents() {
		return allRichMediaEvents;
	}

	public void setPercentageTimeElapsed(Double percentageTimeElapsed) {
		this.percentageTimeElapsed = percentageTimeElapsed;
	}

	public Double getPercentageTimeElapsed() {
		return percentageTimeElapsed;
	}

	
	public void setLineItemCountFlag(int lineItemCountFlag) {
		this.lineItemCountFlag = lineItemCountFlag;
	}

	public int getLineItemCountFlag() {
		return lineItemCountFlag;
	}

	public String getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(String endDateTime) {
		this.endDateTime = endDateTime;
	}

	
	
	
}
