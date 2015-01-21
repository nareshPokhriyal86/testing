package com.lin.server.bean;
import java.io.Serializable;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity
@SuppressWarnings("serial")
@Index
public class AdvertiserByMarketObj implements Serializable{
	    @Id	private String id;
		private String state;
		private String linProperty;
		private double ctrPercent;
		private String date;
		
		public AdvertiserByMarketObj(){}
		
		public AdvertiserByMarketObj(String id,String state,String linProperty,double ctrPercent,String date){
			this.id =id;
			this.state =state;
			this.linProperty =linProperty;
			this.ctrPercent =ctrPercent;
			this.date =date;
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
		public String getLinProperty() {
			return linProperty;
		}
		public void setLinProperty(String linProperty) {
			this.linProperty = linProperty;
		}
		public String getState() {
			return state;
		}
		public void setState(String state) {
			this.state = state;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
}
