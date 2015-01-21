package com.lin.server.bean;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity

/*
 * @author Youdhveer Panwar
 * This entity is used in Media Planner to create custom adFormats in datastore
 */
@Index
public class AdFormatObj implements Serializable {
	
	@Id	private Long id;	
	private long adFormatId;
	private String adFormatName;	
	private String createdBy;
	private String createdOn;
	
	public AdFormatObj(){
	}

	public AdFormatObj(Long id, long adFormatId, String adFormatName,
			String createdBy, String createdOn) {
		this.id = id;
		this.adFormatId = adFormatId;
		this.adFormatName = adFormatName;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
	}

	public AdFormatObj(String adFormatName,String createdBy) {		
		this.adFormatName = adFormatName;
		this.createdBy = createdBy;
	}
	
	public AdFormatObj(long adFormatId, String adFormatName,
			String createdBy, String createdOn) {		
		this.adFormatId = adFormatId;
		this.adFormatName = adFormatName;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AdFormatObj [id=").append(id).append(", adFormatId=")
				.append(adFormatId).append(", adFormatName=")
				.append(adFormatName).append(", createdBy=").append(createdBy)
				.append(", createdOn=").append(createdOn).append("]");
		return builder.toString();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getAdFormatId() {
		return adFormatId;
	}

	public void setAdFormatId(long adFormatId) {
		this.adFormatId = adFormatId;
	}

	public String getAdFormatName() {
		return adFormatName;
	}

	public void setAdFormatName(String adFormatName) {
		this.adFormatName = adFormatName;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	
	
	
}
