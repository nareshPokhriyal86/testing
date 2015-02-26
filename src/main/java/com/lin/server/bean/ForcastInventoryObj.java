package com.lin.server.bean;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity
/*
 * @author Shubham Goel
 * This entity is used to store Forcast Invantory in datastore
 * with respect to DFP Network
 *  
 */

@SuppressWarnings("serial")
@Index
public class ForcastInventoryObj implements Serializable,Comparable<ForcastInventoryObj>{
	@Id	private String id;
	private String DFPPropertyName;
	private String code;
	private String adUnitId;
	private String propertyName;
	private String publisherId;
	private String address;
	private String zipCode;
	private String state;
	private String name;
	private String creativeSize;
	private String startDate;
	private String endDate;
	private Long forcastedImpressions;
	private Long availableImpressions;
	private Long reservedImpressions;
	
	private String publisherName;
	
	
	public ForcastInventoryObj() {
		
	}


	public ForcastInventoryObj(String id, String dFPPropertyName, String code,
			String adUnitId, String propertyName, String publisherId,
			String address, String zipCode, String state, String name,
			String creativeSize, String startDate, String endDate,
			Long forcastedImpressions, Long availableImpressions,
			Long reservedImpressions, String publisherName) {
		this.id = id;
		DFPPropertyName = dFPPropertyName;
		this.code = code;
		this.adUnitId = adUnitId;
		this.propertyName = propertyName;
		this.publisherId = publisherId;
		this.address = address;
		this.zipCode = zipCode;
		this.state = state;
		this.name = name;
		this.creativeSize = creativeSize;
		this.startDate = startDate;
		this.endDate = endDate;
		this.forcastedImpressions = forcastedImpressions;
		this.availableImpressions = availableImpressions;
		this.reservedImpressions = reservedImpressions;
		this.publisherName = publisherName;
	}


	@Override
	public String toString() {
		return "ForcastInventoryObj [id=" + id + ", DFPPropertyName="
				+ DFPPropertyName + ", code=" + code + ", adUnitId=" + adUnitId
				+ ", propertyName=" + propertyName + ", publisherId="
				+ publisherId + ", address=" + address + ", zipCode=" + zipCode
				+ ", state=" + state + ", name=" + name + ", creativeSize="
				+ creativeSize + ", startDate=" + startDate + ", endDate="
				+ endDate + ", forcastedImpressions=" + forcastedImpressions
				+ ", availableImpressions=" + availableImpressions
				+ ", reservedImpressions=" + reservedImpressions
				+ ", publisherName=" + publisherName + "]";
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getDFPPropertyName() {
		return DFPPropertyName;
	}


	public void setDFPPropertyName(String dFPPropertyName) {
		DFPPropertyName = dFPPropertyName;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String getAdUnitId() {
		return adUnitId;
	}


	public void setAdUnitId(String adUnitId) {
		this.adUnitId = adUnitId;
	}


	public String getPropertyName() {
		return propertyName;
	}


	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}


	public String getPublisherId() {
		return publisherId;
	}


	public void setPublisherId(String publisherId) {
		this.publisherId = publisherId;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getZipCode() {
		return zipCode;
	}


	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}


	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getCreativeSize() {
		return creativeSize;
	}


	public void setCreativeSize(String creativeSize) {
		this.creativeSize = creativeSize;
	}


	public String getStartDate() {
		return startDate;
	}


	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}


	public String getEndDate() {
		return endDate;
	}


	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}


	public Long getForcastedImpressions() {
		return forcastedImpressions;
	}


	public void setForcastedImpressions(Long forcastedImpressions) {
		this.forcastedImpressions = forcastedImpressions;
	}


	public Long getAvailableImpressions() {
		return availableImpressions;
	}


	public void setAvailableImpressions(Long availableImpressions) {
		this.availableImpressions = availableImpressions;
	}


	public Long getReservedImpressions() {
		return reservedImpressions;
	}


	public void setReservedImpressions(Long reservedImpressions) {
		this.reservedImpressions = reservedImpressions;
	}


	public String getPublisherName() {
		return publisherName;
	}


	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}


	@Override
	public int compareTo(ForcastInventoryObj o) {
		return this.availableImpressions.compareTo(o.availableImpressions);
		
	}


	
	@Override
	public boolean equals(Object arg0) {
		// TODO Auto-generated method stub
		return super.equals(arg0);
	}		
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}	
}
