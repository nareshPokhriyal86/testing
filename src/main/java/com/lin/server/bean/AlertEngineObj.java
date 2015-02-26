package com.lin.server.bean;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
/*
 * It is a Alert Engine entity bean for datastore to save daily alerts for campaign performance
 *  
 * @author Shubham Goel
 */
@Index

public class AlertEngineObj implements Serializable,Comparable<AlertEngineObj>{
	@Id	private String id;
	private long campaignId;
	private long placementId;
	private String campaignName;
	private String placementName;
	private long publisherId;
	private String publisherName;
	private Date alertDate;
	private long rateType;
	private int campaignStatus;
	private String startDate;
	private String endDate;
	private long placementGoal;
	//Daily Pacing Alert Values
	private long dailyPacingExpected;
	private long dailyPacingCurrent;
	private long dailyPacingReviced;
	private String companyId;
	private int dailyPacingAlert;
	
	//Goal CTR Alert Values
	private float goalCTRExpected;
	private float goalCTRCurrent;
	private int goalCTRAlert;
	
	//Delivery Alert Values
	private long deliveryExpected;
	private long deliveryCurent;
	private int deliveyAlert;
	
	// current flight details
	private long flightDailyPacingCurrent;
	private long flightDailyPacingExpected;
	private long flightDailyPacingRevised;
	private long flightGoal;	
	private String flightStartDate;
	private String flightEndDate;
	private long flightDeliveryExpected;
	private long flightDeliveryCurent;
	private int flightDeliveyAlert;
	private int flightId;
	
	public AlertEngineObj() {
		
	}
	


	public AlertEngineObj(String id, long campaignId, long placementId,
			String campaignName, String placementName, long publisherId,
			Date alertDate, long rateType, int campaignStatus,
			String startDate, String endDate, long dailyPacingExpected,
			long dailyPacingCurrent, long dailyPacingReviced,
			int dailyPacingAlert, float goalCTRExpected, float goalCTRCurrent,
			int goalCTRAlert, long deliveryExpected, long deliveryCurent,
			int deliveyAlert) {
		super();
		this.id = id;
		this.campaignId = campaignId;
		this.placementId = placementId;
		this.campaignName = campaignName;
		this.placementName = placementName;
		this.publisherId = publisherId;
		this.alertDate = alertDate;
		this.rateType = rateType;
		this.campaignStatus = campaignStatus;
		this.startDate = startDate;
		this.endDate = endDate;
		this.dailyPacingExpected = dailyPacingExpected;
		this.dailyPacingCurrent = dailyPacingCurrent;
		this.dailyPacingReviced = dailyPacingReviced;
		this.dailyPacingAlert = dailyPacingAlert;
		this.goalCTRExpected = goalCTRExpected;
		this.goalCTRCurrent = goalCTRCurrent;
		this.goalCTRAlert = goalCTRAlert;
		this.deliveryExpected = deliveryExpected;
		this.deliveryCurent = deliveryCurent;
		this.deliveyAlert = deliveyAlert;
	}



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(long campaignId) {
		this.campaignId = campaignId;
	}

	public long getPlacementId() {
		return placementId;
	}

	public void setPlacementId(long placementId) {
		this.placementId = placementId;
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public String getPlacementName() {
		return placementName;
	}

	public void setPlacementName(String placementName) {
		this.placementName = placementName;
	}

	public long getPublisherId() {
		return publisherId;
	}

	public void setPublisherId(long publisherId) {
		this.publisherId = publisherId;
	}

	public Date getAlertDate() {
		return alertDate;
	}

	public void setAlertDate(Date alertDate) {
		this.alertDate = alertDate;
	}

	public long getDailyPacingExpected() {
		return dailyPacingExpected;
	}

	public void setDailyPacingExpected(long dailyPacingExpected) {
		this.dailyPacingExpected = dailyPacingExpected;
	}

	public long getDailyPacingCurrent() {
		return dailyPacingCurrent;
	}

	public void setDailyPacingCurrent(long dailyPacingCurrent) {
		this.dailyPacingCurrent = dailyPacingCurrent;
	}

	public long getDailyPacingReviced() {
		return dailyPacingReviced;
	}

	public void setDailyPacingReviced(long dailyPacingReviced) {
		this.dailyPacingReviced = dailyPacingReviced;
	}

	public int getDailyPacingAlert() {
		return dailyPacingAlert;
	}

	public void setDailyPacingAlert(int dailyPacingAlert) {
		this.dailyPacingAlert = dailyPacingAlert;
	}

	public float getGoalCTRExpected() {
		return goalCTRExpected;
	}

	public void setGoalCTRExpected(float goalCTRExpected) {
		this.goalCTRExpected = goalCTRExpected;
	}

	public float getGoalCTRCurrent() {
		return goalCTRCurrent;
	}

	public void setGoalCTRCurrent(float goalCTRCurrent) {
		this.goalCTRCurrent = goalCTRCurrent;
	}

	public int getGoalCTRAlert() {
		return goalCTRAlert;
	}

	public void setGoalCTRAlert(int goalCTRAlert) {
		this.goalCTRAlert = goalCTRAlert;
	}

	public long getDeliveryExpected() {
		return deliveryExpected;
	}

	public void setDeliveryExpected(long deliveryExpected) {
		this.deliveryExpected = deliveryExpected;
	}

	public long getDeliveryCurent() {
		return deliveryCurent;
	}

	public void setDeliveryCurent(long deliveryCurent) {
		this.deliveryCurent = deliveryCurent;
	}

	public int getDeliveyAlert() {
		return deliveyAlert;
	}

	public void setDeliveyAlert(int deliveyAlert) {
		this.deliveyAlert = deliveyAlert;
	}


	public long getRateType() {
		return rateType;
	}


	public void setRateType(long rateType) {
		this.rateType = rateType;
	}


	public int getCampaignStatus() {
		return campaignStatus;
	}


	public void setCampaignStatus(int campaignStatus) {
		this.campaignStatus = campaignStatus;
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



	public long getPlacementGoal() {
		return placementGoal;
	}



	public void setPlacementGoal(long placementGoal) {
		this.placementGoal = placementGoal;
	}


	public String getPublisherName() {
		return publisherName;
	}



	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}
	
	/*public static final Comparator<AlertEngineObj> alertDateComparator = new Comparator<AlertEngineObj>(){

        @Override
        public int compare(AlertEngineObj o1, AlertEngineObj o2) {
            return o1.alertDate.compareTo(o2.alertDate);
        }
      
    };
    
    public static final Comparator<AlertEngineObj> campaignNameComparator = new Comparator<AlertEngineObj>(){

        @Override
        public int compare(AlertEngineObj o1, AlertEngineObj o2) {
            return o1.campaignName.compareTo(o2.campaignName);
        }
      
    };
    
    public static final Comparator<AlertEngineObj> placementNameComparator = new Comparator<AlertEngineObj>(){

        @Override
        public int compare(AlertEngineObj o1, AlertEngineObj o2) {
            return o1.placementName.compareTo(o2.placementName);
        }
      
    };*/



	@Override
	public int compareTo(AlertEngineObj o) {
		 if (getAlertDate() == null || o.getAlertDate() == null){
			 return 0;
		 }
			
		 return getAlertDate().compareTo(o.getAlertDate());
		 
		 	/*int dateResult = getAlertDate().compareTo(o.getAlertDate());
	        if (dateResult != 0)
	        {
	            return dateResult;
	        }
		 
		 	int campaignResult = o.getCampaignName().compareTo(getCampaignName());
	        if (campaignResult != 0)
	        {
	            return campaignResult;
	        }

	        int placementResult = o.getPlacementName().compareTo(getPlacementName());
	        if (placementResult != 0)
	        {
	            return placementResult;
	        }

	        return o.getPublisherName().compareTo(getPublisherName());*/

	}



	public long getFlightDailyPacingCurrent() {
		return flightDailyPacingCurrent;
	}



	public void setFlightDailyPacingCurrent(long flightDailyPacingCurrent) {
		this.flightDailyPacingCurrent = flightDailyPacingCurrent;
	}



	public long getFlightDailyPacingExpected() {
		return flightDailyPacingExpected;
	}



	public void setFlightDailyPacingExpected(long flightDailyPacingExpected) {
		this.flightDailyPacingExpected = flightDailyPacingExpected;
	}



	public long getFlightDailyPacingRevised() {
		return flightDailyPacingRevised;
	}



	public void setFlightDailyPacingRevised(long flightDailyPacingRevised) {
		this.flightDailyPacingRevised = flightDailyPacingRevised;
	}



	public long getFlightGoal() {
		return flightGoal;
	}



	public void setFlightGoal(long flightGoal) {
		this.flightGoal = flightGoal;
	}



	public String getFlightStartDate() {
		return flightStartDate;
	}



	public void setFlightStartDate(String flightStartDate) {
		this.flightStartDate = flightStartDate;
	}



	public String getFlightEndDate() {
		return flightEndDate;
	}



	public void setFlightEndDate(String flightEndDate) {
		this.flightEndDate = flightEndDate;
	}



	public long getFlightDeliveryExpected() {
		return flightDeliveryExpected;
	}



	public void setFlightDeliveryExpected(long flightDeliveryExpected) {
		this.flightDeliveryExpected = flightDeliveryExpected;
	}



	public long getFlightDeliveryCurent() {
		return flightDeliveryCurent;
	}



	public void setFlightDeliveryCurent(long flightDeliveryCurent) {
		this.flightDeliveryCurent = flightDeliveryCurent;
	}



	public int getFlightDeliveyAlert() {
		return flightDeliveyAlert;
	}



	public void setFlightDeliveyAlert(int flightDeliveyAlert) {
		this.flightDeliveyAlert = flightDeliveyAlert;
	}



	public int getFlightId() {
		return flightId;
	}



	public void setFlightId(int flightId) {
		this.flightId = flightId;
	}



	public String getCompanyId() {
		return companyId;
	}



	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	@Override
	public boolean equals(Object arg0) {
		// TODO Auto-generated method stub
		return super.equals(arg0);
	}	
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
}
