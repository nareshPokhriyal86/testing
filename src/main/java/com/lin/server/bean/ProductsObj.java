package com.lin.server.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.lin.web.dto.AdUnitDTO;
import com.lin.web.dto.CityDTO;
import com.lin.web.dto.ZipDTO;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity

/*
 * @author Naresh Pokhriyal
 * This entity is used to store Product for publisher.
 */

@SuppressWarnings("serial")
@Index
public class ProductsObj implements Serializable{
	
	@Id private String id;
	private long productId;
	private String productName ;
	private double rate;
	private String publisherId;	// adServerId
	private String partnerId; // publisherCompanyId
	private String publisherName;
	private String note;

	//private boolean status; // True if Product is available
	private boolean geoFencing; // True if Support Geo Fencing
	private boolean demographic; // True if Support Any kind
	private boolean behaviour; // True if Support Any kind
	
	private List<IABContextObj> context; // List of IAB Context Objects
	private List<CreativeObj> creative; // list of available creative objects

	// GEO Based Properties
	//private List<CountryObj> countries; // list of countries objects. As on now only USA
	private List<StateObj> states; // list of states object based on countries. As of now only states of USA
	private List<CityDTO> cities; // list of city objects
	private List<ZipDTO> zips; // list of city objects but unique zip
	private List<GeoTargetsObj> dmas; // List of DMA
	
	private List<PlatformObj> platforms; // list of platform objects
	private List<DeviceObj> devices; // List of device objects
	private List<AdUnitDTO> adUnits; // list of adUnits objects
	private Date lastUpdatedOn;
	private int deviceCapability = 0;
	
	
	
	@Override
	public String toString() {
		return "ProductsObj [id=" + id + ", productId=" + productId
				+ ", productName=" + productName + ", rate=" + rate
				+ ", publisherId=" + publisherId + ", partnerId=" + partnerId
				+ ", publisherName=" + publisherName + ", note=" + note
				+ ", geoFencing=" + geoFencing + ", demographic=" + demographic
				+ ", behaviour=" + behaviour + ", context=" + context
				+ ", creative=" + creative + ", states=" + states + ", cities="
				+ cities + ", zips=" + zips + ", dmas=" + dmas + ", platforms="
				+ platforms + ", devices=" + devices + ", adUnits=" + adUnits
				+ ", lastUpdatedOn=" + lastUpdatedOn + "]";
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
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
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public boolean isGeoFencing() {
		return geoFencing;
	}
	public void setGeoFencing(boolean geoFencing) {
		this.geoFencing = geoFencing;
	}
	public boolean isDemographic() {
		return demographic;
	}
	public void setDemographic(boolean demographic) {
		this.demographic = demographic;
	}
	public boolean isBehaviour() {
		return behaviour;
	}
	public void setBehaviour(boolean behaviour) {
		this.behaviour = behaviour;
	}
	public List<IABContextObj> getContext() {
		return context;
	}
	public void setContext(List<IABContextObj> context) {
		this.context = context;
	}
	public List<CreativeObj> getCreative() {
		return creative;
	}
	public void setCreative(List<CreativeObj> creative) {
		this.creative = creative;
	}
	public List<CityDTO> getCities() {
		return cities;
	}
	public void setCities(List<CityDTO> cities) {
		this.cities = cities;
	}
	public List<GeoTargetsObj> getDmas() {
		return dmas;
	}
	public void setDmas(List<GeoTargetsObj> dmas) {
		this.dmas = dmas;
	}
	public List<PlatformObj> getPlatforms() {
		return platforms;
	}
	public void setPlatforms(List<PlatformObj> platforms) {
		this.platforms = platforms;
	}
	public List<DeviceObj> getDevices() {
		return devices;
	}
	public void setDevices(List<DeviceObj> devices) {
		this.devices = devices;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public List<AdUnitDTO> getAdUnits() {
		return adUnits;
	}
	public void setAdUnits(List<AdUnitDTO> adUnits) {
		this.adUnits = adUnits;
	}
	public List<StateObj> getStates() {
		return states;
	}
	public void setStates(List<StateObj> states) {
		this.states = states;
	}
	public Date getLastUpdatedOn() {
		return lastUpdatedOn;
	}
	public void setLastUpdatedOn(Date lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}
	
	@Override
	public boolean equals(Object obj) {
		ProductsObj productsObj = (ProductsObj) obj;
		if(this.productId == productsObj.productId) {
			return true;
		}
		return false;
	}
	public List<ZipDTO> getZips() {
		return zips;
	}
	public void setZips(List<ZipDTO> zips) {
		this.zips = zips;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	public String getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
	public int getDeviceCapability() {
		return deviceCapability;
	}
	public void setDeviceCapability(int deviceCapability) {
		this.deviceCapability = deviceCapability;
	}
	
	
	
}
