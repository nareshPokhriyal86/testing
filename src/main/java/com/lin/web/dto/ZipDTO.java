package com.lin.web.dto;

import java.io.Serializable;

public class ZipDTO implements Serializable{

	private String text;
	private String cityId;
	private String cityName;
	private long stateId;
	private String stateName;
	
	public ZipDTO() {
	}
	
	public ZipDTO(String text, String cityId, String cityName,
			long stateId, String stateName) {
		this.text = text;
		this.cityId = cityId;
		this.cityName = cityName;
		this.stateId = stateId;
		this.stateName = stateName;
	}
	
	@Override
	public String toString() {
		return "ZipDTO [text=" + text + ", cityId=" + cityId + ", cityName="
				+ cityName + ", stateId=" + stateId + ", stateName="
				+ stateName + "]";
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
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ZipDTO other = (ZipDTO) obj;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}	
	
	
}
