package com.lin.sdk.params;

import com.lin.sdk.params.enums.ConnectionType;

public class NetworkConnectionType {

	private String timeStamp = "";
	private String macAddress = "";
	private String ipAddress = "";
	private ConnectionType type = ConnectionType.notype;
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
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
	public ConnectionType getType() {
		return type;
	}
	public void setType(ConnectionType type) {
		this.type = type;
	}
	
}
