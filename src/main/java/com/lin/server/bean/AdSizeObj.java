
package com.lin.server.bean;


import java.io.Serializable;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity
/*
 * @author Youdhveer Panwar
 * This entity is used in Media Planner to create custom AdSize in datastore
 */
@Index
public class AdSizeObj implements Serializable {
	
	@Id	private Long id;	
	private long adSizeId;
	private String adSize;	
	private String createdBy;
	private String createdOn;
	
	public AdSizeObj(){
	}

	
	public AdSizeObj(Long id, long adSizeId, String adSize, String createdBy,
			String createdOn) {
		this.id = id;
		this.adSizeId = adSizeId;
		this.adSize = adSize;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
	}

	
	public AdSizeObj(String adSize, String createdBy) {		
		this.adSize = adSize;
		this.createdBy = createdBy;
	}


	public AdSizeObj(long adSizeId, String adSize, String createdBy,
			String createdOn) {		
		this.adSizeId = adSizeId;
		this.adSize = adSize;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AdSizeObj [id=").append(id).append(", adSizeId=")
				.append(adSizeId).append(", adSize=").append(adSize)
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

	public long getAdSizeId() {
		return adSizeId;
	}

	public void setAdSizeId(long adSizeId) {
		this.adSizeId = adSizeId;
	}

	public String getAdSize() {
		return adSize;
	}

	public void setAdSize(String adSize) {
		this.adSize = adSize;
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