package com.lin.server.bean;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity
/*
 * @author Youdhveer Panwar
 * This entity is used in Media Planner to create KPIs in datastore
 */
@Index
public class KPIObj implements Serializable {
	
	@Id	private Long id;	
	private long kpiId;
	private String kpiName;	
	private String createdBy;
	private String createdOn;
	
	public KPIObj(){
	}

	public KPIObj(long kpiId, String kpiName, String createdBy,
			String createdOn) {		
		this.kpiId = kpiId;
		this.kpiName = kpiName;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
	}
	
	
	public KPIObj(Long id, long kpiId, String kpiName, String createdBy,
			String createdOn) {
		this.id = id;
		this.kpiId = kpiId;
		this.kpiName = kpiName;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("KPIObj [id=").append(id).append(", kpiId=")
				.append(kpiId).append(", kpiName=").append(kpiName)
				.append(", createdBy=").append(createdBy)
				.append(", createdOn=").append(createdOn).append("]");
		return builder.toString();
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getKpiId() {
		return kpiId;
	}

	public void setKpiId(long kpiId) {
		this.kpiId = kpiId;
	}

	public String getKpiName() {
		return kpiName;
	}

	public void setKpiName(String kpiName) {
		this.kpiName = kpiName;
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
