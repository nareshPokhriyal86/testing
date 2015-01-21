package com.lin.web.dto;

public class MojivaReportDTO {
	
	private String siteId;
	private String siteName;
	private String cpmImpressions;
	private String cpcImpressions; 
	private String totalImpressions;
	private String requests;
	private String clicks;
	private String cpmRevenue;
	private String cpcRevenue;
	private String ctr;
	private String eCPM;
	private String totalRevenue;
	private String fillRate;
	
	
	private String id;
	private String domain;
	private String networkId;
	
	private String date;
	
	public MojivaReportDTO(){
		
	}
	
	
	public MojivaReportDTO( String id, String domain,String networkId) {		
		this.id = id;
		this.domain = domain;
		this.networkId = networkId;
	}

	public MojivaReportDTO(String siteId, String siteName,
			String cpmImpressions, String cpcImpressions,
			String totalImpressions, String requests, String clicks,
			String cpmRevenue, String cpcRevenue, String ctr, String eCPM,
			String totalRevenue) {
		this.siteId = siteId;
		this.siteName = siteName;
		this.cpmImpressions = cpmImpressions;
		this.cpcImpressions = cpcImpressions;
		this.totalImpressions = totalImpressions;
		this.requests = requests;
		this.clicks = clicks;
		this.cpmRevenue = cpmRevenue;
		this.cpcRevenue = cpcRevenue;
		this.ctr = ctr;
		this.eCPM = eCPM;
		this.totalRevenue = totalRevenue;	
	}
	
	public MojivaReportDTO(String siteId, String siteName,
			String cpmImpressions, String cpcImpressions,
			String totalImpressions, String requests, String clicks,
			String cpmRevenue, String cpcRevenue, String ctr, String eCPM,
			String totalRevenue,String date) {
		this.siteId = siteId;
		this.siteName = siteName;
		this.cpmImpressions = cpmImpressions;
		this.cpcImpressions = cpcImpressions;
		this.totalImpressions = totalImpressions;
		this.requests = requests;
		this.clicks = clicks;
		this.cpmRevenue = cpmRevenue;
		this.cpcRevenue = cpcRevenue;
		this.ctr = ctr;
		this.eCPM = eCPM;
		this.totalRevenue = totalRevenue;
		this.date = date;	
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MojivaReportDTO [siteId=").append(siteId)
				.append(", siteName=").append(siteName)
				.append(", cpmImpressions=").append(cpmImpressions)
				.append(", cpcImpressions=").append(cpcImpressions)
				.append(", totalImpressions=").append(totalImpressions)
				.append(", requests=").append(requests).append(", clicks=")
				.append(clicks).append(", cpmRevenue=").append(cpmRevenue)
				.append(", cpcRevenue=").append(cpcRevenue).append(", ctr=")
				.append(ctr).append(", eCPM=").append(eCPM)
				.append(", totalRevenue=").append(totalRevenue)
				.append(", fillRate=").append(fillRate).append(", id=")
				.append(id).append(", domain=").append(domain)
				.append(", networkId=").append(networkId)
				.append(", date=").append(date).append("]");
		return builder.toString();
	}


	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getCpmImpressions() {
		return cpmImpressions;
	}
	public void setCpmImpressions(String cpmImpressions) {
		this.cpmImpressions = cpmImpressions;
	}
	public String getCpcImpressions() {
		return cpcImpressions;
	}
	public void setCpcImpressions(String cpcImpressions) {
		this.cpcImpressions = cpcImpressions;
	}
	public String getTotalImpressions() {
		return totalImpressions;
	}
	public void setTotalImpressions(String totalImpressions) {
		this.totalImpressions = totalImpressions;
	}
	public String getRequests() {
		return requests;
	}
	public void setRequests(String requests) {
		this.requests = requests;
	}
	public String getClicks() {
		return clicks;
	}
	public void setClicks(String clicks) {
		this.clicks = clicks;
	}
	public String getCpmRevenue() {
		return cpmRevenue;
	}
	public void setCpmRevenue(String cpmRevenue) {
		this.cpmRevenue = cpmRevenue;
	}
	public String getCpcRevenue() {
		return cpcRevenue;
	}
	public void setCpcRevenue(String cpcRevenue) {
		this.cpcRevenue = cpcRevenue;
	}
	public String getCtr() {
		return ctr;
	}
	public void setCtr(String ctr) {
		this.ctr = ctr;
	}
	public String geteCPM() {
		return eCPM;
	}
	public void seteCPM(String eCPM) {
		this.eCPM = eCPM;
	}
	public String getTotalRevenue() {
		return totalRevenue;
	}
	public void setTotalRevenue(String totalRevenue) {
		this.totalRevenue = totalRevenue;
	}
	public void setFillRate(String fillRate) {
		this.fillRate = fillRate;
	}
	public String getFillRate() {
		return fillRate;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getDomain() {
		return domain;
	}

	public void setNetworkId(String networkId) {
		this.networkId = networkId;
	}

	public String getNetworkId() {
		return networkId;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public String getDate() {
		return date;
	}		
	
}
