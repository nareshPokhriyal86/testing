package com.lin.server.bean;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity
/*
 * @author Naresh Pokhriyal
 * This entity is used to store static data for dropdowns in application.
 */

@SuppressWarnings("serial")
@Index
public class DropdownDataObj implements Serializable {
	@Id	private Long id;
	private long objectId;
	private String valueType;
	private String value;
	private String createdBy;
	private String createdOn;
	
	public DropdownDataObj(){
	}

	public DropdownDataObj(long objectId, String valueType, String value, String createdBy, String createdOn) {
		this.objectId = objectId;
		this.valueType = valueType;
		this.value = value;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	public String getValueType() {
		return valueType;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}

	public long getObjectId() {
		return objectId;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DropdownDataObj other = (DropdownDataObj) obj;
		if (objectId != other.objectId)
			return false;
		if (valueType == null) {
			if (other.valueType != null)
				return false;
		} else if (!valueType.equals(other.valueType))
			return false;
		return true;
	}
	
	

	
}
