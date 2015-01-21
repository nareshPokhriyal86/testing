package com.lin.server.bean;

import java.io.Serializable;

import javax.persistence.Id;

@SuppressWarnings("serial")
public class DemandPartnersObj implements Serializable{
	@Id	private Long id;
	private String demandPartnerName;
	private String dataSource;
	private String demandPartnerType;
	private String LastChangeTms;
	private String LastChgUser;
	
	
	public DemandPartnersObj() {
		
	}


	public DemandPartnersObj(Long id, String demandPartnerName,
			String dataSource, String demandPartnerType, String lastChangeTms,
			String lastChgUser) {
		super();
		this.id = id;
		this.demandPartnerName = demandPartnerName;
		this.dataSource = dataSource;
		this.demandPartnerType = demandPartnerType;
		LastChangeTms = lastChangeTms;
		LastChgUser = lastChgUser;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getDemandPartnerName() {
		return demandPartnerName;
	}


	public void setDemandPartnerName(String demandPartnerName) {
		this.demandPartnerName = demandPartnerName;
	}


	public String getDataSource() {
		return dataSource;
	}


	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}


	public String getDemandPartnerType() {
		return demandPartnerType;
	}


	public void setDemandPartnerType(String demandPartnerType) {
		this.demandPartnerType = demandPartnerType;
	}


	public String getLastChangeTms() {
		return LastChangeTms;
	}


	public void setLastChangeTms(String lastChangeTms) {
		LastChangeTms = lastChangeTms;
	}


	public String getLastChgUser() {
		return LastChgUser;
	}


	public void setLastChgUser(String lastChgUser) {
		LastChgUser = lastChgUser;
	}
	

}
