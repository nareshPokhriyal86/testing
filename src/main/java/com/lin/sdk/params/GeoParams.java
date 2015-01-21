package com.lin.sdk.params;

public class GeoParams {
	
	private String timeStamp = "";
	private String latitue = "";
	private String longitude = "";
	private String cellId = "";
	private String lac = "";
	
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getLatitue() {
		return latitue;
	}
	public void setLatitue(String latitue) {
		this.latitue = latitue;
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
}
