package com.lin.server.bean;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity
/*
 * @author Youdhveer Panwar
 * This entity is used in Media Planner to create industries in datastore
 */
@Index
public class IndustryObj implements Serializable {
	
	@Id	private Long id;	
	private long industryId;
	private String industryName;	
	private String createdBy;
	private String createdOn;
	
	public IndustryObj(){
	}

	public IndustryObj(long industryId, String industryName,
			String createdBy, String createdOn) {	
		this.industryId = industryId;
		this.industryName = industryName;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
	}
	
	
	public IndustryObj(Long id, long industryId, String industryName,
			String createdBy, String createdOn) {
		this.id = id;
		this.industryId = industryId;
		this.industryName = industryName;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("IndustryObj [id=").append(id).append(", industryId=")
				.append(industryId).append(", industryName=")
				.append(industryName).append(", createdBy=").append(createdBy)
				.append(", createdOn=").append(createdOn).append("]");
		return builder.toString();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getIndustryId() {
		return industryId;
	}

	public void setIndustryId(long industryId) {
		this.industryId = industryId;
	}

	public String getIndustryName() {
		return industryName;
	}

	public void setIndustryName(String industryName) {
		this.industryName = industryName;
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
