package com.lin.web.dto;

import java.util.List;

public class DataUploaderDTO {
	
	private String publisherName;
	private int publisherBQId;
	private String dfpNetworkCode;
	private List<String> orderIdList;
	
	public DataUploaderDTO() {
	}
	

	public DataUploaderDTO(String publisherName, int publisherBQId,
			String dfpNetworkCode, List<String> orderIdList) {
		super();
		this.publisherName = publisherName;
		this.publisherBQId = publisherBQId;
		this.dfpNetworkCode = dfpNetworkCode;
		this.orderIdList = orderIdList;
	}


	@Override
	public String toString() {
		return "DataUploaderDTO [publisherName=" + publisherName
				+ ", publisherBQId=" + publisherBQId + ", dfpNetworkCode="
				+ dfpNetworkCode + ", orderIdList=" + orderIdList + "]";
	}


	public String getPublisherName() {
		return publisherName;
	}

	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}

	public int getPublisherBQId() {
		return publisherBQId;
	}

	public void setPublisherBQId(int publisherBQId) {
		this.publisherBQId = publisherBQId;
	}

	public List<String> getOrderIdList() {
		return orderIdList;
	}

	public void setOrderIdList(List<String> orderIdList) {
		this.orderIdList = orderIdList;
	}

	public String getDfpNetworkCode() {
		return dfpNetworkCode;
	}

	public void setDfpNetworkCode(String dfpNetworkCode) {
		this.dfpNetworkCode = dfpNetworkCode;
	}

}
