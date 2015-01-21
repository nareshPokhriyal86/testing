package com.lin.sdk.params;

import java.util.List;

import com.lin.sdk.params.enums.ConnectionType;


public class NetworkParams {

	private String wifiTimeStamp = "";
	private String wifiMacAddress = "";
	private String wifiName = "";
	private String wifiIpAddress = "";
	private ConnectionType type = ConnectionType.wifi;
	private List<ConnectionProviderParam> connectionProvider;

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
	public ConnectionType getType() {
		return type;
	}
	public void setType(ConnectionType type) {
		this.type = type;
	}
	public List<ConnectionProviderParam> getConnectionProvider() {
		return connectionProvider;
	}
	public void setConnectionProvider(List<ConnectionProviderParam> connectionProvider) {
		this.connectionProvider = connectionProvider;
	}

}
