package com.lin.server.bean;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity
/*
 * @author Naresh Pokhriyal
 * This entity is used in Admin(Teams) to get advertisers of company's DFP
 */
@Index
public class DFPAdvertisersObj implements Serializable {
	@Id	private String id;			// "adServerId" + "adServerUserName" + "advertiserId"
	private String adServerId;
	private String adServerUserName;
	private String agencyId;
	private String advertiserId;
	private String advertiserName;
	
	private String companyId;
	
	// following variable("selectedStatus") will be used for temporary purpose
	private String selectedStatus;
	
	public DFPAdvertisersObj(){
	}

	public DFPAdvertisersObj(String adServerId, String adServerUserName, String agencyId, String advertiserId, String advertiserName) {
		this.id = adServerId.trim() + adServerUserName.trim() + advertiserId.trim();
		this.adServerId = adServerId;
		this.adServerUserName = adServerUserName;
		this.agencyId = agencyId;
		this.advertiserId = advertiserId;
		this.advertiserName = advertiserName;
	}

	public DFPAdvertisersObj(String advertiserId, String advertiserName) {
		this.advertiserId = advertiserId;
		this.advertiserName = advertiserName;
	}

	public String getAdvertiserId() {
		return advertiserId;
	}

	public void setAdvertiserId(String advertiserId) {
		this.advertiserId = advertiserId;
	}

	public String getAdvertiserName() {
		return advertiserName;
	}

	public void setAdvertiserName(String advertiserName) {
		this.advertiserName = advertiserName;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}

	public String getAgencyId() {
		return agencyId;
	}

	public void setSelectedStatus(String selectedStatus) {
		this.selectedStatus = selectedStatus;
	}

	public String getSelectedStatus() {
		return selectedStatus;
	}

	public void setAdServerId(String adServerId) {
		this.adServerId = adServerId;
	}

	public String getAdServerId() {
		return adServerId;
	}

	public void setAdServerUserName(String adServerUserName) {
		this.adServerUserName = adServerUserName;
	}

	public String getAdServerUserName() {
		return adServerUserName;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyId() {
		return companyId;
	}

	
}
