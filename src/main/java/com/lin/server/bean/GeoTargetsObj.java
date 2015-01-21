package com.lin.server.bean;
import java.io.Serializable;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity
/*
 * @author Naresh Pokhriyal
 * GeoTargetsObj - POJO (Entity) file for datastore
 */
@SuppressWarnings("serial")
@Index
public class GeoTargetsObj implements Serializable {
	
	@Id private long geoTargetId;
	private String geoTargetsName;
	
	public GeoTargetsObj() {
	}

	public GeoTargetsObj(long geoTargetId, String geoTargetsName) {
		this.geoTargetId = geoTargetId;
		this.geoTargetsName = geoTargetsName;
	}

	@Override
	public String toString() {
		return "GeoTargetsObj [geoTargetId=" + geoTargetId
				+ ", geoTargetsName=" + geoTargetsName + "]";
	}

	public long getGeoTargetId() {
		return geoTargetId;
	}

	public void setGeoTargetId(long geoTargetId) {
		this.geoTargetId = geoTargetId;
	}

	public String getGeoTargetsName() {
		return geoTargetsName;
	}

	public void setGeoTargetsName(String geoTargetsName) {
		this.geoTargetsName = geoTargetsName;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GeoTargetsObj other = (GeoTargetsObj) obj;
		if (geoTargetId != other.geoTargetId)
			return false;
		return true;
	}
	
	
	
	
}
