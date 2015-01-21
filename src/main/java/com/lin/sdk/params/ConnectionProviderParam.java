package com.lin.sdk.params;

import java.util.List;

import com.lin.sdk.params.enums.SIMInfo;

public class ConnectionProviderParam {
	private String timeStamp = "";
	private SIMInfo sim = SIMInfo.nosim;
	private String mcc = "";
	private String mnc = "";
	private String timezone = "";
	private NetworkConnectionType networkConnection;
	private List<GeoParams> geoParams;
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public SIMInfo getSim() {
		return sim;
	}
	public void setSim(SIMInfo sim) {
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
	public NetworkConnectionType getNetworkConnection() {
		return networkConnection;
	}
	public void setNetworkConnection(NetworkConnectionType networkConnection) {
		this.networkConnection = networkConnection;
	}
	public List<GeoParams> getGeoParams() {
		return geoParams;
	}
	public void setGeoParams(List<GeoParams> geoParams) {
		this.geoParams = geoParams;
	}

}
