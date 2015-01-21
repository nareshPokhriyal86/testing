package com.lin.server.bean;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity
@SuppressWarnings("serial")
@Index
public class SellThroughDataObj implements Serializable{
	@Id	private String id;
	private String property;
	private String adUnit;
	private String forecastedImpressions;
	private String availableImpressions;
	private String reservedImpressions;
	private String sellThroughRate;
	private String date;
	
	public SellThroughDataObj(){}

	
	public SellThroughDataObj(String id, String property, String adUnit,
			String forecastedImpressions, String availableImpressions,
			String reservedImpressions, String sellThroughRate, String date) {
		this.id = id;
		this.property = property;
		this.adUnit = adUnit;
		this.forecastedImpressions = forecastedImpressions;
		this.availableImpressions = availableImpressions;
		this.reservedImpressions = reservedImpressions;
		this.sellThroughRate = sellThroughRate;
		this.date = date;
	}

	public SellThroughDataObj(String property, String adUnit,
			String forecastedImpressions, String availableImpressions,
			String reservedImpressions, String sellThroughRate) {
		this.property = property;
		this.adUnit = adUnit;
		this.forecastedImpressions = forecastedImpressions;
		this.availableImpressions = availableImpressions;
		this.reservedImpressions = reservedImpressions;
		this.sellThroughRate = sellThroughRate;
	}
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getAdUnit() {
		return adUnit;
	}

	public void setAdUnit(String adUnit) {
		this.adUnit = adUnit;
	}



	public String getForecastedImpressions() {
		return forecastedImpressions;
	}


	public void setForecastedImpressions(String forecastedImpressions) {
		this.forecastedImpressions = forecastedImpressions;
	}


	public String getAvailableImpressions() {
		return availableImpressions;
	}


	public void setAvailableImpressions(String availableImpressions) {
		this.availableImpressions = availableImpressions;
	}


	public String getReservedImpressions() {
		return reservedImpressions;
	}


	public void setReservedImpressions(String reservedImpressions) {
		this.reservedImpressions = reservedImpressions;
	}


	public String getSellThroughRate() {
		return sellThroughRate;
	}


	public void setSellThroughRate(String sellThroughRate) {
		this.sellThroughRate = sellThroughRate;
	}


	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	

	
}
