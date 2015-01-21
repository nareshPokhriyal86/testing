package com.lin.server.bean;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity
/*
 * @author Naresh Pokhriyal
 * This entity is used in Admin(Teams) to get Orders of company's DFP
 */
@Index
public class OrdersObj implements Serializable{
	@Id	private String id;			// "adServerId" + "adServerUserName" + "orderId"
	private String adServerId;
	private String adServerUserName;
	private String agencyId;
	private String advertiserId;
	private String orderId;
	private String orderName;
	
	private String companyId;
	
	// following variable("selectedStatus") will be used for temporary purpose
	private String selectedStatus;
	
	public OrdersObj(){
	}

	public OrdersObj(String adServerId, String adServerUserName, String agencyId, String advertiserId, String orderId, String orderName) {
		this.id = adServerId.trim() + adServerUserName.trim() + orderId.trim();
		this.adServerId = adServerId;
		this.adServerUserName = adServerUserName;
		this.agencyId = agencyId;
		this.advertiserId = advertiserId;
		this.orderId = orderId;
		this.orderName = orderName;
	}

	public OrdersObj(String orderId, String orderName) {
		this.orderId = orderId;
		this.orderName = orderName;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setAdvertiserId(String advertiserId) {
		this.advertiserId = advertiserId;
	}

	public String getAdvertiserId() {
		return advertiserId;
	}

	public void setSelectedStatus(String selectedStatus) {
		this.selectedStatus = selectedStatus;
	}

	public String getSelectedStatus() {
		return selectedStatus;
	}

	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}

	public String getAgencyId() {
		return agencyId;
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
