package com.lin.sdk;

import com.lin.sdk.params.DeviceParam;

public class SDKTargetingData {
	
	private String key = "";
	private String timeStamp = "";
	private String make = "";
	private String model = "";
	private String idfa = "";
	private String idfv = "";
	private String androidId = "";
	private String googleAdId = "";
	private String deviceId = "";
	private String odin = "";
	private String pushNotificationId = "";
	private DeviceParam deviceParam;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
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
	public DeviceParam getDeviceParam() {
		return deviceParam;
	}
	public void setDeviceParam(DeviceParam deviceParam) {
		this.deviceParam = deviceParam;
	}
	
}
