package com.lin.web.dto;

import java.util.Date;


public class PublisherReportComputedValuesDTO {

	private double change;
	private double changePercentage;
	private double propertyChange;
	private double propertyChangePercentage;
	
	public PublisherReportComputedValuesDTO() {
	}

	public PublisherReportComputedValuesDTO(double change,
			double changePercentage, double propertyChange,
			double propertyChangePercentage) {
		this.change = change;
		this.changePercentage = changePercentage;
		this.propertyChange = propertyChange;
		this.propertyChangePercentage = propertyChangePercentage;
	}

	@Override
	public String toString() {
		return "PublisherReportComputedValuesDTO [change=" + change
				+ ", changePercentage=" + changePercentage
				+ ", propertyChange=" + propertyChange
				+ ", propertyChangePercentage=" + propertyChangePercentage
				+ "]";
	}

	public double getChange() {
		return change;
	}

	public void setChange(double change) {
		this.change = change;
	}

	public double getChangePercentage() {
		return changePercentage;
	}

	public void setChangePercentage(double changePercentage) {
		this.changePercentage = changePercentage;
	}

	public double getPropertyChange() {
		return propertyChange;
	}

	public void setPropertyChange(double propertyChange) {
		this.propertyChange = propertyChange;
	}

	public double getPropertyChangePercentage() {
		return propertyChangePercentage;
	}

	public void setPropertyChangePercentage(double propertyChangePercentage) {
		this.propertyChangePercentage = propertyChangePercentage;
	}

	
	
	
}
