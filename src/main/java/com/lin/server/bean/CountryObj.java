package com.lin.server.bean;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

/*
 * @author Naresh Pokhriyal
 * This entity is used as embedded bean to store Countries for Product.
 */

@SuppressWarnings("serial")
@Entity
@Index
public class CountryObj implements Serializable {
	@Id private long id;
	private String code;
	private String text;
	
	public CountryObj(){
		
	}
	public CountryObj(long id, String code, String text) {
		this.id = id;
		this.code = code;
		this.text = text;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
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
		CountryObj other = (CountryObj) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}	
	
	
}
