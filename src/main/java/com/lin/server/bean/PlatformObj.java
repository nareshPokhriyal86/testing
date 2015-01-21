package com.lin.server.bean;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;



/*
 * @author Naresh Pokhriyal
 * This entity is used as embedded bean to store Platform for Product.
 */

@SuppressWarnings("serial")
@Entity
public class PlatformObj implements Serializable {
	@Id private long id;
	private String text;

	public PlatformObj() {
	}
	
	public PlatformObj(long id, String text) {
		this.id = id;
		this.text = text;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlatformObj other = (PlatformObj) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
}
