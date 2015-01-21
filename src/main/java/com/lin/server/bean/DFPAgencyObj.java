package com.lin.server.bean;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity

/*
 * @author Naresh Pokhriyal
 * This entity is used in Admin(Teams) to get agencies of company's DFP
 */
@Index
public class DFPAgencyObj implements Serializable {
	@Id	private String id;			// "adServerId" + "adServerUserName" + "agencyId"
	private String adServerId;
	private String adServerUserName;
	private String agencyId;
	private String agencyName;
	
	private String companyId;
	
	// following variable("selectedStatus") will be used for temporary purpose
	private String selectedStatus;
	
	public DFPAgencyObj(){
	}
	
	public DFPAgencyObj(String adServerId, String adServerUserName, String agencyId, String agencyName) {
		this.id = adServerId.trim() + adServerUserName.trim() + agencyId.trim();
		this.adServerId = adServerId;
		this.adServerUserName = adServerUserName;
		this.agencyId = agencyId;
		this.agencyName = agencyName;
	}

	public DFPAgencyObj(String agencyId, String agencyName) {
		this.agencyId = agencyId;
		this.agencyName = agencyName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}

	public String getAgencyName() {
		return agencyName;
	}

	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
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
