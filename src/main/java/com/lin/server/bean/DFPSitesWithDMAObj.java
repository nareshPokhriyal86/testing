package com.lin.server.bean;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity

/*
 * @author Shubham Goel
 * This entity is used to store  DFP sites with DMA  in datastore
 * with respect to DFP Network
 *  
 */

@SuppressWarnings("serial")
@Index
public class DFPSitesWithDMAObj implements Serializable {
	
	@Id	private String id;
	private long code;
	private String DMAId;
	private String propertyName;
	private String publisherId;
	private String DFPPropertyName;
	private String address;
	private String zipCode;
	private String State;
	private String name;
	private String publisherName;
	
    public DFPSitesWithDMAObj(){
		
	 }
    
    

	public DFPSitesWithDMAObj(String id, long code, String dMAId,
			String propertyName, String publisherId, String dFPPropertyName,
			String address, String zipCode, String state, String name,
			String publisherName) {
		super();
		this.id = id;
		this.code = code;
		DMAId = dMAId;
		this.propertyName = propertyName;
		this.publisherId = publisherId;
		DFPPropertyName = dFPPropertyName;
		this.address = address;
		this.zipCode = zipCode;
		State = state;
		this.name = name;
		this.publisherName = publisherName;
	}



	@Override
	public String toString() {
		return "DFPSitesWithDMAObj [id=" + id + ", code=" + code + ", DMAId="
				+ DMAId + ", propertyName=" + propertyName + ", publisherId="
				+ publisherId + ", DFPPropertyName=" + DFPPropertyName
				+ ", address=" + address + ", zipCode=" + zipCode + ", State="
				+ State + ", name=" + name + ", publisherName=" + publisherName
				+ "]";
	}



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getCode() {
		return code;
	}

	public void setCode(long code) {
		this.code = code;
	}

	public String getDMAId() {
		return DMAId;
	}

	public void setDMAId(String dMAId) {
		DMAId = dMAId;
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

	public String getDFPPropertyName() {
		return DFPPropertyName;
	}

	public void setDFPPropertyName(String dFPPropertyName) {
		DFPPropertyName = dFPPropertyName;
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
		return State;
	}

	public void setState(String state) {
		State = state;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPublisherName() {
		return publisherName;
	}

	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}
    
    

}
