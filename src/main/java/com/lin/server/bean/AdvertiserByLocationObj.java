package com.lin.server.bean;
import java.io.Serializable;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity
@SuppressWarnings("serial")
@Index
public class AdvertiserByLocationObj implements Serializable{
	@Id	private String id;

	private String state;
	private long impression;
	private double ctrPercent;
	private String date;
	private String site;
	private String station;
	
	public AdvertiserByLocationObj(){}
	
	public AdvertiserByLocationObj(String id,String state,long impression,double ctrPercent,String date){
		this.id=id;
		this.state=state;
		this.impression=impression;
		this.ctrPercent=ctrPercent;
		this.date=date;
	}
	
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public double getCtrPercent() {
		return ctrPercent;
	}
	public void setCtrPercent(double ctrPercent) {
		this.ctrPercent = ctrPercent;
	}
	public long getImpression() {
		return impression;
	}
	public void setImpression(long impression) {
		this.impression = impression;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getSite() {
		return site;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public String getStation() {
		return station;
	}
}
