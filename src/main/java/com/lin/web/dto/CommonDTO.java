package com.lin.web.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;

public class CommonDTO implements Serializable, Comparable<CommonDTO>{
	private String id;
	private String value;
	
	/* following variables are used to get popUp Data */
	private String date;
	private String impressionsDelivered;
	
	/*  following variable is used in update team ajax call 
		to get the selected/unselected state for dropdown options*/
	private String status;
	
	/* following variables are used for Reconciliation Screen Data */
	private String channelName;
	private String ChannelDataSource;
	
	/* following variables is used in company create and update in Admin*/
	private String companyType;
	
	/* following variables is used in team create and update in Admin*/
	private List<CommonDTO> appViews;
	private boolean accessToAccounts;
	private boolean accessToProperties;
	private List<CommonDTO> propertyList;
	private List<CommonDTO> accountsList;
	/*private String publisherIdsForBigQuery;*/
	
	
	public CommonDTO(){
		
	}
	
	public CommonDTO(String id, String value, String date, String impressionsDelivered) {
		this.id = id;
		this.value = value;
		this.date = date;
		this.impressionsDelivered = impressionsDelivered;
	}
	
	public CommonDTO(String id,String value){
		this.id=id;
		this.value=value;
	}
	
	public CommonDTO(String id,String value, String status){
		this.id=id;
		this.value=value;
		this.status=status;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDate() {
		return date;
	}
	public void setImpressionsDelivered(String impressionsDelivered) {
		this.impressionsDelivered = impressionsDelivered;
	}
	public String getImpressionsDelivered() {
		return impressionsDelivered;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelDataSource(String channelDataSource) {
		ChannelDataSource = channelDataSource;
	}

	public String getChannelDataSource() {
		return ChannelDataSource;
	}

	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}

	public String getCompanyType() {
		return companyType;
	}

	@Override
	public int compareTo(CommonDTO commonDTO) {
		return value.compareToIgnoreCase(commonDTO.getValue());
	}

	public List<CommonDTO> getAppViews() {
		return appViews;
	}

	public void setAppViews(List<CommonDTO> appViews) {
		this.appViews = appViews;
	}

	public boolean isAccessToAccounts() {
		return accessToAccounts;
	}

	public void setAccessToAccounts(boolean accessToAccounts) {
		this.accessToAccounts = accessToAccounts;
	}

	public boolean isAccessToProperties() {
		return accessToProperties;
	}

	public void setAccessToProperties(boolean accessToProperties) {
		this.accessToProperties = accessToProperties;
	}

	public void setPropertyList(List<CommonDTO> propertyList) {
		this.propertyList = propertyList;
	}

	public List<CommonDTO> getPropertyList() {
		return propertyList;
	}

	public void setAccountsList(List<CommonDTO> accountsList) {
		this.accountsList = accountsList;
	}

	public List<CommonDTO> getAccountsList() {
		return accountsList;
	}

	 

	@Override
	public int hashCode() {
		  return id.hashCode();
		 
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CommonDTO other = (CommonDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
