package com.lin.server.bean;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;


@Entity
@Index
public class ReallocationDataObj implements Serializable {
	@Id	private String id;
	private String date;
	private String lineItem;
	private String size;
	private float ecpm;
	private float budget;
	private long bookedImp;
	private long delivery;
	private int clicks;
	private long overUnder;
	private float ctr;
	private float revenueDelivered;
	private float revenueRemaining;
	private float revisedBudget;
	private long revisedBookedImp;
	private float revenueToBeDelivered;
	
	public ReallocationDataObj(){
		
	}

	
	public ReallocationDataObj(String id, String date, String lineItem,
			String size, float ecpm, float budget, long bookedImp,
			long delivery, int clicks, long overUnder, float ctr,
			float revenueDelivered, float revenueRemaining, float revisedBudget,
			long revisedBookedImp, float revenueToBeDelivered) {
		super();
		this.id = id;
		this.date = date;
		this.lineItem = lineItem;
		this.size = size;
		this.ecpm = ecpm;
		this.budget = budget;
		this.bookedImp = bookedImp;
		this.delivery = delivery;
		this.clicks = clicks;
		this.overUnder = overUnder;
		this.ctr = ctr;
		this.revenueDelivered = revenueDelivered;
		this.revenueRemaining = revenueRemaining;
		this.revisedBudget = revisedBudget;
		this.revisedBookedImp = revisedBookedImp;
		this.revenueToBeDelivered = revenueToBeDelivered;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getLineItem() {
		return lineItem;
	}

	public void setLineItem(String lineItem) {
		this.lineItem = lineItem;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public float getEcpm() {
		return ecpm;
	}

	public void setEcpm(float ecpm) {
		this.ecpm = ecpm;
	}

	public float getBudget() {
		return budget;
	}

	public void setBudget(float budget) {
		this.budget = budget;
	}

	public long getBookedImp() {
		return bookedImp;
	}

	public void setBookedImp(long bookedImp) {
		this.bookedImp = bookedImp;
	}

	public long getDelivery() {
		return delivery;
	}

	public void setDelivery(long delivery) {
		this.delivery = delivery;
	}

	public int getClicks() {
		return clicks;
	}

	public void setClicks(int clicks) {
		this.clicks = clicks;
	}

	public long getOverUnder() {
		return overUnder;
	}

	public void setOverUnder(long overUnder) {
		this.overUnder = overUnder;
	}

	public float getCtr() {
		return ctr;
	}

	public void setCtr(float ctr) {
		this.ctr = ctr;
	}

	public float getRevenueDelivered() {
		return revenueDelivered;
	}

	public void setRevenueDelivered(float revenueDelivered) {
		this.revenueDelivered = revenueDelivered;
	}

	public float getRevenueRemaining() {
		return revenueRemaining;
	}

	public void setRevenueRemaining(float revenueRemaining) {
		this.revenueRemaining = revenueRemaining;
	}

	public float getRevisedBudget() {
		return revisedBudget;
	}

	public void setRevisedBudget(float revisedBudget) {
		this.revisedBudget = revisedBudget;
	}

	public long getRevisedBookedImp() {
		return revisedBookedImp;
	}

	public void setRevisedBookedImp(long revisedBookedImp) {
		this.revisedBookedImp = revisedBookedImp;
	}

	public float getRevenueToBeDelivered() {
		return revenueToBeDelivered;
	}

	public void setRevenueToBeDelivered(float revenueToBeDelivered) {
		this.revenueToBeDelivered = revenueToBeDelivered;
	}

	

}
