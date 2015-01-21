package com.lin.server.bean;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Index;

@Index
public class PropertyChildObj  implements Serializable{
	private String childId;
	private String childName;

	public PropertyChildObj() {

	}

	public PropertyChildObj(String childId, String childName) {
		this.childId = childId;
		this.childName = childName;
	}

	public String getChildId() {
		return childId;
	}

	public void setChildId(String childId) {
		this.childId = childId;
	}

	public String getChildName() {
		return childName;
	}

	public void setChildName(String childName) {
		this.childName = childName;
	}
	
	@Override
	public boolean equals(Object obj) {
		PropertyChildObj tempObj = (PropertyChildObj)obj;
		return tempObj.childId.equals(this.childId);
	}

}
