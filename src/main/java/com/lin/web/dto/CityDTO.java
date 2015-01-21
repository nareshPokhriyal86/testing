package com.lin.web.dto;

import java.io.Serializable;

public class CityDTO implements Serializable{

	private long id;
	private String text;
	private long stateId;
	private String stateName;
	
	public CityDTO() {
	}
	public CityDTO(long id, String text, long stateId, String stateName) {
		this.id = id;
		this.text = text;
		this.stateId = stateId;
		this.stateName = stateName;
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
	public long getStateId() {
		return stateId;
	}
	public void setStateId(long stateId) {
		this.stateId = stateId;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CityDTO other = (CityDTO) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
	
	
	
	
	
	
}
