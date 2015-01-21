package com.lin.web.dto;


public class PropertyDropDownDTO {
	
	private String propertyId;
	private String propertyName;
	private String DFP_propertyName;
	
	public PropertyDropDownDTO(){
		
	}
	
	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getDFP_propertyName() {
		return DFP_propertyName;
	}

	public void setDFP_propertyName(String dFP_propertyName) {
		DFP_propertyName = dFP_propertyName;
	}
	
	
}
