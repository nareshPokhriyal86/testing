package com.lin.web.dto;

import java.io.Serializable;

public class ForcastInventoryDTO implements Serializable {

	private String code;
	private Long availableImpressions;
	private String name;
	private String publisherId;
	private String publisherName;
	private String dfpPropertyName;
	
	public ForcastInventoryDTO() {
		
	}

	public ForcastInventoryDTO(String code, Long availableImpressions,
			String name, String publisherId, String publisherName,
			String dfpPropertyName) {
		super();
		this.code = code;
		this.availableImpressions = availableImpressions;
		this.name = name;
		this.publisherId = publisherId;
		this.publisherName = publisherName;
		this.dfpPropertyName = dfpPropertyName;
	}



	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getAvailableImpressions() {
		return availableImpressions;
	}

	public void setAvailableImpressions(Long availableImpressions) {
		this.availableImpressions = availableImpressions;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPublisherId() {
		return publisherId;
	}

	public void setPublisherId(String publisherId) {
		this.publisherId = publisherId;
	}

	public String getPublisherName() {
		return publisherName;
	}

	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}

	public String getDfpPropertyName() {
		return dfpPropertyName;
	}

	public void setDfpPropertyName(String dfpPropertyName) {
		this.dfpPropertyName = dfpPropertyName;
	}
	
	
}
