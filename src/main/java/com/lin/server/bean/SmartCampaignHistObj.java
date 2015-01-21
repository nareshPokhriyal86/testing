package com.lin.server.bean;

import java.io.Serializable;
import java.util.Date;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;


@Entity
@Index
public class SmartCampaignHistObj implements Serializable{

	@Id private Long id;
	private long campaignId;
	private String campaignName;
	private long placementId;
	private String placementName;
	
    private Date updatedOn;
    private long updatedByUserId;
    private String updatedByUserName;
    private String changeLog;
    
	public SmartCampaignHistObj() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(long campaignId) {
		this.campaignId = campaignId;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public long getUpdatedByUserId() {
		return updatedByUserId;
	}

	public void setUpdatedByUserId(long updatedByUserId) {
		this.updatedByUserId = updatedByUserId;
	}

	public String getUpdatedByUserName() {
		return updatedByUserName;
	}

	public void setUpdatedByUserName(String updatedByUserName) {
		this.updatedByUserName = updatedByUserName;
	}

	public String getChangeLog() {
		return changeLog;
	}

	public void setChangeLog(String changeLog) {
		this.changeLog = changeLog;
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public long getPlacementId() {
		return placementId;
	}

	public void setPlacementId(long placementId) {
		this.placementId = placementId;
	}

	public String getPlacementName() {
		return placementName;
	}

	public void setPlacementName(String placementName) {
		this.placementName = placementName;
	}
	

}
