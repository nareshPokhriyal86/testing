package com.lin.server.bean;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity
/*
 * @author Naresh Pokhriyal
 * This entity is used as embedded bean to store IABContext for Product.
 */

@SuppressWarnings("serial")
@Index
public class IABContextObj implements Serializable,  Comparable<IABContextObj> {
	@Id private long id;
	private String group;
	private String subgroup;
	
	public IABContextObj() {
	}
	
	public IABContextObj(long id, String group, String subgroup) {
		this.id = id;
		this.group = group;
		this.subgroup = subgroup;
	}
	
	
	
	@Override
	public String toString() {
		return "IABContextObj [group=" + group + ", subgroup=" + subgroup + "]";
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getSubgroup() {
		return subgroup;
	}
	public void setSubgroup(String subgroup) {
		this.subgroup = subgroup;
	}
	
	@Override
	public int compareTo(IABContextObj obj) {
		return subgroup.compareToIgnoreCase(obj.getSubgroup());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IABContextObj other = (IABContextObj) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}	
}
