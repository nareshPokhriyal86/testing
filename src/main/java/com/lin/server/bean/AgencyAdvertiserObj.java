package com.lin.server.bean;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity
@Index
public class AgencyAdvertiserObj implements Serializable{
	@Id	private String id;
	private long agencyId;
	private String agencyName;
	private long advertiserId;
	private String advertiserName;
	private double budget;
	private double revenueDelivered;
	private double revenueRemaining;
	private long impressionsBooked;
	private double clicks;
	private double CTR;
	
	public AgencyAdvertiserObj(){
		
	}
	
	public AgencyAdvertiserObj(long agencyId, String agencyName,
			long advertiserId, String advertiserName, double budget,
			double revenueDelivered, double revenueRemaining,
			long impressionsBooked, double clicks, double cTR) {		
		this.agencyId = agencyId;
		this.agencyName = agencyName;
		this.advertiserId = advertiserId;
		this.advertiserName = advertiserName;
		this.budget = budget;
		this.revenueDelivered = revenueDelivered;
		this.revenueRemaining = revenueRemaining;
		this.impressionsBooked = impressionsBooked;
		this.clicks = clicks;
		CTR = cTR;
	}


	public AgencyAdvertiserObj(String id, long agencyId, String agencyName,
			long advertiserId, String advertiserName, double budget,
			double revenueDelivered, double revenueRemaining,
			long impressionsBooked, double clicks, double cTR) {
		this.id = id;
		this.agencyId = agencyId;
		this.agencyName = agencyName;
		this.advertiserId = advertiserId;
		this.advertiserName = advertiserName;
		this.budget = budget;
		this.revenueDelivered = revenueDelivered;
		this.revenueRemaining = revenueRemaining;
		this.impressionsBooked = impressionsBooked;
		this.clicks = clicks;
		CTR = cTR;
	}



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(long agencyId) {
		this.agencyId = agencyId;
	}

	public String getAgencyName() {
		return agencyName;
	}

	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}

	public long getAdvertiserId() {
		return advertiserId;
	}

	public void setAdvertiserId(long advertiserId) {
		this.advertiserId = advertiserId;
	}

	public String getAdvertiserName() {
		return advertiserName;
	}

	public void setAdvertiserName(String advertiserName) {
		this.advertiserName = advertiserName;
	}

	public double getBudget() {
		return budget;
	}

	public void setBudget(double budget) {
		this.budget = budget;
	}

	public double getRevenueDelivered() {
		return revenueDelivered;
	}

	public void setRevenueDelivered(double revenueDelivered) {
		this.revenueDelivered = revenueDelivered;
	}

	public double getRevenueRemaining() {
		return revenueRemaining;
	}

	public void setRevenueRemaining(double revenueRemaining) {
		this.revenueRemaining = revenueRemaining;
	}

	public long getImpressionsBooked() {
		return impressionsBooked;
	}

	public void setImpressionsBooked(long impressionsBooked) {
		this.impressionsBooked = impressionsBooked;
	}

	public double getClicks() {
		return clicks;
	}

	public void setClicks(double clicks) {
		this.clicks = clicks;
	}

	public double getCTR() {
		return CTR;
	}

	public void setCTR(double cTR) {
		CTR = cTR;
	}

	
}
