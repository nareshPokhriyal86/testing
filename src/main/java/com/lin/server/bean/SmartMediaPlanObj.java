package com.lin.server.bean;


import java.io.Serializable;
import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.lin.web.dto.ProductDTO;
import com.lin.web.dto.SmartCampaignPlacementDTO;

/*
 * @author Youdhveer Panwar
 * This entity is used to create Smart Media Plan
 */

@SuppressWarnings("serial")
@Entity
@Index
public class SmartMediaPlanObj implements Serializable {
	
	@Id	private Long id;	
	private String campaignName;
	private String campaignId;
	private String campaignStatus;
	private long localAdvertiserId;
	private long localAgencyId;
	private double budget;
	private long impression;
    private double eCPM;
    private double cost;
    private String servingFee;
    private String netRevenue;
    private String startDate;
    private String endDate;
   
    private List<ProductDTO> products;
	private List<SmartCampaignPlacementDTO> placements;
	private long dfpOrderId;
	private String dfpOrderName;
	
	private List<SmartCampaignPlacementDTO> dfpPlacements;
	
	private int active; // 0-Inactive, 1-active, 2-Need Update
	private String updatedOn;
	
	public SmartMediaPlanObj() {
		
	}

	public SmartMediaPlanObj(Long id, String campaignName, String campaignId,
			double budget, long impression, double eCPM, double cost,
			String servingFee, String netRevenue, List<ProductDTO> products,
			List<SmartCampaignPlacementDTO> placements,int active,String updatedOn) {
		this.id = id;
		this.campaignName = campaignName;
		this.campaignId = campaignId;
		this.budget = budget;
		this.impression = impression;
		this.eCPM = eCPM;
		this.cost = cost;
		this.servingFee = servingFee;
		this.netRevenue = netRevenue;
		this.products = products;
		this.placements = placements;
		this.active=active;
		this.updatedOn=updatedOn;
	}

	


	public SmartMediaPlanObj(Long id, String campaignName, String campaignId,
			String campaignStatus, long localAdvertiserId, long localAgencyId,
			double budget, long impression, double eCPM, double cost,
			String servingFee, String netRevenue, String startDate,
			String endDate, List<ProductDTO> products,
			List<SmartCampaignPlacementDTO> placements, long dfpOrderId,
			String dfpOrderName, List<SmartCampaignPlacementDTO> dfpPlacements,
			int active,String updatedOn) {
		this.id = id;
		this.campaignName = campaignName;
		this.campaignId = campaignId;
		this.campaignStatus = campaignStatus;
		this.localAdvertiserId = localAdvertiserId;
		this.localAgencyId = localAgencyId;
		this.budget = budget;
		this.impression = impression;
		this.eCPM = eCPM;
		this.cost = cost;
		this.servingFee = servingFee;
		this.netRevenue = netRevenue;
		this.startDate = startDate;
		this.endDate = endDate;
		this.products = products;
		this.placements = placements;
		this.dfpOrderId = dfpOrderId;
		this.dfpOrderName = dfpOrderName;
		this.dfpPlacements = dfpPlacements;
		this.active = active;
		this.updatedOn=updatedOn;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SmartMediaPlanObj [id=");
		builder.append(id);
		builder.append(", campaignName=");
		builder.append(campaignName);
		builder.append(", campaignId=");
		builder.append(campaignId);
		builder.append(", campaignStatus=");
		builder.append(campaignStatus);
		builder.append(", localAdvertiserId=");
		builder.append(localAdvertiserId);
		builder.append(", localAgencyId=");
		builder.append(localAgencyId);
		builder.append(", budget=");
		builder.append(budget);
		builder.append(", impression=");
		builder.append(impression);
		builder.append(", eCPM=");
		builder.append(eCPM);
		builder.append(", cost=");
		builder.append(cost);
		builder.append(", servingFee=");
		builder.append(servingFee);
		builder.append(", netRevenue=");
		builder.append(netRevenue);
		builder.append(", startDate=");
		builder.append(startDate);
		builder.append(", endDate=");
		builder.append(endDate);
		builder.append(", products=");
		builder.append(products);
		builder.append(", placements=");
		builder.append(placements);
		builder.append(", dfpOrderId=");
		builder.append(dfpOrderId);
		builder.append(", dfpOrderName=");
		builder.append(dfpOrderName);
		builder.append(", dfpPlacements=");
		builder.append(dfpPlacements);
		builder.append(", active=");
		builder.append(active);
		builder.append(", createdOn=");
		builder.append(updatedOn);
		builder.append("]");
		return builder.toString();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public String getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}

	public double getBudget() {
		return budget;
	}

	public void setBudget(double budget) {
		this.budget = budget;
	}

	public long getImpression() {
		return impression;
	}

	public void setImpression(long impression) {
		this.impression = impression;
	}

	public double geteCPM() {
		return eCPM;
	}

	public void seteCPM(double eCPM) {
		this.eCPM = eCPM;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public String getServingFee() {
		return servingFee;
	}

	public void setServingFee(String servingFee) {
		this.servingFee = servingFee;
	}

	public String getNetRevenue() {
		return netRevenue;
	}

	public void setNetRevenue(String netRevenue) {
		this.netRevenue = netRevenue;
	}

	public List<ProductDTO> getProducts() {
		return products;
	}

	public void setProducts(List<ProductDTO> products) {
		this.products = products;
	}

	public List<SmartCampaignPlacementDTO> getPlacements() {
		return placements;
	}

	public void setPlacements(List<SmartCampaignPlacementDTO> placements) {
		this.placements = placements;
	}

	public long getDfpOrderId() {
		return dfpOrderId;
	}

	public void setDfpOrderId(long dfpOrderId) {
		this.dfpOrderId = dfpOrderId;
	}

	public String getDfpOrderName() {
		return dfpOrderName;
	}

	public void setDfpOrderName(String dfpOrderName) {
		this.dfpOrderName = dfpOrderName;
	}

	public List<SmartCampaignPlacementDTO> getDfpPlacements() {
		return dfpPlacements;
	}

	public void setDfpPlacements(List<SmartCampaignPlacementDTO> dfpPlacements) {
		this.dfpPlacements = dfpPlacements;
	}

	public long getLocalAdvertiserId() {
		return localAdvertiserId;
	}

	public void setLocalAdvertiserId(long localAdvertiserId) {
		this.localAdvertiserId = localAdvertiserId;
	}

	public long getLocalAgencyId() {
		return localAgencyId;
	}

	public void setLocalAgencyId(long localAgencyId) {
		this.localAgencyId = localAgencyId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public String getCampaignStatus() {
		return campaignStatus;
	}

	public void setCampaignStatus(String campaignStatus) {
		this.campaignStatus = campaignStatus;
	}

	public String getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdateOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}

}