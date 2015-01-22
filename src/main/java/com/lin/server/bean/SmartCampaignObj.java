package com.lin.server.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.lin.persistance.dao.impl.PerformanceMonitoringDAO;

@Entity
/*
 * It is a Smart Campaign entity bean for datastore
 * 
 * @author Shubham Goel
 */
@Index
public class SmartCampaignObj implements Serializable {
	private static final Logger log = Logger.getLogger(SmartCampaignObj.class.getName());

	@Id
	private Long id;
	private Long campaignId;
	private Long userId;
	private String adServerId;
	private String name;

	private String startDate;
	private String endDate;
	// Changed by Anup | Requirement was to sort by date
	private Date sDate;
	private Date eDate;

	private String companyName;
	private String adServerUsername;
	private String companyId;
	private Date lastUpdatedOn;
	private String notes;
	private String campaignStatus;
	private String advertiserId;
	private String agencyId;
	private List<DropdownDataObj> rateTypeList;

	private long dfpOrderId;
	private String dfpOrderName;
	private String dfpStatus;

	private boolean isProcessing;
	private boolean hasMediaPlan;
	private boolean isSetupOnDFP;

	private boolean isMigrated;
	private boolean isHistoryLoaded;
	private Integer mediaPlanType=0;
	private int historicalStatus = 0 ; 

	/*
	 * private String publisherId; private String publisherName;
	 */

	public SmartCampaignObj() {

	}

	// copy Constructor
	public SmartCampaignObj(SmartCampaignObj obj) {
		this.id = obj.id;
		this.campaignId = obj.campaignId;
		this.userId = obj.userId;
		this.adServerId = obj.adServerId;
		this.name = obj.name;
		this.startDate = obj.startDate;
		this.endDate = obj.endDate;
		this.companyName = obj.companyName;
		this.adServerUsername = obj.adServerUsername;
		this.companyId = obj.companyId;
		this.lastUpdatedOn = obj.lastUpdatedOn;
		this.notes = obj.notes;
		this.campaignStatus = obj.campaignStatus;
		this.advertiserId = obj.advertiserId;
		this.agencyId = obj.agencyId;
		this.rateTypeList = obj.rateTypeList;
		this.dfpOrderId = obj.dfpOrderId;
		this.dfpOrderName = obj.dfpOrderName;
		this.dfpStatus = obj.dfpStatus;
		this.isProcessing = obj.isProcessing;
		this.hasMediaPlan = obj.hasMediaPlan;
		this.isSetupOnDFP = obj.isSetupOnDFP;
		this.isMigrated = obj.isMigrated;
		this.isHistoryLoaded = obj.isHistoryLoaded;

		// Added By Anup
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
			if (obj.sDate != null) {
				this.sDate = obj.sDate;
			} else {
				this.sDate = formatter.parse(obj.startDate);
			}

			if (obj.eDate != null) {
				this.eDate = obj.eDate;
			} else {
				this.eDate = formatter.parse(obj.endDate);
			}

		} catch (Exception e) {
			log.severe("Error Ocurred: "+ e.getMessage());
		}
	}

	public SmartCampaignObj(Long id, Long campaignId, Long userId,
			String adServerId, String name, String startDate, String endDate,
			String companyName, String adServerUsername, String companyId,
			Date lastUpdatedOn, String notes, String campaignStatus,
			String advertiserId, String agencyId,
			List<DropdownDataObj> rateTypeList, int mediaPlanStatus,
			int mediaPlanEditStatus, long dfpOrderId, String dfpOrderName,
			boolean isProcessing, boolean hasMediaPlan, boolean isSetupOnDFP) {
		this.id = id;
		this.campaignId = campaignId;
		this.userId = userId;
		this.adServerId = adServerId;
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.companyName = companyName;
		this.adServerUsername = adServerUsername;
		this.companyId = companyId;
		this.lastUpdatedOn = lastUpdatedOn;
		this.notes = notes;
		this.campaignStatus = campaignStatus;
		this.advertiserId = advertiserId;
		this.agencyId = agencyId;
		this.rateTypeList = rateTypeList;
		this.dfpOrderId = dfpOrderId;
		this.dfpOrderName = dfpOrderName;
		this.isProcessing = isProcessing;
		this.hasMediaPlan = hasMediaPlan;
		this.isSetupOnDFP = isSetupOnDFP;

		// Added By Anup | to sort campaigns by date
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
			this.sDate = formatter.parse(startDate);
			this.eDate = formatter.parse(endDate);
		} catch (Exception e) {
			log.severe("Error Ocurred: "+ e.getMessage());
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SmartCampaignObj [id=");
		builder.append(id);
		builder.append(", campaignId=");
		builder.append(campaignId);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", adServerId=");
		builder.append(adServerId);
		builder.append(", name=");
		builder.append(name);
		builder.append(", startDate=");
		builder.append(startDate);
		builder.append(", endDate=");
		builder.append(endDate);
		builder.append(", companyName=");
		builder.append(companyName);
		builder.append(", adServerUsername=");
		builder.append(adServerUsername);
		builder.append(", companyId=");
		builder.append(companyId);
		builder.append(", lastUpdatedOn=");
		builder.append(lastUpdatedOn);
		builder.append(", notes=");
		builder.append(notes);
		builder.append(", campaignStatus=");
		builder.append(campaignStatus);
		builder.append(", advertiserId=");
		builder.append(advertiserId);
		builder.append(", agencyId=");
		builder.append(agencyId);
		builder.append(", rateTypeList=");
		builder.append(rateTypeList);
		builder.append(", dfpOrderId=");
		builder.append(dfpOrderId);
		builder.append(", dfpOrderName=");
		builder.append(dfpOrderName);
		builder.append(", isProcessing=");
		builder.append(isProcessing);
		builder.append(", hasMediaPlan=");
		builder.append(hasMediaPlan);
		builder.append(", isSetupOnDFP=");
		builder.append(isSetupOnDFP);
		builder.append("]");
		return builder.toString();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Long campaignId) {
		this.campaignId = campaignId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
		// Added By Anup
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
			this.sDate = formatter.parse(startDate);
		} catch (Exception e) {
			log.severe("Error Ocurred: "+ e.getMessage());
		}
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
		// Added By Anup
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
			this.eDate = formatter.parse(endDate);
		} catch (Exception e) {
			log.severe("Error Ocurred: "+ e.getMessage());
		}
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getCampaignStatus() {
		return campaignStatus;
	}

	public void setCampaignStatus(String campaignStatus) {
		this.campaignStatus = campaignStatus;
	}

	public List<DropdownDataObj> getRateTypeList() {
		return rateTypeList;
	}

	public void setRateTypeList(List<DropdownDataObj> rateTypeList) {
		this.rateTypeList = rateTypeList;
	}

	public String getAdvertiserId() {
		return advertiserId;
	}

	public void setAdvertiserId(String advertiserId) {
		this.advertiserId = advertiserId;
	}

	public String getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getAdServerId() {
		return adServerId;
	}

	public void setAdServerId(String adServerId) {
		this.adServerId = adServerId;
	}

	public String getAdServerUsername() {
		return adServerUsername;
	}

	public void setAdServerUsername(String adServerUsername) {
		this.adServerUsername = adServerUsername;
	}

	public Date getLastUpdatedOn() {
		return lastUpdatedOn;
	}

	public void setLastUpdatedOn(Date lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
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

	public boolean isProcessing() {
		return isProcessing;
	}

	public void setProcessing(boolean isProcessing) {
		this.isProcessing = isProcessing;
	}

	public boolean isHasMediaPlan() {
		return hasMediaPlan;
	}

	public void setHasMediaPlan(boolean hasMediaPlan) {
		this.hasMediaPlan = hasMediaPlan;
	}

	public boolean isSetupOnDFP() {
		return isSetupOnDFP;
	}

	public void setSetupOnDFP(boolean isSetupOnDFP) {
		this.isSetupOnDFP = isSetupOnDFP;
	}

	public String getDfpStatus() {
		return dfpStatus;
	}

	public void setDfpStatus(String dfpStatus) {
		this.dfpStatus = dfpStatus;
	}

	public boolean isMigrated() {
		return isMigrated;
	}

	public void setMigrated(boolean isMigrated) {
		this.isMigrated = isMigrated;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public boolean isHistoryLoaded() {
		return isHistoryLoaded;
	}

	public void setHistoryLoaded(boolean isHistoryLoaded) {
		this.isHistoryLoaded = isHistoryLoaded;
	}

	public Date getsDate() {
		return sDate;
	}

	public Date geteDate() {
		return eDate;
	}

	public void setsDate(Date sDate) {
		this.sDate = sDate;
	}

	public void seteDate(Date eDate) {
		this.eDate = eDate;
	}

	public void setsDate(String sDate) {
		try{
			SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
			this.sDate = formatter.parse(sDate);
		}catch(Exception e){
			
		}
	}

	public void seteDate(String eDate) {
		try{
			SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
			this.eDate = formatter.parse(eDate);
		}catch(Exception e){
			
		}
	} 
	/*
	  public String getPublisherId() { return publisherId; }
	 
	  
	 public void setPublisherId(String publisherId) { this.publisherId =
	  publisherId; }
	  
	  
	  public String getPublisherName() { return publisherName; }
	  
	  
	 public void setPublisherName(String publisherName) { this.publisherName =
	  publisherName; }
	 */

	public Integer getMediaPlanType() {
		return mediaPlanType;
	}

	public void setMediaPlanType(Integer mediaPlanType) {
		this.mediaPlanType = mediaPlanType;
	}

}
