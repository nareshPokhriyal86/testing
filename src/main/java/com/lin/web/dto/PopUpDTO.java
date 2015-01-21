package com.lin.web.dto;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class PopUpDTO implements Serializable{
	
	private String id;
	private String title;
	private String bookedImpression;
	
	private String date;
	
	private String eCPM;
	private String clicksLifeTime;	
	private String ctrLifeTime;
	private String payout;
	private String impressionDeliveredLifeTime;
	
	private String revenueDeliveredLifeTime;	
	private String revenueRemainingLifeTime;	
	private String changeCTRLifeTime;
	
	private String impressionForcasted;
	private String sellThroughRate;
	private String impressionAvailable;
	private String impressionReserved;
	
	
	private String timePeriod;
	private String chartData;
	
	private String revenueDeliveredInSelectedTime;
	private String revenueRemainingInSelectedTime;
	private String changeCTRInSelectedTime;
	
	private String clicksInSelectedTime;
	private String ctrInSelectedTime;	
	private String impressionDeliveredInSelectedTime;	
	private String payoutInSelectedTime;
	private String eCPMInSelectedTime;
	
	private String impressionsDeliveredLastTime;
	private String clicksLastTime;
	private String ctrLastTime;
	private String eCPMLastTime;
	private String payoutsLastTime;
	
	private String impressionsDeliveredMTD;
	private String clicksMTD;
	private String ctrMTD;
	private String eCPMMTD;
	private String payoutsMTD;
	
	private String order;
	
	private String costType;
	
	private List<CommonDTO> popUpGraphDataList;
	
	
	public PopUpDTO(){
		
	}	

	@Override
	public String toString() {
		return "PopUpDTO [id=" + id + ", title=" + title
				+ ", bookedImpression=" + bookedImpression + ", date=" + date
				+ ", eCPM=" + eCPM + ", clicksLifeTime=" + clicksLifeTime
				+ ", ctrLifeTime=" + ctrLifeTime + ", payout=" + payout
				+ ", impressionDeliveredLifeTime="
				+ impressionDeliveredLifeTime + ", revenueDeliveredLifeTime="
				+ revenueDeliveredLifeTime + ", revenueRemainingLifeTime="
				+ revenueRemainingLifeTime + ", changeCTRLifeTime="
				+ changeCTRLifeTime + ", impressionForcasted="
				+ impressionForcasted + ", sellThroughRate=" + sellThroughRate
				+ ", impressionAvailable=" + impressionAvailable
				+ ", impressionReserved=" + impressionReserved
				+ ", timePeriod=" + timePeriod + ", chartData=" + chartData
				+ ", revenueDeliveredInSelectedTime="
				+ revenueDeliveredInSelectedTime
				+ ", revenueRemainingInSelectedTime="
				+ revenueRemainingInSelectedTime + ", changeCTRInSelectedTime="
				+ changeCTRInSelectedTime + ", clicksInSelectedTime="
				+ clicksInSelectedTime + ", ctrInSelectedTime="
				+ ctrInSelectedTime + ", impressionDeliveredInSelectedTime="
				+ impressionDeliveredInSelectedTime + ", payoutInSelectedTime="
				+ payoutInSelectedTime + ", eCPMInSelectedTime="
				+ eCPMInSelectedTime + ", impressionsDeliveredLastTime="
				+ impressionsDeliveredLastTime + ", clicksLastTime="
				+ clicksLastTime + ", ctrLastTime=" + ctrLastTime
				+ ", eCPMLastTime=" + eCPMLastTime + ", payoutsLastTime="
				+ payoutsLastTime + ", impressionsDeliveredMTD="
				+ impressionsDeliveredMTD + ", clicksMTD=" + clicksMTD
				+ ", ctrMTD=" + ctrMTD + ", eCPMMTD=" + eCPMMTD
				+ ", payoutsMTD=" + payoutsMTD + ", popUpGraphDataList="
				+ popUpGraphDataList + "]";
	}
	

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBookedImpression() {
		return bookedImpression;
	}
	public void setBookedImpression(String bookedImpression) {
		this.bookedImpression = bookedImpression;
	}
	
	public String getImpressionDeliveredLifeTime() {
		return impressionDeliveredLifeTime;
	}
	public void setImpressionDeliveredLifeTime(String impressionDeliveredLifeTime) {
		this.impressionDeliveredLifeTime = impressionDeliveredLifeTime;
	}
	public String getImpressionDeliveredInSelectedTime() {
		return impressionDeliveredInSelectedTime;
	}
	public void setImpressionDeliveredInSelectedTime(
			String impressionDeliveredInSelectedTime) {
		this.impressionDeliveredInSelectedTime = impressionDeliveredInSelectedTime;
	}
	public String getClicksLifeTime() {
		return clicksLifeTime;
	}
	public void setClicksLifeTime(String clicksLifeTime) {
		this.clicksLifeTime = clicksLifeTime;
	}
	public String getClicksInSelectedTime() {
		return clicksInSelectedTime;
	}
	public void setClicksInSelectedTime(String clicksInSelectedTime) {
		this.clicksInSelectedTime = clicksInSelectedTime;
	}
	public String getCtrLifeTime() {
		return ctrLifeTime;
	}
	public void setCtrLifeTime(String ctrLifeTime) {
		this.ctrLifeTime = ctrLifeTime;
	}
	public String getCtrInSelectedTime() {
		return ctrInSelectedTime;
	}
	public void setCtrInSelectedTime(String ctrInSelectedTime) {
		this.ctrInSelectedTime = ctrInSelectedTime;
	}
	public String getRevenueDeliveredLifeTime() {
		return revenueDeliveredLifeTime;
	}
	public void setRevenueDeliveredLifeTime(String revenueDeliveredLifeTime) {
		this.revenueDeliveredLifeTime = revenueDeliveredLifeTime;
	}
	public String getRevenueDeliveredInSelectedTime() {
		return revenueDeliveredInSelectedTime;
	}
	public void setRevenueDeliveredInSelectedTime(
			String revenueDeliveredInSelectedTime) {
		this.revenueDeliveredInSelectedTime = revenueDeliveredInSelectedTime;
	}
	public String getRevenueRemainingLifeTime() {
		return revenueRemainingLifeTime;
	}
	public void setRevenueRemainingLifeTime(String revenueRemainingLifeTime) {
		this.revenueRemainingLifeTime = revenueRemainingLifeTime;
	}
	public String getRevenueRemainingInSelectedTime() {
		return revenueRemainingInSelectedTime;
	}
	public void setRevenueRemainingInSelectedTime(
			String revenueRemainingInSelectedTime) {
		this.revenueRemainingInSelectedTime = revenueRemainingInSelectedTime;
	}
	public String getChangeCTRLifeTime() {
		return changeCTRLifeTime;
	}
	public void setChangeCTRLifeTime(String changeCTRLifeTime) {
		this.changeCTRLifeTime = changeCTRLifeTime;
	}
	public String getChangeCTRInSelectedTime() {
		return changeCTRInSelectedTime;
	}
	public void setChangeCTRInSelectedTime(String changeCTRInSelectedTime) {
		this.changeCTRInSelectedTime = changeCTRInSelectedTime;
	}


	public void setChartData(String chartData) {
		this.chartData = chartData;
	}


	public String getChartData() {
		return chartData;
	}


	public void setPayout(String payout) {
		this.payout = payout;
	}


	public String getPayout() {
		return payout;
	}


	public void setImpressionForcasted(String impressionForcasted) {
		this.impressionForcasted = impressionForcasted;
	}


	public String getImpressionForcasted() {
		return impressionForcasted;
	}


	public void setSellThroughRate(String sellThroughRate) {
		this.sellThroughRate = sellThroughRate;
	}


	public String getSellThroughRate() {
		return sellThroughRate;
	}


	public void setImpressionAvailable(String impressionAvailable) {
		this.impressionAvailable = impressionAvailable;
	}


	public String getImpressionAvailable() {
		return impressionAvailable;
	}


	public void setImpressionReserved(String impressionReserved) {
		this.impressionReserved = impressionReserved;
	}


	public String getImpressionReserved() {
		return impressionReserved;
	}


	public String getImpressionsDeliveredLastTime() {
		return impressionsDeliveredLastTime;
	}


	public void setImpressionsDeliveredLastTime(String impressionsDeliveredLastTime) {
		this.impressionsDeliveredLastTime = impressionsDeliveredLastTime;
	}


	public String getClicksLastTime() {
		return clicksLastTime;
	}


	public void setClicksLastTime(String clicksLastTime) {
		this.clicksLastTime = clicksLastTime;
	}


	public String getCtrLastTime() {
		return ctrLastTime;
	}


	public void setCtrLastTime(String ctrLastTime) {
		this.ctrLastTime = ctrLastTime;
	}


	public String geteCPMLastTime() {
		return eCPMLastTime;
	}


	public void seteCPMLastTime(String eCPMLastTime) {
		this.eCPMLastTime = eCPMLastTime;
	}


	public String getPayoutsLastTime() {
		return payoutsLastTime;
	}


	public void setPayoutsLastTime(String payoutsLastTime) {
		this.payoutsLastTime = payoutsLastTime;
	}


	public String getImpressionsDeliveredMTD() {
		return impressionsDeliveredMTD;
	}


	public void setImpressionsDeliveredMTD(String impressionsDeliveredMTD) {
		this.impressionsDeliveredMTD = impressionsDeliveredMTD;
	}


	public String getClicksMTD() {
		return clicksMTD;
	}


	public void setClicksMTD(String clicksMTD) {
		this.clicksMTD = clicksMTD;
	}


	public String getCtrMTD() {
		return ctrMTD;
	}


	public void setCtrMTD(String ctrMTD) {
		this.ctrMTD = ctrMTD;
	}


	public String geteCPMMTD() {
		return eCPMMTD;
	}


	public void seteCPMMTD(String eCPMMTD) {
		this.eCPMMTD = eCPMMTD;
	}


	public String getPayoutsMTD() {
		return payoutsMTD;
	}


	public void setPayoutsMTD(String payoutsMTD) {
		this.payoutsMTD = payoutsMTD;
	}


	public void setTimePeriod(String timePeriod) {
		this.timePeriod = timePeriod;
	}


	public String getTimePeriod() {
		return timePeriod;
	}


	public void setPayoutInSelectedTime(String payoutInSelectedTime) {
		this.payoutInSelectedTime = payoutInSelectedTime;
	}


	public String getPayoutInSelectedTime() {
		return payoutInSelectedTime;
	}


	public void seteCPMInSelectedTime(String eCPMInSelectedTime) {
		this.eCPMInSelectedTime = eCPMInSelectedTime;
	}


	public String geteCPMInSelectedTime() {
		return eCPMInSelectedTime;
	}


	public void seteCPM(String eCPM) {
		this.eCPM = eCPM;
	}


	public String geteCPM() {
		return eCPM;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public String getDate() {
		return date;
	}


	public void setPopUpGraphDataList(List<CommonDTO> popUpGraphDataList) {
		this.popUpGraphDataList = popUpGraphDataList;
	}


	public List<CommonDTO> getPopUpGraphDataList() {
		return popUpGraphDataList;
	}

	public void setCostType(String costType) {
		this.costType = costType;
	}

	public String getCostType() {
		return costType;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getOrder() {
		return order;
	}
	
	
}
