package com.lin.server.bean;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Id;

public class PublisherObj implements Serializable{

	@Id	private Long id;
	private String publisherName;
	private List<DemandPartnersObj> channelObject;
	private List<String> dataSource;
	private List<String> order;
	private String companyId;
	private String pubisherAdserver;
	private String DFPNetworkId;
	private String DFPPublisherId;
	private String type;
	private String NoOfStations;
	private String LastChangeTms;
	private String LastChgUser;
	
    public PublisherObj(){
		
	}

	public PublisherObj(String publisherName,
			List<DemandPartnersObj> channelObject, List<String> dataSource,
			List<String> order, String companyId, String pubisherAdserver,
			String dFPNetworkId, String dFPPublisherId, String type,
			String noOfStations, String lastChangeTms, String lastChgUser) {
		this.publisherName = publisherName;
		this.channelObject = channelObject;
		this.dataSource = dataSource;
		this.order = order;
		this.companyId = companyId;
		this.pubisherAdserver = pubisherAdserver;
		DFPNetworkId = dFPNetworkId;
		DFPPublisherId = dFPPublisherId;
		this.type = type;
		NoOfStations = noOfStations;
		LastChangeTms = lastChangeTms;
		LastChgUser = lastChgUser;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPublisherName() {
		return publisherName;
	}

	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}

	public List<String> getDataSource() {
		return dataSource;
	}

	public void setDataSource(List<String> dataSource) {
		this.dataSource = dataSource;
	}

	public List<String> getOrder() {
		return order;
	}

	public void setOrder(List<String> order) {
		this.order = order;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getPubisherAdserver() {
		return pubisherAdserver;
	}

	public void setPubisherAdserver(String pubisherAdserver) {
		this.pubisherAdserver = pubisherAdserver;
	}

	public String getDFPNetworkId() {
		return DFPNetworkId;
	}

	public void setDFPNetworkId(String dFPNetworkId) {
		DFPNetworkId = dFPNetworkId;
	}

	public String getDFPPublisherId() {
		return DFPPublisherId;
	}

	public void setDFPPublisherId(String dFPPublisherId) {
		DFPPublisherId = dFPPublisherId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNoOfStations() {
		return NoOfStations;
	}

	public void setNoOfStations(String noOfStations) {
		NoOfStations = noOfStations;
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

	public void setChannelObject(List<DemandPartnersObj> channelObject) {
		this.channelObject = channelObject;
	}

	public List<DemandPartnersObj> getChannelObject() {
		return channelObject;
	}
	
	
}
