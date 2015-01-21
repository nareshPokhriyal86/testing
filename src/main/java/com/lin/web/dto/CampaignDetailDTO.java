package com.lin.web.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/*
 * @author Naresh Pokhriyal
 */

public class CampaignDetailDTO implements Serializable{
	
	private String campaignName;
	private String allocationName;
	private String placement;
	private String dealType;
	private String geoTarget;
	private String adUnits;
	private String richMedia;
	private Date startDate;
	private Date endDate;
	private String javaScriptTag;
	private long impression;
	private double rate;
	private double campaignCost;
	private List<JavaScriptTagDTO> javaScriptTagListForDfp;
	private List<JavaScriptTagDTO> javaScriptTagListForNonDfp;
	
	public CampaignDetailDTO() {
	}

	public CampaignDetailDTO(String campaignName,
			String allocationName, String placement, String dealType,
			String geoTarget, String adUnits, String richMedia,
			Date startDate, Date endDate, String javaScriptTag,
			long impression, double rate, double campaignCost) {
		this.campaignName = campaignName;
		this.allocationName = allocationName;
		this.placement = placement;
		this.dealType = dealType;
		this.geoTarget = geoTarget;
		this.adUnits = adUnits;
		this.richMedia = richMedia;
		this.startDate = startDate;
		this.endDate = endDate;
		this.javaScriptTag = javaScriptTag;
		this.impression = impression;
		this.rate = rate;
		this.campaignCost = campaignCost;
	}

	@Override
	public String toString() {
		return "CampaignDetailDTO [campaignName="
				+ campaignName + ", allocationName=" + allocationName
				+ ", placement=" + placement + ", dealType=" + dealType
				+ ", geoTarget=" + geoTarget + ", adUnits=" + adUnits
				+ ", richMedia=" + richMedia + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", javaScriptTag=" + javaScriptTag
				+ ", impression=" + impression + ", rate=" + rate
				+ ", campaignCost=" + campaignCost + "]";
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public String getAllocationName() {
		return allocationName;
	}

	public void setAllocationName(String allocationName) {
		this.allocationName = allocationName;
	}

	public String getPlacement() {
		return placement;
	}

	public void setPlacement(String placement) {
		this.placement = placement;
	}

	public String getDealType() {
		return dealType;
	}

	public void setDealType(String dealType) {
		this.dealType = dealType;
	}

	public String getGeoTarget() {
		return geoTarget;
	}

	public void setGeoTarget(String geoTarget) {
		this.geoTarget = geoTarget;
	}

	public String getAdUnits() {
		return adUnits;
	}

	public void setAdUnits(String adUnits) {
		this.adUnits = adUnits;
	}

	public String getRichMedia() {
		return richMedia;
	}

	public void setRichMedia(String richMedia) {
		this.richMedia = richMedia;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public long getImpression() {
		return impression;
	}

	public void setImpression(long impression) {
		this.impression = impression;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public double getCampaignCost() {
		return campaignCost;
	}

	public void setCampaignCost(double campaignCost) {
		this.campaignCost = campaignCost;
	}

	public String getJavaScriptTag() {
		return javaScriptTag;
	}

	public void setJavaScriptTag(String javaScriptTag) {
		this.javaScriptTag = javaScriptTag;
	}

	public List<JavaScriptTagDTO> getJavaScriptTagListForDfp() {
		return javaScriptTagListForDfp;
	}

	public void setJavaScriptTagListForDfp(
			List<JavaScriptTagDTO> javaScriptTagListForDfp) {
		this.javaScriptTagListForDfp = javaScriptTagListForDfp;
	}

	public List<JavaScriptTagDTO> getJavaScriptTagListForNonDfp() {
		return javaScriptTagListForNonDfp;
	}

	public void setJavaScriptTagListForNonDfp(
			List<JavaScriptTagDTO> javaScriptTagListForNonDfp) {
		this.javaScriptTagListForNonDfp = javaScriptTagListForNonDfp;
	}


}
