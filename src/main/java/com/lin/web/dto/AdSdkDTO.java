package com.lin.web.dto;

import java.io.Serializable;
import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.lin.web.util.DateUtil;

@Entity
public class AdSdkDTO implements Serializable{ 
	@Id private Long id;
	private String  key;
	private String  timestamp;
	private String  make;
	private String  model;
	private String  idfa;
	private String  idfv;
	private String  androidId;
	private String  googleAdId ;
	private String  deviceId;
	private String  odin	;
	private String  pushNotificationId;
	//Device parameters
	private String  osDeviceParam;
	private String  osVersion;
	private String  imei;
	private String  meid;
	private String  serialId;
	private String  multisim;
	// Network parameters
	private String  wifiTimeStamp;
	private String  wifiMacAddress;
	private String  wifiName;
	private String  wifiIpAddress;
	private String  networkType;
	// connection provider
	private String  connectionProviderTimestamp;
	private String  sim;
	private String  mcc;
	private String  mnc;
	private String  timezone;
	// network connection
	private String  networkConnectionTimestamp;
	private String  macAddress;
	private String  ipAddress;
	private String  networkConnectionType;
	// geo Params
	private String  geoTimestamp;
	private String  latitude;
	private String  longitude;
	private String  cellId;
	private String  lac;
	// user params
	private String  language;
	private String  appstoreId;
	private String  country;
	private String  firstName;
	private String  lastName;
	
	private boolean isMigrated;
	private String insertDate;
	private String insertTime;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getMake() {
		return make;
	}
	public void setMake(String make) {
		this.make = make;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getIdfa() {
		return idfa;
	}
	public void setIdfa(String idfa) {
		this.idfa = idfa;
	}
	public String getIdfv() {
		return idfv;
	}
	public void setIdfv(String idfv) {
		this.idfv = idfv;
	}
	public String getAndroidId() {
		return androidId;
	}
	public void setAndroidId(String androidId) {
		this.androidId = androidId;
	}
	public String getGoogleAdId() {
		return googleAdId;
	}
	public void setGoogleAdId(String googleAdId) {
		this.googleAdId = googleAdId;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getOdin() {
		return odin;
	}
	public void setOdin(String odin) {
		this.odin = odin;
	}
	public String getPushNotificationId() {
		return pushNotificationId;
	}
	public void setPushNotificationId(String pushNotificationId) {
		this.pushNotificationId = pushNotificationId;
	}
	public String getOsDeviceParam() {
		return osDeviceParam;
	}
	public void setOsDeviceParam(String osDeviceParam) {
		this.osDeviceParam = osDeviceParam;
	}
	public String getOsVersion() {
		return osVersion;
	}
	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getSerialId() {
		return serialId;
	}
	public void setSerialId(String serialId) {
		this.serialId = serialId;
	}
	public String getMultisim() {
		return multisim;
	}
	public void setMultisim(String multisim) {
		this.multisim = multisim;
	}
	public String getWifiTimeStamp() {
		return wifiTimeStamp;
	}
	public void setWifiTimeStamp(String wifiTimeStamp) {
		this.wifiTimeStamp = wifiTimeStamp;
	}
	public String getWifiMacAddress() {
		return wifiMacAddress;
	}
	public void setWifiMacAddress(String wifiMacAddress) {
		this.wifiMacAddress = wifiMacAddress;
	}
	public String getWifiName() {
		return wifiName;
	}
	public void setWifiName(String wifiName) {
		this.wifiName = wifiName;
	}
	public String getWifiIpAddress() {
		return wifiIpAddress;
	}
	public void setWifiIpAddress(String wifiIpAddress) {
		this.wifiIpAddress = wifiIpAddress;
	}
	public String getNetworkType() {
		return networkType;
	}
	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}
	public String getConnectionProviderTimestamp() {
		return connectionProviderTimestamp;
	}
	public void setConnectionProviderTimestamp(String connectionProviderTimestamp) {
		this.connectionProviderTimestamp = connectionProviderTimestamp;
	}
	public String getSim() {
		return sim;
	}
	public void setSim(String sim) {
		this.sim = sim;
	}
	public String getMcc() {
		return mcc;
	}
	public void setMcc(String mcc) {
		this.mcc = mcc;
	}
	public String getMnc() {
		return mnc;
	}
	public void setMnc(String mnc) {
		this.mnc = mnc;
	}
	public String getTimezone() {
		return timezone;
	}
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	public String getNetworkConnectionTimestamp() {
		return networkConnectionTimestamp;
	}
	public void setNetworkConnectionTimestamp(String networkConnectionTimestamp) {
		this.networkConnectionTimestamp = networkConnectionTimestamp;
	}
	public String getMacAddress() {
		return macAddress;
	}
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getNetworkConnectionType() {
		return networkConnectionType;
	}
	public void setNetworkConnectionType(String networkConnectionType) {
		this.networkConnectionType = networkConnectionType;
	}
	public String getGeoTimestamp() {
		return geoTimestamp;
	}
	public void setGeoTimestamp(String geoTimestamp) {
		this.geoTimestamp = geoTimestamp;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getCellId() {
		return cellId;
	}
	public void setCellId(String cellId) {
		this.cellId = cellId;
	}
	public String getLac() {
		return lac;
	}
	public void setLac(String lac) {
		this.lac = lac;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getAppstoreId() {
		return appstoreId;
	}
	public void setAppstoreId(String appstoreId) {
		this.appstoreId = appstoreId;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	 
	public String getMeid() {
		return meid;
	}
	public void setMeid(String meid) {
		this.meid = meid;
	}
	
	
	public String getInsertDate() {
		return insertDate;
	}
	public void setInsertDate(String insertDate) {
		this.insertDate = insertDate;
	}
	public String getInsertTime() {
		return insertTime;
	}
	public void setInsertTime(String insertTime) {
		this.insertTime = insertTime;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "AdSdkDTO [id=" + id + ", key=" + key + ", timestamp="
				+ timestamp + ", make=" + make + ", model=" + model + ", idfa="
				+ idfa + ", idfv=" + idfv + ", androidId=" + androidId
				+ ", googleAdId=" + googleAdId + ", deviceId=" + deviceId
				+ ", odin=" + odin + ", pushNotificationId="
				+ pushNotificationId + ", osDeviceParam=" + osDeviceParam
				+ ", osVersion=" + osVersion + ", imei=" + imei + ", serialId="
				+ serialId + ", multisim=" + multisim + ", wifiTimeStamp="
				+ wifiTimeStamp + ", wifiMacAddress=" + wifiMacAddress
				+ ", wifiName=" + wifiName + ", wifiIpAddress=" + wifiIpAddress
				+ ", networkType=" + networkType
				+ ", connectionProviderTimestamp="
				+ connectionProviderTimestamp + ", sim=" + sim + ", mcc=" + mcc
				+ ", mnc=" + mnc + ", timezone=" + timezone
				+ ", networkConnectionTimestamp=" + networkConnectionTimestamp
				+ ", macAddress=" + macAddress + ", ipAddress=" + ipAddress
				+ ", networkConnectionType=" + networkConnectionType
				+ ", geoTimestamp=" + geoTimestamp + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", cellId=" + cellId + ", lac="
				+ lac + ", language=" + language + ", appstoreId=" + appstoreId
				+ ", country=" + country + ", firstName=" + firstName
				+ ", lastName=" + lastName + "]";
	}
	public boolean isMigrated() {
		return isMigrated;
	}
	public void setMigrated(boolean isMigrated) {
		this.isMigrated = isMigrated;
	}
	
	public String[][] getColumnValueArray(){
		AdSdkDTO dto = this;
		return new String[][]{
				{"key",dto.getKey()},
				{"timestamp",dto.getTimestamp()},
				{"make",dto.getMake()},
				{"model",dto.getModel()},
				{"idfa",dto.getIdfa()},
				{"idfv",dto.getIdfv()},
				{"androidId",dto.getAndroidId()},
				{"googleAdId",dto.getGoogleAdId()},
				{"deviceId",dto.getDeviceId()},
				{"odin",dto.getOdin()},
				{"pushNotificationId",dto.getPushNotificationId()},
					//Device parameters
				{"osDeviceParam",dto.getOsDeviceParam()},
				{"osVersion",dto.getOsVersion()},
				{"imei",dto.getImei()},
				{"meid",dto.getMeid()},
				{"serialId",dto.getSerialId()},
				{"multisim",dto.getMultisim()},
					// Network parameters
				{"wifiTimeStamp",dto.getWifiTimeStamp()},
				{"wifiMacAddress",dto.getWifiMacAddress()},
				{"wifiName",dto.getWifiName()},
				{"wifiIpAddress",dto.getWifiIpAddress()},
				{"networkType",dto.getNetworkConnectionType()},
					// connection provider
				{"connectionProviderTimestamp",dto.getConnectionProviderTimestamp()},
				{"sim",dto.getSim()},
				{"mcc",dto.getMcc()},
				{"mnc",dto.getMnc()},
				{"timezone",dto.getTimezone()},
					// network connection
				{"networkConnectionTimestamp",dto.getNetworkConnectionTimestamp()},
				{"macAddress",dto.getMacAddress()},
				{"ipAddress",dto.getIpAddress()},
				{"networkConnectionType",dto.getNetworkConnectionType()},
					// geo Params
				{"geoTimestamp",dto.getGeoTimestamp()},
				{"latitude",dto.getLatitude()},
				{"longitude",dto.getLongitude()},
				{"cellId",dto.getCellId()},
				{"lac",dto.getLac()},
					// user params
				{"language",dto.getLanguage()},
				{"appstoreId",dto.getAppstoreId()},
				{"country",dto.getCountry()},
				{"firstName",dto.getFirstName()},
				{"lastName",dto.getLastName()},
				{"insertDate",this.insertDate},
				{"insertTimeStamp",this.insertTime}
		};
	}
	
}
