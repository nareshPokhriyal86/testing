package com.lin.server.bean;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;


@Entity

/*
 * @author Naresh Pokhriyal
 * This entity is used in Admin(Products) to save AdUnit HIerarchy of AdServers
 */

@Index
public class AdUnitHierarchy implements Serializable{
	@Id	private String id;				// adServerId_adUnitId
	private String adServerId;
	private String adUnitId;
	@Index private String adUnitName;
	private String pid;
	//private boolean isHasChildren;
	private String canonicalPath;
    
	public AdUnitHierarchy(){
	}

	
	public AdUnitHierarchy(String id, String adServerId, String adUnitId,
			String adUnitName, String pid, String canonicalPath) {
		this.id = id;
		this.adServerId = adServerId;
		this.adUnitId = adUnitId;
		this.adUnitName = adUnitName;
		this.pid = pid;
		this.canonicalPath = canonicalPath;
	}


	public AdUnitHierarchy(String id, String adServerId, String adUnitId, String adUnitName, String pid) {
		this.id = id;
		this.adServerId = adServerId;
		this.adUnitId = adUnitId;
		this.adUnitName = adUnitName;
		this.pid = pid;
	}

	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AdUnitHierarchy [id=");
		builder.append(id);
		builder.append(", adServerId=");
		builder.append(adServerId);
		builder.append(", adUnitId=");
		builder.append(adUnitId);
		builder.append(", adUnitName=");
		builder.append(adUnitName);
		builder.append(", pid=");
		builder.append(pid);
		builder.append(", canonicalPath=");
		builder.append(canonicalPath);
		builder.append("]");
		return builder.toString();
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAdServerId() {
		return adServerId;
	}

	public void setAdServerId(String adServerId) {
		this.adServerId = adServerId;
	}

	public String getAdUnitId() {
		return adUnitId;
	}

	public void setAdUnitId(String adUnitId) {
		this.adUnitId = adUnitId;
	}

	public String getAdUnitName() {
		return adUnitName;
	}

	public void setAdUnitName(String adUnitName) {
		this.adUnitName = adUnitName;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AdUnitHierarchy other = (AdUnitHierarchy) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getCanonicalPath() {
		return canonicalPath;
	}

	public void setCanonicalPath(String canonicalPath) {
		this.canonicalPath = canonicalPath;
	}
	
	
	
}
