package com.lin.server.bean;

import java.io.Serializable;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

/*
 * @author Naresh Pokhriyal
 * This entity is used as embedded bean to store City for Product.
 */

@SuppressWarnings("serial")
@Entity
@Index
public class CityObj implements Serializable {
	@Id private long id;
	@Index private String zip;
	private String text;
	@Parent private Key<StateObj> state;
	
	
	public CityObj() {
	}
	
	public CityObj(long id, String zip, String text) {
		this.id = id;
		this.zip = zip;
		this.text = text;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Key<StateObj> getState() {
		return state;
	}
	public void setState(Key<StateObj> state) {
		this.state = state;
	}
	
}
