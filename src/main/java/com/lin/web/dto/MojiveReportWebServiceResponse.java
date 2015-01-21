package com.lin.web.dto;

import java.util.List;

public class MojiveReportWebServiceResponse {
	
	private String login;
	private String error;
	private String site;	
	private String name;
	private String site_id;
	private String cpm_impressions;
	private String cpc_impressions; 
	private String total_impressions;
	private String requests;
	private String clicks;
	private String cpm_revenue;
	private String cpc_revenue;
	private String ctr;
	private String eCPM;
	private String total_revenue;
	private List<MojivaReportDTO> siteList;
	
	public MojiveReportWebServiceResponse(){
		
	}
	
	/*public MojiveReportWebServiceResponse(String site, String name, String site_id, String cpm_impressions,
			String cpc_impressions, String total_impressions, String requests,
			String clicks, String cpm_revenue, String cpc_revenue, String ctr,
			String eCPM, String total_revenue) {		
		this.site = site;
		this.name = name;
		this.site_id = site_id;
		this.cpm_impressions = cpm_impressions;
		this.cpc_impressions = cpc_impressions;
		this.total_impressions = total_impressions;
		this.requests = requests;
		this.clicks = clicks;
		this.cpm_revenue = cpm_revenue;
		this.cpc_revenue = cpc_revenue;
		this.ctr = ctr;
		this.eCPM = eCPM;
		this.total_revenue = total_revenue;
	}*/
	
	
	
	
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MojiveReportWebServiceResponse [login=").append(login)
				.append(", error=").append(error).append(", site=")
				.append(site).append(", name=").append(name)
				.append(", site_id=").append(site_id)
				.append(", cpm_impressions=").append(cpm_impressions)
				.append(", cpc_impressions=").append(cpc_impressions)
				.append(", total_impressions=").append(total_impressions)
				.append(", requests=").append(requests).append(", clicks=")
				.append(clicks).append(", cpm_revenue=").append(cpm_revenue)
				.append(", cpc_revenue=").append(cpc_revenue).append(", ctr=")
				.append(ctr).append(", eCPM=").append(eCPM)
				.append(", total_revenue=").append(total_revenue).append("]");
		return builder.toString();
	}

	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSite_id() {
		return site_id;
	}
	public void setSite_id(String site_id) {
		this.site_id = site_id;
	}
	public String getCpm_impressions() {
		return cpm_impressions;
	}
	public void setCpm_impressions(String cpm_impressions) {
		this.cpm_impressions = cpm_impressions;
	}
	public String getCpc_impressions() {
		return cpc_impressions;
	}
	public void setCpc_impressions(String cpc_impressions) {
		this.cpc_impressions = cpc_impressions;
	}
	public String getTotal_impressions() {
		return total_impressions;
	}
	public void setTotal_impressions(String total_impressions) {
		this.total_impressions = total_impressions;
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
	public String getCpm_revenue() {
		return cpm_revenue;
	}
	public void setCpm_revenue(String cpm_revenue) {
		this.cpm_revenue = cpm_revenue;
	}
	public String getCpc_revenue() {
		return cpc_revenue;
	}
	public void setCpc_revenue(String cpc_revenue) {
		this.cpc_revenue = cpc_revenue;
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
	public String getTotal_revenue() {
		return total_revenue;
	}
	public void setTotal_revenue(String total_revenue) {
		this.total_revenue = total_revenue;
	}

	public void setSiteList(List<MojivaReportDTO> siteList) {
		this.siteList = siteList;
	}

	public List<MojivaReportDTO> getSiteList() {
		return siteList;
	}
	
	
}
