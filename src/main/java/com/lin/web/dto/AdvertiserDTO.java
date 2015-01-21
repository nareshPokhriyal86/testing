package com.lin.web.dto;


public class AdvertiserDTO {

	private long agencyId;	
	private long advertiserId;
	private String advertiserName;
	
	
	public AdvertiserDTO(){
		
	}


	public AdvertiserDTO(long agencyId, long advertiserId, String advertiserName) {
		this.agencyId = agencyId;
		this.advertiserId = advertiserId;
		this.advertiserName = advertiserName;
	}


	public long getAgencyId() {
		return agencyId;
	}


	public void setAgencyId(long agencyId) {
		this.agencyId = agencyId;
	}


	public long getAdvertiserId() {
		return advertiserId;
	}


	public void setAdvertiserId(long advertiserId) {
		this.advertiserId = advertiserId;
	}


	public String getAdvertiserName() {
		return advertiserName;
	}


	public void setAdvertiserName(String advertiserName) {
		this.advertiserName = advertiserName;
	}
	
	
	
}
