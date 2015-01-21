package com.lin.web.dto;

import java.util.List;

import com.lin.server.bean.CreativeObj;

/*
 * This DTO is used to migrate placements from their DFP to datastore for monitoring performance
 * 
 * @author Youdhveer Panwar
 */
public class MigratePlacementDTO{
	
	private long lineItemId;
	private String lineItemName;
	private String lineItemStartDate;
	private String lineItemEndDate;	
	private long orderId;
	private String lineItemType;
	private String rate;
	private  List<CreativeObj> creativeList;
	
	public MigratePlacementDTO(){
	}

	
	public MigratePlacementDTO(long lineItemId, String lineItemName,
			String lineItemStartDate, String lineItemEndDate, long orderId,
			String lineItemType, String rate, List<CreativeObj> creativeList) {
		this.lineItemId = lineItemId;
		this.lineItemName = lineItemName;
		this.lineItemStartDate = lineItemStartDate;
		this.lineItemEndDate = lineItemEndDate;
		this.orderId = orderId;
		this.lineItemType = lineItemType;
		this.rate = rate;
		this.creativeList = creativeList;
	}

	@Override
	public String toString() {
		return "MigratePlacementDTO [lineItemId=" + lineItemId
				+ ", lineItemName=" + lineItemName + ", lineItemStartDate="
				+ lineItemStartDate + ", lineItemEndDate=" + lineItemEndDate
				+ ", orderId=" + orderId + ", lineItemType=" + lineItemType
				+ ", rate=" + rate + ", creativeList=" + creativeList + "]";
	}


	public long getLineItemId() {
		return lineItemId;
	}

	public void setLineItemId(long lineItemId) {
		this.lineItemId = lineItemId;
	}

	public String getLineItemName() {
		return lineItemName;
	}

	public void setLineItemName(String lineItemName) {
		this.lineItemName = lineItemName;
	}

	public String getLineItemStartDate() {
		return lineItemStartDate;
	}

	public void setLineItemStartDate(String lineItemStartDate) {
		this.lineItemStartDate = lineItemStartDate;
	}

	public String getLineItemEndDate() {
		return lineItemEndDate;
	}

	public void setLineItemEndDate(String lineItemEndDate) {
		this.lineItemEndDate = lineItemEndDate;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public List<CreativeObj> getCreativeList() {
		return creativeList;
	}

	public void setCreativeList(List<CreativeObj> creativeList) {
		this.creativeList = creativeList;
	}


	public String getLineItemType() {
		return lineItemType;
	}


	public void setLineItemType(String lineItemType) {
		this.lineItemType = lineItemType;
	}


	public String getRate() {
		return rate;
	}


	public void setRate(String rate) {
		this.rate = rate;
	}

	
	
	
}
