package com.lin.server.bean;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

/*
 * It is a PlacementObj entity bean 
 * referenced by ProposalObj
 *  
 * @author Youdhveer Panwar
 */
@SuppressWarnings("serial")
@Entity
@Index
public class PlacementObj implements Serializable {
	@Id	private String id;
	
	private String placementId;	
	private long proposalId;
	
	private String placementName;	
	private String createdOn;
	private String updatedOn;
	private String createdBy;
	private String updatedBy;	
	
	private String siteId;
	private String siteName;
	private String adSize;
	private String adFormat;
	private String adServer;
	private double publisherCPM;
	private double firstPartyAdServerCost;
	private double thirdPartyAdServerCost;
	private double totalCost;
	private double marginPercent;
	private double margin;
	private double priceQuote;
	private double budgetAllocation;	
	private double publisherPayout;
	private double grossRevenue;
	private double netRevenue;
	private long forcastedImpression;
	private long reservedImpression;
	private long availableImpression;
	private long proposedImpression;

	private long siteNumberPerPlacement;
	private long placementNumber;
	
	private double effectiveCPM;
	private double netCostCPM;
	private double netCost;
	private double servingFees;
	
	private String status;
	
	public PlacementObj(){
		
	}
	
	
	public PlacementObj(String id, long proposalId, String placementId,
			String placementName, String createdOn, String updatedOn,
			String createdBy, String updatedBy, String siteId, String siteName,
			String adSize, String adFormat, String adServer,
			double publisherCPM, double firstPartyAdServerCost,
			double thirdPartyAdServerCost, double totalCost,
			double marginPercent, double margin, double priceQuote,
			double budgetAllocation, double publisherPayout,
			double grossRevenue, double netRevenue, long forcastedImpression,
			long reservedImpression, long availableImpression,
			long proposedImpression,long siteNumberPerPlacement,long placementNumber) {
		this.id = id;
		this.proposalId = proposalId;
		this.placementId = placementId;
		this.placementName = placementName;
		this.createdOn = createdOn;
		this.updatedOn = updatedOn;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
		this.siteId = siteId;
		this.siteName = siteName;
		this.adSize = adSize;
		this.adFormat = adFormat;
		this.adServer = adServer;
		this.publisherCPM = publisherCPM;
		this.firstPartyAdServerCost = firstPartyAdServerCost;
		this.thirdPartyAdServerCost = thirdPartyAdServerCost;
		this.totalCost = totalCost;
		this.marginPercent = marginPercent;
		this.margin = margin;
		this.priceQuote = priceQuote;
		this.budgetAllocation = budgetAllocation;
		this.publisherPayout = publisherPayout;
		this.grossRevenue = grossRevenue;
		this.netRevenue = netRevenue;
		this.forcastedImpression = forcastedImpression;
		this.reservedImpression = reservedImpression;
		this.availableImpression = availableImpression;
		this.proposedImpression = proposedImpression;
		this.siteNumberPerPlacement=siteNumberPerPlacement;
		this.placementNumber=placementNumber;
	}
	
	public PlacementObj(String id, long proposalId, String placementId,
			String placementName, String createdOn, String updatedOn,
			String createdBy, String updatedBy, String siteId, String siteName,
			String adSize, String adFormat, String adServer,
			double publisherCPM, double firstPartyAdServerCost,
			double thirdPartyAdServerCost, double totalCost,
			double marginPercent, double margin, double priceQuote,
			double budgetAllocation, double publisherPayout,
			double grossRevenue, double netRevenue, long forcastedImpression,
			long reservedImpression, long availableImpression,
			long proposedImpression) {
		this.id = id;
		this.proposalId = proposalId;
		this.placementId = placementId;
		this.placementName = placementName;
		this.createdOn = createdOn;
		this.updatedOn = updatedOn;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
		this.siteId = siteId;
		this.siteName = siteName;
		this.adSize = adSize;
		this.adFormat = adFormat;
		this.adServer = adServer;
		this.publisherCPM = publisherCPM;
		this.firstPartyAdServerCost = firstPartyAdServerCost;
		this.thirdPartyAdServerCost = thirdPartyAdServerCost;
		this.totalCost = totalCost;
		this.marginPercent = marginPercent;
		this.margin = margin;
		this.priceQuote = priceQuote;
		this.budgetAllocation = budgetAllocation;
		this.publisherPayout = publisherPayout;
		this.grossRevenue = grossRevenue;
		this.netRevenue = netRevenue;
		this.forcastedImpression = forcastedImpression;
		this.reservedImpression = reservedImpression;
		this.availableImpression = availableImpression;
		this.proposedImpression = proposedImpression;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PlacementObj [id=").append(id).append(", proposalId=")
				.append(proposalId).append(", placementId=")
				.append(placementId).append(", placementName=")
				.append(placementName).append(", createdOn=").append(createdOn)
				.append(", updatedOn=").append(updatedOn)
				.append(", createdBy=").append(createdBy)
				.append(", updatedBy=").append(updatedBy).append(", siteId=")
				.append(siteId).append(", siteName=").append(siteName)
				.append(", adSize=").append(adSize).append(", adFormat=")
				.append(adFormat).append(", adServer=").append(adServer)
				.append(", publisherCPM=").append(publisherCPM)
				.append(", firstPartyAdServerCost=")
				.append(firstPartyAdServerCost)
				.append(", thirdPartyAdServerCost=")
				.append(thirdPartyAdServerCost).append(", totalCost=")
				.append(totalCost).append(", marginPercent=")
				.append(marginPercent).append(", margin=").append(margin)
				.append(", priceQuote=").append(priceQuote)
				.append(", budgetAllocation=").append(budgetAllocation)
				.append(", publisherPayout=").append(publisherPayout)
				.append(", grossRevenue=").append(grossRevenue)
				.append(", netRevenue=").append(netRevenue)
				.append(", forcastedImpression=").append(forcastedImpression)
				.append(", reservedImpression=").append(reservedImpression)
				.append(", availableImpression=").append(availableImpression)
				.append(", proposedImpression=").append(proposedImpression)
				.append(", siteNumberPerPlacement=").append(siteNumberPerPlacement)
				.append(", placementNumber=").append(placementNumber)				
				.append("]");
		return builder.toString();
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getProposalId() {
		return proposalId;
	}

	public void setProposalId(long proposalId) {
		this.proposalId = proposalId;
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

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
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


	public String getAdSize() {
		return adSize;
	}


	public void setAdSize(String adSize) {
		this.adSize = adSize;
	}


	public String getAdFormat() {
		return adFormat;
	}


	public void setAdFormat(String adFormat) {
		this.adFormat = adFormat;
	}


	public String getAdServer() {
		return adServer;
	}


	public void setAdServer(String adServer) {
		this.adServer = adServer;
	}


	public double getPublisherCPM() {
		return publisherCPM;
	}


	public void setPublisherCPM(double publisherCPM) {
		this.publisherCPM = publisherCPM;
	}


	public double getFirstPartyAdServerCost() {
		return firstPartyAdServerCost;
	}


	public void setFirstPartyAdServerCost(double firstPartyAdServerCost) {
		this.firstPartyAdServerCost = firstPartyAdServerCost;
	}


	public double getThirdPartyAdServerCost() {
		return thirdPartyAdServerCost;
	}


	public void setThirdPartyAdServerCost(double thirdPartyAdServerCost) {
		this.thirdPartyAdServerCost = thirdPartyAdServerCost;
	}


	public double getTotalCost() {
		return totalCost;
	}


	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}


	public double getMarginPercent() {
		return marginPercent;
	}


	public void setMarginPercent(double marginPercent) {
		this.marginPercent = marginPercent;
	}


	public double getMargin() {
		return margin;
	}


	public void setMargin(double margin) {
		this.margin = margin;
	}


	public double getPriceQuote() {
		return priceQuote;
	}


	public void setPriceQuote(double priceQuote) {
		this.priceQuote = priceQuote;
	}


	public double getBudgetAllocation() {
		return budgetAllocation;
	}


	public void setBudgetAllocation(double budgetAllocation) {
		this.budgetAllocation = budgetAllocation;
	}


	public double getPublisherPayout() {
		return publisherPayout;
	}


	public void setPublisherPayout(double publisherPayout) {
		this.publisherPayout = publisherPayout;
	}


	public double getGrossRevenue() {
		return grossRevenue;
	}


	public void setGrossRevenue(double grossRevenue) {
		this.grossRevenue = grossRevenue;
	}


	public double getNetRevenue() {
		return netRevenue;
	}


	public void setNetRevenue(double netRevenue) {
		this.netRevenue = netRevenue;
	}


	public long getForcastedImpression() {
		return forcastedImpression;
	}


	public void setForcastedImpression(long forcastedImpression) {
		this.forcastedImpression = forcastedImpression;
	}


	public long getReservedImpression() {
		return reservedImpression;
	}


	public void setReservedImpression(long reservedImpression) {
		this.reservedImpression = reservedImpression;
	}


	public long getAvailableImpression() {
		return availableImpression;
	}


	public void setAvailableImpression(long availableImpression) {
		this.availableImpression = availableImpression;
	}


	public long getProposedImpression() {
		return proposedImpression;
	}


	public void setProposedImpression(long proposedImpression) {
		this.proposedImpression = proposedImpression;
	}


	public void setSiteNumberPerPlacement(long siteNumberPerPlacement) {
		this.siteNumberPerPlacement = siteNumberPerPlacement;
	}


	public long getSiteNumberPerPlacement() {
		return siteNumberPerPlacement;
	}


	public void setPlacementNumber(long placementNumber) {
		this.placementNumber = placementNumber;
	}


	public long getPlacementNumber() {
		return placementNumber;
	}


	public void setEffectiveCPM(double effectiveCPM) {
		this.effectiveCPM = effectiveCPM;
	}


	public double getEffectiveCPM() {
		return effectiveCPM;
	}


	public void setNetCostCPM(double netCostCPM) {
		this.netCostCPM = netCostCPM;
	}


	public double getNetCostCPM() {
		return netCostCPM;
	}


	public void setNetCost(double netCost) {
		this.netCost = netCost;
	}


	public double getNetCost() {
		return netCost;
	}


	public void setServingFees(double servingFees) {
		this.servingFees = servingFees;
	}


	public double getServingFees() {
		return servingFees;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}
		

}
