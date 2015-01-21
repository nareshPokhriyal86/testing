package com.lin.server.bean;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity
@SuppressWarnings("serial")
@Index
public class PublisherPropertiesObj implements Serializable{
	@Id	private String id;
	private String name;
	private double eCPM;
	private double CHG;
	private double percentageCHG;
	private double impressionsDelivered;
	private long clicks;
	private double payout;
	private double DFPPayout;
	private String date;
	private String dfpPropertyName;
	private String stateName;
	private String stationName;
	private long requestSubmited;
	private long requestReceived;
	private double requestVariance;
	private double lastPayout;
	private long lastImpressionsDelivered;
	private String channelName;
	private String site;
	private double totalRevenue;
	private long totalImpressionsDeliveredBySiteName;
	private String dataSource;
	private double totalImpressionsDeliveredByChannelName;
	private String latitude;
	private String longitude;
	
	public PublisherPropertiesObj(){
		
	}
	public PublisherPropertiesObj(String channelName, String name, double eCPM,double impressionsDelivered, long clicks, long totalImpressionsDeliveredBySiteName, double payout,String stateName, String site,double DFPPayout) {
		this.channelName = channelName;
		this.name = name;
		this.eCPM = eCPM;
		this.impressionsDelivered = impressionsDelivered;
		this.clicks = clicks;
		this.totalImpressionsDeliveredBySiteName = totalImpressionsDeliveredBySiteName;
		this.payout = payout;
		this.stateName = stateName;
		this.site = site;
		this.DFPPayout = DFPPayout;
	}
	
	
	public PublisherPropertiesObj(String id, String name, double eCPM,
			double cHG, double percentageCHG, long impressionsDelivered,
			long clicks, double payout, String date) {
		this.id = id;
		this.name = name;
		this.eCPM = eCPM;
		CHG = cHG;
		this.percentageCHG = percentageCHG;
		this.impressionsDelivered = impressionsDelivered;
		this.clicks = clicks;
		this.payout = payout;
		this.date = date;
	}
	
	public PublisherPropertiesObj(String id, 
			String name, 
			double eCPM,
			double cHG, 
			double percentageCHG,
			long impressionsDelivered,
			long clicks, 
			double payout, 
			String date,
			 String dfpPropertyName,
			 String stateName,
			 String stationName,
			 double lastPayout,
			 long lastImpressionsDelivered) {
		this.id = id;
		this.name = name;
		this.eCPM = eCPM;
		CHG = cHG;
		this.percentageCHG = percentageCHG;
		this.impressionsDelivered = impressionsDelivered;
		this.clicks = clicks;
		this.payout = payout;
		this.date = date;
		this.dfpPropertyName=dfpPropertyName;
		this.stateName=stateName;
		this.stationName=stationName;
		this.lastPayout = lastPayout;
		this.lastImpressionsDelivered = lastImpressionsDelivered;
		
	}
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double geteCPM() {
		return eCPM;
	}
	public void seteCPM(double eCPM) {
		this.eCPM = eCPM;
	}
	public double getCHG() {
		return CHG;
	}
	public void setCHG(double cHG) {
		CHG = cHG;
	}
	public double getPercentageCHG() {
		return percentageCHG;
	}
	public void setPercentageCHG(double percentageCHG) {
		this.percentageCHG = percentageCHG;
	}
	public double getImpressionsDelivered() {
		return impressionsDelivered;
	}
	public void setImpressionsDelivered(double impressionsDelivered) {
		this.impressionsDelivered = impressionsDelivered;
	}
	public long getClicks() {
		return clicks;
	}
	public void setClicks(long clicks) {
		this.clicks = clicks;
	}
	public double getPayout() {
		return payout;
	}
	public void setPayout(double payout) {
		this.payout = payout;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

	public void setDfpPropertyName(String dfpPropertyName) {
		this.dfpPropertyName = dfpPropertyName;
	}

	public String getDfpPropertyName() {
		return dfpPropertyName;
	}
	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public void setRequestSubmited(long requestSubmited) {
		this.requestSubmited = requestSubmited;
	}

	public long getRequestSubmited() {
		return requestSubmited;
	}

	public void setRequestReceived(long requestReceived) {
		this.requestReceived = requestReceived;
	}

	public long getRequestReceived() {
		return requestReceived;
	}

	public void setRequestVariance(double requestVariance) {
		this.requestVariance = requestVariance;
	}

	public double getRequestVariance() {
		return requestVariance;
	}

	public double getLastPayout() {
		return lastPayout;
	}

	public void setLastPayout(double lastPayout) {
		this.lastPayout = lastPayout;
	}

	public long getLastImpressionsDelivered() {
		return lastImpressionsDelivered;
	}

	public void setLastImpressionsDelivered(long lastImpressionsDelivered) {
		this.lastImpressionsDelivered = lastImpressionsDelivered;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public void setTotalRevenue(double totalRevenue) {
		this.totalRevenue = totalRevenue;
	}
	public double getTotalRevenue() {
		return totalRevenue;
	}
	public void setTotalImpressionsDeliveredBySiteName(
			long totalImpressionsDeliveredBySiteName) {
		this.totalImpressionsDeliveredBySiteName = totalImpressionsDeliveredBySiteName;
	}
	public long getTotalImpressionsDeliveredBySiteName() {
		return totalImpressionsDeliveredBySiteName;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	public String getDataSource() {
		return dataSource;
	}
	public void setTotalImpressionsDeliveredByChannelName(
			double totalImpressionsDeliveredByChannelName) {
		this.totalImpressionsDeliveredByChannelName = totalImpressionsDeliveredByChannelName;
	}
	public double getTotalImpressionsDeliveredByChannelName() {
		return totalImpressionsDeliveredByChannelName;
	}
	public void setDFPPayout(double dFPPayout) {
		DFPPayout = dFPPayout;
	}
	public double getDFPPayout() {
		return DFPPayout;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLongitude() {
		return longitude;
	}
	
}
