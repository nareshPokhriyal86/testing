package com.lin.sdk.params;


public class DeviceParam {
	private String os = "";
	private String osVersion = "";
	private String imei = "";
	private String meid = "";
	private String serialId = "";
	private boolean multiSim = false;
	private NetworkParams networkParams;
	private UserParams userParams;
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
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
	public String getMeid() {
		return meid;
	}
	public void setMeid(String meid) {
		this.meid = meid;
	}
	public String getSerialId() {
		return serialId;
	}
	public void setSerialId(String serialId) {
		this.serialId = serialId;
	}
	public boolean isMultiSim() {
		return multiSim;
	}
	public void setMultiSim(boolean multiSim) {
		this.multiSim = multiSim;
	}
	public NetworkParams getNetworkParams() {
		return networkParams;
	}
	public void setNetworkParams(NetworkParams networkParams) {
		this.networkParams = networkParams;
	}
	public UserParams getUserParams() {
		return userParams;
	}
	public void setUserParams(UserParams userParams) {
		this.userParams = userParams;
	}
	
}
