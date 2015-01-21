package com.lin.web.dto;

import java.io.Serializable;

public class PublisherSetupDTO implements Serializable{
	private Long id;
	private String publisherName;
	private String demandPartnersName;
	private String status;
	
	public PublisherSetupDTO(Long id, String publisherName,
			String demandPartnersName, String status) {
		this.id = id;
		this.publisherName = publisherName;
		this.demandPartnersName = demandPartnersName;
		this.status = status;
	}

	public PublisherSetupDTO() {
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

	public String getDemandPartnersName() {
		return demandPartnersName;
	}

	public void setDemandPartnersName(String demandPartnersName) {
		this.demandPartnersName = demandPartnersName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
}
