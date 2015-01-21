package com.lin.web.dto;

import java.io.Serializable;

public class MapEngineDTO implements Serializable {
	
	private long stateId;
	private String stateName;
	private long impressions;
	private long clicks;
	private long cityId;
	private String cityName;
	
	public MapEngineDTO() {
		
	}

	public MapEngineDTO(long stateId, String stateName, long impressions,
			long clicks) {
		super();
		this.stateId = stateId;
		this.stateName = stateName;
		this.impressions = impressions;
		this.clicks = clicks;
	}

	public MapEngineDTO(long impressions, long clicks, long cityId,
			String cityName) {
		super();
		this.impressions = impressions;
		this.clicks = clicks;
		this.cityId = cityId;
		this.cityName = cityName;
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

	public long getImpressions() {
		return impressions;
	}

	public void setImpressions(long impressions) {
		this.impressions = impressions;
	}

	public long getClicks() {
		return clicks;
	}

	public void setClicks(long clicks) {
		this.clicks = clicks;
	}

	public long getCityId() {
		return cityId;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	
		
}
