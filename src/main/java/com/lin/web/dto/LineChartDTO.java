package com.lin.web.dto;

import java.io.Serializable;

public class LineChartDTO implements Serializable{

	private String id;
	private String siteName;
	private String impressions;
	private String clicks;
	private String ctr;
	private String date;
	private String fillRate;
	private String eCPM;
	private String revenue;
	
	private String startDate;
	private String endDate;
	private String goal;
	private String placementName;
	
	/*private String rm_Expansions;
	private String rm_Expanding_Time;
	private String rm_Interaction_Time;
	private String rm_Interaction_Count;
	private String rm_Avg_Interaction_Time;
	private String rm_Interaction_Imps;
	private String rm_Manual_Closes;
	private String rm_Full_Screen_Imps;
	 
	private String rmv_Interactions;
	private String rmv_Interaction_Rate;
	private String rmv_Mutes;
	private String rmv_Pauses;
	private String rmv_Playes;
	private String rmv_MidPoint;
	private String rmv_Completes;
	private String rmv_Replays;
	private String rmv_Stops;
	private String rmv_Unmutes;
	private String rmv_View_Rate;*/
	 
	private String average_Interaction_Rate;
	private String average_View_Rate;
	private String completion_Rate;
	
	private String firstQuartile;
	private String midpoint;
	private String third_Quartile;
	private String complete;
	
	private String start;
	private String pause;
	private String resume;
	private String rewind;
	private String mute;
	private String unmute;
	private String fullScreen;
	
	private long imp;
	private long clk;
	
	public LineChartDTO(){
		
	}
	public LineChartDTO(String siteName, String impressions,
			String clicks, String ctr, String date) {		
		this.siteName = siteName;
		this.impressions = impressions;
		this.clicks = clicks;
		this.ctr = ctr;
		this.date = date;
	}
	
	
	
	public LineChartDTO(String impressions, String clicks, String ctr,
			String date, String fillRate, String eCPM, String revenue) {
		super();
		this.impressions = impressions;
		this.clicks = clicks;
		this.ctr = ctr;
		this.date = date;
		this.fillRate = fillRate;
		this.eCPM = eCPM;
		this.revenue = revenue;
	}
	
	@Override
	public String toString() {
		return "LineChartDTO [id=" + id + ", siteName=" + siteName
				+ ", impressions=" + impressions + ", clicks=" + clicks
				+ ", ctr=" + ctr + ", date=" + date + ", fillRate=" + fillRate
				+ ", eCPM=" + eCPM + ", revenue=" + revenue + ", startDate="
				+ startDate + ", endDate=" + endDate + ", goal=" + goal
				+ ", placementName=" + placementName + "]";
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getImpressions() {
		return impressions;
	}
	public void setImpressions(String impressions) {
		this.impressions = impressions;
	}
	public String getClicks() {
		return clicks;
	}
	public void setClicks(String clicks) {
		this.clicks = clicks;
	}
	public String getCtr() {
		return ctr;
	}
	public void setCtr(String ctr) {
		this.ctr = ctr;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getFillRate() {
		return fillRate;
	}
	public void setFillRate(String fillRate) {
		this.fillRate = fillRate;
	}
	public String geteCPM() {
		return eCPM;
	}
	public void seteCPM(String eCPM) {
		this.eCPM = eCPM;
	}
	public String getRevenue() {
		return revenue;
	}
	public void setRevenue(String revenue) {
		this.revenue = revenue;
	}
	public String getAverage_Interaction_Rate() {
		return average_Interaction_Rate;
	}
	public void setAverage_Interaction_Rate(String average_Interaction_Rate) {
		this.average_Interaction_Rate = average_Interaction_Rate;
	}
	public String getAverage_View_Rate() {
		return average_View_Rate;
	}
	public void setAverage_View_Rate(String average_View_Rate) {
		this.average_View_Rate = average_View_Rate;
	}
	public String getCompletion_Rate() {
		return completion_Rate;
	}
	public void setCompletion_Rate(String completion_Rate) {
		this.completion_Rate = completion_Rate;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getFirstQuartile() {
		return firstQuartile;
	}
	public void setFirstQuartile(String firstQuartile) {
		this.firstQuartile = firstQuartile;
	}
	public String getMidpoint() {
		return midpoint;
	}
	public void setMidpoint(String midpoint) {
		this.midpoint = midpoint;
	}
	public String getThird_Quartile() {
		return third_Quartile;
	}
	public void setThird_Quartile(String third_Quartile) {
		this.third_Quartile = third_Quartile;
	}
	public String getComplete() {
		return complete;
	}
	public void setComplete(String complete) {
		this.complete = complete;
	}
	public String getPause() {
		return pause;
	}
	public void setPause(String pause) {
		this.pause = pause;
	}
	public String getResume() {
		return resume;
	}
	public void setResume(String resume) {
		this.resume = resume;
	}
	public String getRewind() {
		return rewind;
	}
	public void setRewind(String rewind) {
		this.rewind = rewind;
	}
	public String getMute() {
		return mute;
	}
	public void setMute(String mute) {
		this.mute = mute;
	}
	public String getUnmute() {
		return unmute;
	}
	public void setUnmute(String unmute) {
		this.unmute = unmute;
	}
	public String getFullScreen() {
		return fullScreen;
	}
	public void setFullScreen(String fullScreen) {
		this.fullScreen = fullScreen;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getGoal() {
		return goal;
	}
	public void setGoal(String goal) {
		this.goal = goal;
	}
	public String getPlacementName() {
		return placementName;
	}
	public void setPlacementName(String placementName) {
		this.placementName = placementName;
	}
	public long getImp() {
		return imp;
	}
	public void setImp(long imp) {
		this.imp = imp;
	}
	public long getClk() {
		return clk;
	}
	public void setClk(long clk) {
		this.clk = clk;
	}
	
	
}
