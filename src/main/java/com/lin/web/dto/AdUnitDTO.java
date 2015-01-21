package com.lin.web.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class AdUnitDTO implements Serializable{

	private long id;
	private String name;
	private String canonicalPath;
	private long pid;
	private String adUnitCode;
	private String targetPlatform;
	public AdUnitDTO() {
	}

	public AdUnitDTO(long id, String name, String canonicalPath, long pid) {
		this.id = id;
		this.name = name;
		this.canonicalPath=canonicalPath;
	}

	@Override
	public boolean equals(Object obj) {
	    if (obj == null) {
	        return false;
	    }
	    if (getClass() != obj.getClass()) {
	        return false;
	    }
	    final AdUnitDTO other = (AdUnitDTO) obj;
	    if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
	        return false;
	    }
	    if (this.id != other.id) {
	        return false;
	    }
	    return true;
	}

	@Override
	public int hashCode() {
	    int hash = 3;
	    hash = 53 * hash + (this.name != null ? this.name.hashCode() : 0);
	    hash = (int) (53 * hash + this.id);
	    return hash;
	}
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public String getCanonicalPath() {
		return canonicalPath;
	}

	public void setCanonicalPath(String canonicalPath) {
		this.canonicalPath = canonicalPath;
	}

	public String getAdUnitCode() {
		return adUnitCode;
	}

	public void setAdUnitCode(String adUnitCode) {
		this.adUnitCode = adUnitCode;
	}

	public String getTargetPlatform() {
		return targetPlatform;
	}

	public void setTargetPlatform(String targetPlatform) {
		this.targetPlatform = targetPlatform;
	}
	
	
	
	
}
