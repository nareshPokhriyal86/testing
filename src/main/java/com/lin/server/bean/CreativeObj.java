package com.lin.server.bean;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/*
 * @author Naresh Pokhriyal
 * This entity is used as embedded bean to store Creative for Product.
 */

@SuppressWarnings("serial")
@Entity
@Index
public class CreativeObj implements Serializable {
	@Id private long id;
	private String format;
	private String size;
	
	public CreativeObj() {
	}
	public CreativeObj(long id, String format, String size) {
		this.id = id;
		this.format = format;
		this.size = size;
	}
	
	@Override
	public String toString() {
		return "CreativeObj [id=" + id + ", format=" + format + ", size="
				+ size + "]";
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CreativeObj other = (CreativeObj) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
	
	
}
